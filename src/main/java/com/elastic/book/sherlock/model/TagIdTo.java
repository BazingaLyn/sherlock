package com.elastic.book.sherlock.model;

import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-23 11:07
 **/
public class TagIdTo {

    private int count;

    private int start;

    private int total;

    private List<BookId> books;

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

    public List<BookId> getBooks() {
        return books;
    }

    public void setBooks(List<BookId> books) {
        this.books = books;
    }

    public static class BookId {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}
