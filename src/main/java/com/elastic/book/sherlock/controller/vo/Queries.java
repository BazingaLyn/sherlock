package com.elastic.book.sherlock.controller.vo;

/**
 * @author liguolin
 * @create 2018-03-24 15:48
 **/
public class Queries {

    private String wq;

    private int from;

    private int size;

    private Integer filterPriceFrom;

    private Integer filterPriceTo;

    private boolean isMatchAll;

    private String publisherName;



    public String getWq() {
        return wq;
    }

    public void setWq(String wq) {
        this.wq = wq;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

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

    public boolean isMatchAll() {
        return isMatchAll;
    }

    public void setMatchAll(boolean matchAll) {
        isMatchAll = matchAll;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public static final class QueriesBuilder {
        private String wq;
        private int from;
        private int size;
        private Integer filterPriceFrom;
        private Integer filterPriceTo;
        private boolean isMatchAll;
        private String publisherName;

        private QueriesBuilder() {
        }

        public static QueriesBuilder aQueries() {
            return new QueriesBuilder();
        }

        public QueriesBuilder wq(String wq) {
            this.wq = wq;
            return this;
        }

        public QueriesBuilder from(int from) {
            this.from = from;
            return this;
        }

        public QueriesBuilder size(int size) {
            this.size = size;
            return this;
        }

        public QueriesBuilder filterPriceFrom(Integer filterPriceFrom) {
            this.filterPriceFrom = filterPriceFrom;
            return this;
        }

        public QueriesBuilder filterPriceTo(Integer filterPriceTo) {
            this.filterPriceTo = filterPriceTo;
            return this;
        }

        public QueriesBuilder isMatchAll(boolean isMatchAll) {
            this.isMatchAll = isMatchAll;
            return this;
        }

        public QueriesBuilder publisherName(String publisherName) {
            this.publisherName = publisherName;
            return this;
        }

        public Queries build() {
            Queries queries = new Queries();
            queries.setWq(wq);
            queries.setFrom(from);
            queries.setSize(size);
            queries.setFilterPriceFrom(filterPriceFrom);
            queries.setFilterPriceTo(filterPriceTo);
            queries.setPublisherName(publisherName);
            queries.isMatchAll = this.isMatchAll;
            return queries;
        }
    }
}
