package com.elastic.book.sherlock.elasticsearch.support.impl.java;

import com.alibaba.fastjson.JSON;
import com.elastic.book.sherlock.elasticsearch.support.domain.EsIndexResponse;
import com.elastic.book.sherlock.elasticsearch.support.domain.SearchParams;
import com.elastic.book.sherlock.elasticsearch.support.impl.AbstractElasticsearchApi;
import com.elastic.book.sherlock.model.Page;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liguolin
 * @create 2018-04-13 9:30
 **/
@Service(value = "nativeJavaElasticsearchApi")
public class NativeJavaElasticsearchApiImpl<T> extends AbstractElasticsearchApi<T> {


    private final static Logger logger = LoggerFactory.getLogger(NativeJavaElasticsearchApiImpl.class);


    @Override
    public EsIndexResponse index(T t) {

        EsIndexResponse esIndexResponse = new EsIndexResponse();

        if(null != index && null != type){
            IndexResponse indexResponse = client.prepareIndex(index, type).setSource(JSON.parseObject(JSON.toJSONString(t),Map.class)).get();
            esIndexResponse.setMsg(indexResponse.status().name());
            esIndexResponse.setId(indexResponse.getId());
            esIndexResponse.setVersion(indexResponse.getVersion());
            esIndexResponse.setSuccess(true);
        }
        return esIndexResponse;
    }

    @Override
    public EsIndexResponse indexWithId(T t, String id) {

        EsIndexResponse esIndexResponse = new EsIndexResponse();

        if(null != index && null != type){
            IndexResponse indexResponse = client.prepareIndex(index, type,id).setSource(JSON.parseObject(JSON.toJSONString(t),Map.class)).get();
            esIndexResponse.setMsg(indexResponse.status().name());
            esIndexResponse.setId(id);
            esIndexResponse.setVersion(indexResponse.getVersion());
            esIndexResponse.setSuccess(true);
        }
        return esIndexResponse;
    }

    @Override
    public T getDocumentById(String id, Class<T> clz) {
        return null;
    }

    @Override
    public List<T> multiGetDocumentByIds(List<String> id, Class<T> clz) {
        return null;
    }

    @Override
    public long totalCount() {
        return 0;
    }

    @Override
    public Page<T> page(SearchParams searchParams, Class<T> clz) {
        return null;
    }

}
