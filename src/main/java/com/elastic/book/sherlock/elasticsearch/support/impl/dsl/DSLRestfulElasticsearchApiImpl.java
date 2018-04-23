package com.elastic.book.sherlock.elasticsearch.support.impl.dsl;

import com.elastic.book.sherlock.elasticsearch.support.domain.EsIndexResponse;
import com.elastic.book.sherlock.elasticsearch.support.domain.SearchParams;
import com.elastic.book.sherlock.elasticsearch.support.impl.AbstractElasticsearchApi;
import com.elastic.book.sherlock.model.Page;
import org.elasticsearch.action.ActionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liguolin
 * @create 2018-04-13 9:28
 **/
@Service(value = "dslElasticsearchApi")
public class DSLRestfulElasticsearchApiImpl extends AbstractElasticsearchApi {


    @Override
    public EsIndexResponse index(Object o) {
        return null;
    }

    @Override
    public EsIndexResponse indexWithId(Object o, String id) {
        return null;
    }

    @Override
    public Object getDocumentById(String id, Class clz) {
        return null;
    }

    @Override
    public long totalCount() {
        return 0;
    }

    @Override
    public Page page(SearchParams searchParams, Class clz) {
        return null;
    }

    @Override
    public List multiGetDocumentByIds(List id, Class clz) {
        return null;
    }

}
