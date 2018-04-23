package com.elastic.book.sherlock.model;

/**
 * @author liguolin
 * @create 2018-04-03 11:29
 **/
public class EsSearchQueries<T> {

    private String wq;

    private int from;

    private int size;

    private boolean isMatchAll;

    private Class<T> clz;


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

    public boolean isMatchAll() {
        return isMatchAll;
    }

    public void setMatchAll(boolean matchAll) {
        isMatchAll = matchAll;
    }

    public Class<T> getClz() {
        return clz;
    }

    public void setClz(Class<T> clz) {
        this.clz = clz;
    }
}
