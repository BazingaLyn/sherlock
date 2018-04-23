package com.elastic.book.sherlock.elasticsearch.support.domain;

import java.util.Map;

/**
 * @author liguolin
 * @create 2018-04-15 10:15
 **/
public class SearchParams {

    private int from;

    private int to;

    private Map<String,Object> params;

    private boolean isHighlight;

    private String highlightPrefix;

    private String highlightSuffix;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public String getHighlightPrefix() {
        return highlightPrefix;
    }

    public void setHighlightPrefix(String highlightPrefix) {
        this.highlightPrefix = highlightPrefix;
    }

    public String getHighlightSuffix() {
        return highlightSuffix;
    }

    public void setHighlightSuffix(String highlightSuffix) {
        this.highlightSuffix = highlightSuffix;
    }
}
