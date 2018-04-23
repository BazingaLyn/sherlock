package com.elastic.book.sherlock.elasticsearch.support.api;


import com.elastic.book.sherlock.controller.vo.Queries;
import com.elastic.book.sherlock.model.EsSearchQueries;
import com.elastic.book.sherlock.model.Page;
import org.elasticsearch.client.Client;

import java.util.List;
import java.util.Map;

 public interface ElasticSearchApi<T> {

	 void setIndex(String index);

	 void setType(String type);

	 Integer index(T t);

	 Integer update(String fieldName, String value, String id);

	 Integer bulkIndex(List<T> ts);

	 Integer indexWithId(T t, String id);

	 String indexAndReturnId(T t);

	 T getDocumentById(String id, Class<T> clz);

	 List<T> multiGetDocumentByIds(List<String> id, Class<T> clz);

	 String searchByTemplate(String scriptLocation, Map<String, Object> params);

	 String searchByCompleteScript(String completeScript);

	 String searchByTemplateAndParams(String scriptLocation, Map<String, Object> params);

	 Boolean deleteById(String id);

	 Boolean deleteAllDatas(String index);

	 String simpleAggregationByTemplate(String templateName, String field);

     List<T> queryAllObjectBySorts(List<String> sortFields, Class<T> clz);

	 String searchSuggester(String scriptLocation, String key);

	 Page<T> page(int from, int pageSize, Class<T> clz);

	 long totalCount();

	 Client getClient();


}
