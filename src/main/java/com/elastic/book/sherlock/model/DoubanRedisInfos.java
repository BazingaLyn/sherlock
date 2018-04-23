package com.elastic.book.sherlock.model;

/**
 * @author liguolin
 * @create 2018-03-24 10:09
 **/
public class DoubanRedisInfos {

    private long hasInEsBookIdCount;

    private long notHasConsumerBookIdCount;

    private long readyConsumerTagCount;

    private long hasConsumerTagCount;

    private long exceptionIdCount;

    public long getHasInEsBookIdCount() {
        return hasInEsBookIdCount;
    }

    public void setHasInEsBookIdCount(long hasInEsBookIdCount) {
        this.hasInEsBookIdCount = hasInEsBookIdCount;
    }

    public long getNotHasConsumerBookIdCount() {
        return notHasConsumerBookIdCount;
    }

    public void setNotHasConsumerBookIdCount(long notHasConsumerBookIdCount) {
        this.notHasConsumerBookIdCount = notHasConsumerBookIdCount;
    }

    public long getReadyConsumerTagCount() {
        return readyConsumerTagCount;
    }

    public void setReadyConsumerTagCount(long readyConsumerTagCount) {
        this.readyConsumerTagCount = readyConsumerTagCount;
    }

    public long getHasConsumerTagCount() {
        return hasConsumerTagCount;
    }

    public void setHasConsumerTagCount(long hasConsumerTagCount) {
        this.hasConsumerTagCount = hasConsumerTagCount;
    }

    public long getExceptionIdCount() {
        return exceptionIdCount;
    }

    public void setExceptionIdCount(long exceptionIdCount) {
        this.exceptionIdCount = exceptionIdCount;
    }
}
