package com.elastic.book.sherlock.model;

import java.io.Serializable;

/**
 * @author liguolin
 * @create 2018-03-21 19:36
 **/
public class BookSeries implements Serializable {

    private String id;

    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
