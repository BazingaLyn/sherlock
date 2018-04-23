package com.elastic.book.sherlock.elasticsearch.support.api;

import com.elastic.book.sherlock.elasticsearch.support.domain.EsIndexResponse;
import com.elastic.book.sherlock.elasticsearch.support.domain.SearchParams;
import com.elastic.book.sherlock.model.Page;
import org.elasticsearch.action.ActionResponse;

import java.util.List;

public interface Elasticsearch<T> {

    void setIndex(String index);

    void setType(String type);

    EsIndexResponse index(T t);

    EsIndexResponse indexWithId(T t, String id);

    T getDocumentById(String id, Class<T> clz);

    List<T> multiGetDocumentByIds(List<String> id, Class<T> clz);

    long totalCount();

    Page<T> page(SearchParams searchParams, Class<T> clz);

}
