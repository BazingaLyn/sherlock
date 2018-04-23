package com.elastic.book.sherlock.controller.vo;

import java.math.BigDecimal;

/**
 * @author liguolin
 * @create 2018-03-24 15:44
 **/
public class BookVo {

    private String id;

    private String title;

    private String altTitle;

    private String author;

    private String publisher;

    private String pubDate;

    private String price;

    private String bookImg;

//    private long version;

    private float score;

    private boolean is_recommendation;

    private float recommendation_score;

//    public long getVersion() {
//        return version;
//    }
//
//    public void setVersion(long version) {
//        this.version = version;
//    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isIs_recommendation() {
        return is_recommendation;
    }

    public void setIs_recommendation(boolean is_recommendation) {
        this.is_recommendation = is_recommendation;
    }

    public float getRecommendation_score() {
        return recommendation_score;
    }

    public void setRecommendation_score(float recommendation_score) {
        this.recommendation_score = recommendation_score;
    }

    public String getAltTitle() {
        return altTitle;
    }

    public void setAltTitle(String altTitle) {
        this.altTitle = altTitle;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }
}
