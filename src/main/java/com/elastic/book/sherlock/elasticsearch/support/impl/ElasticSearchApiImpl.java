package com.elastic.book.sherlock.elasticsearch.support.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elastic.book.sherlock.controller.vo.Queries;
import com.elastic.book.sherlock.elasticsearch.support.api.ElasticSearchApi;
import com.elastic.book.sherlock.model.EsSearchQueries;
import com.elastic.book.sherlock.model.Page;
import com.elastic.book.sherlock.util.FileToJsonStrUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "mixedTempElasticsearchApi")
public class ElasticSearchApiImpl<T> implements ElasticSearchApi<T> {

    private final static Logger logger = LoggerFactory.getLogger(ElasticSearchApiImpl.class);
	

	private String index;
	private String type;
	@Resource
	private Client client;


	@Override
	public Client getClient() {
		return client;
	}

	public Integer index(T t) {
		if(null != index && null != type){
			IndexResponse indexResponse = client.prepareIndex(index, type).setSource(JSON.parseObject(JSON.toJSONString(t),Map.class)).get();
			if(indexResponse.getId() != null){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public Integer update(String fieldName,String value, String id) {
		if(null != index && null != type && null != fieldName && !Strings.isNullOrEmpty(id)){
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index(index);
			updateRequest.type(type);
			updateRequest.id(id);

			try {
				updateRequest.doc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field(fieldName, value)
                        .endObject());
				UpdateResponse updateResponse = client.update(updateRequest).get();
                if(updateResponse.status().getStatus() == RestStatus.OK.getStatus()){
                    return 1;
                }else{
                    logger.error("update failed {}",updateResponse.status().getStatus());
                }
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return 0;
	}

	public Integer indexWithId(T t,String id){
		if(null != index && null != type){
			IndexResponse indexResponse = client.prepareIndex(index, type,id).setSource(JSON.parseObject(JSON.toJSONString(t),Map.class)).get();
			if(indexResponse.getId() != null){
				return 1;
			}
		}
		return 0;
	}

	public Integer bulkIndex(List<T> ts) {
		if(null != index && null != type && null != ts && !ts.isEmpty()){
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for(T t:ts){
				bulkRequest.add(client.prepareIndex(index, type).setSource(JSON.toJSONString(t)));
			}
			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
				throw new RuntimeException("bulk index failed");
			}
			return ts.size();
		}
		return 0;
	}
	
	public String indexAndReturnId(T t) {
		if(null != index && null != type){
			IndexResponse indexResponse = client.prepareIndex(index, type).setSource(JSON.toJSONString(t)).get();
			return indexResponse.getId();
		}
		return null;
	}

	public T getDocumentById(String id,Class<T> clz) {
		GetResponse getResponse = client.prepareGet(index, type, id).get();
		return JSON.parseObject(getResponse.getSourceAsString(), clz);
		
	}

	public List<T> multiGetDocumentByIds(List<String> id,Class<T> clz) {
		List<T> result = null;
		MultiGetRequestBuilder multiGetRequestBuilder = client.prepareMultiGet().add(index, type, id);
		MultiGetResponse multiGetResponse = multiGetRequestBuilder.get();
		if(null != multiGetResponse){
			result = new ArrayList<T>();
			for (MultiGetItemResponse itemResponse : multiGetResponse) {
			    GetResponse response = itemResponse.getResponse();
			    if (response.isExists()) {                      
			        String json = response.getSourceAsString(); 
			        result.add(JSON.parseObject(json, clz));
			    }
			}
		}
		return result;
	}

	@Override
	public long totalCount() {
		String nativeValue = searchByTemplate("es_count.json",null);

		if(null != nativeValue){
			JSONObject jsonObject = JSONObject.parseObject(nativeValue);
			return jsonObject.getJSONObject("hits").getLong("total");
		}
		return 0;
	}


	public String searchByTemplateAndParams(String scriptLocation, Map<String, Object> params) {
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript(FileToJsonStrUtils.getJSONStr(scriptLocation,params))
				.setScriptType(ScriptType.INLINE)
				.setRequest(new SearchRequest())
				.get()
				.getResponse();
		if(sr != null){
			return  sr.toString();
		}
		return null;
	}


	@Override
	public String searchByCompleteScript(String completeScript) {
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript(completeScript)
				.setScriptType(ScriptType.INLINE)
				.setRequest(new SearchRequest())
				.get()
				.getResponse();
		if(sr != null){
			return  sr.toString();
		}
		return null;

	}

	public String searchByTemplate(String scriptLocation, Map<String, Object> params) {
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
		        .setScript(FileToJsonStrUtils.getJSONStr(scriptLocation))
		        .setScriptType(ScriptType.INLINE)
		        .setScriptParams(params)                  
		        .setRequest(new SearchRequest())
		        .get()                                             
		        .getResponse();   
		if(sr != null){
			return  sr.toString();
		}
		return null;
	}

	public Boolean deleteById(String id) {
		DeleteResponse response = client.prepareDelete(index, type, id).get();
		return response.getResult().getLowercase().equals("deleted");
	}

	@Override
	public Boolean deleteAllDatas(String index) {
		BulkByScrollResponse response =
				DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
						.filter(QueryBuilders.matchAllQuery())
						.source(index)
						.get();
		long deleted = response.getDeleted();
		return deleted > 0 ? true : false;
	}

	public String simpleAggregationByTemplate(String templateName, String fieldName) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("aggsField",fieldName);
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
		        .setScript(FileToJsonStrUtils.getJSONStr(templateName))
		        .setScriptType(ScriptType.INLINE)
		        .setScriptParams(params)                  
		        .setRequest(new SearchRequest())
		        .get()                                             
		        .getResponse();
		return sr.toString();
	}

	@Override
	public List<T> queryAllObjectBySorts(List<String> sortFields,Class<T> clz) {

		List<T> result = new ArrayList<>();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type).setFrom(0).setSize(20).setQuery(QueryBuilders.matchAllQuery());
		if(!CollectionUtils.isEmpty(sortFields)){
			for(String eachField : sortFields){
				searchRequestBuilder.addSort(eachField, SortOrder.DESC);
			}
		}

		SearchResponse response = searchRequestBuilder.setExplain(true).get();
		if(null != response){
			SearchHits searchHits = response.getHits();
			SearchHit[] hits = searchHits.getHits();
			for (int i = 0;i < hits.length;i++){
				T t = JSON.parseObject(hits[i].getSourceAsString(),clz);
				result.add(t);
			}
		}
		return result;
	}

	@Override
	public String searchSuggester(String scriptLocation, String key) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("key",key);
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript(FileToJsonStrUtils.getJSONStr(scriptLocation))
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(params)
				.setRequest(new SearchRequest())
				.get()
				.getResponse();
		return sr.toString();
	}

	@Override
	public Page<T> page(int from, int pageSize, Class<T> clz) {

		Page<T> page = new Page<>();

		List<T> result = new ArrayList<>();

		SearchResponse countResponse = client.prepareSearch(index).setSource(new SearchSourceBuilder().size(0).query(QueryBuilders.matchAllQuery())).get();

		if(countResponse != null){
			long totalCount = countResponse.getHits().getTotalHits();
			if(totalCount > 0){
				SearchResponse response = client.prepareSearch(index)
						.setTypes(type)
						.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						.setQuery(QueryBuilders.matchAllQuery())                 // Query
						.setFrom(from).setSize(pageSize).setExplain(true)
						.get();
				if(null != response){

					SearchHits searchHits = response.getHits();
					SearchHit[] hits = searchHits.getHits();
					for (int i = 0;i < hits.length;i++){
						T t = JSON.parseObject(hits[i].getSourceAsString(),clz);
						result.add(t);
					}
					page.setRoot(result);
					page.setTotal((int)totalCount);
					page.setCurrentPage((from / pageSize) +(from % pageSize == 0? 0:1));
					page.setLimit(pageSize);
					return page;
				}
			}
		}
		return new Page<>();

	}


	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
