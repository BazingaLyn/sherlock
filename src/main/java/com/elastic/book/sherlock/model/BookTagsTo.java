package com.elastic.book.sherlock.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-23 9:26
 **/
public class BookTagsTo {

    private int count;

    private int start;

    private int total;

    private List<BookTags> tags;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BookTags> getTags() {
        return tags;
    }

    public void setTags(List<BookTags> tags) {
        this.tags = tags;
    }

    public static class BookTags {

        private int count;

        private String name;

        private String title;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
