package com.elastic.book.sherlock.model;

/**
 * @author liguolin
 * @create 2018-04-03 11:31
 **/
public class EsBookSearchQueries extends EsSearchQueries<Book> {

    private Integer filterPriceFrom;

    private Integer filterPriceTo;

    private boolean isFilterPrice;

    private boolean isTermPublisher;

    private String publisherName;

    private boolean isHighlight;

    private String highlightPrefix;

    private String highlightSuffix;

    public Integer getFilterPriceFrom() {
        return filterPriceFrom;
    }

    public void setFilterPriceFrom(Integer filterPriceFrom) {
        this.filterPriceFrom = filterPriceFrom;
    }

    public Integer getFilterPriceTo() {
        return filterPriceTo;
    }

    public void setFilterPriceTo(Integer filterPriceTo) {
        this.filterPriceTo = filterPriceTo;
    }

    public boolean isFilterPrice() {
        return isFilterPrice;
    }

    public void setFilterPrice(boolean filterPrice) {
        isFilterPrice = filterPrice;
    }

    public boolean isTermPublisher() {
        return isTermPublisher;
    }

    public void setTermPublisher(boolean termPublisher) {
        isTermPublisher = termPublisher;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
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
