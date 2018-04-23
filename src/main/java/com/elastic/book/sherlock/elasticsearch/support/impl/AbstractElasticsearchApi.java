package com.elastic.book.sherlock.elasticsearch.support.impl;

import com.elastic.book.sherlock.elasticsearch.support.api.Elasticsearch;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liguolin
 * @create 2018-04-13 9:24
 **/
@Component
public abstract class AbstractElasticsearchApi<T> implements Elasticsearch<T> {

    protected String index;
    protected String type;
    @Resource
    protected Client client;

    @Override
    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }


}
