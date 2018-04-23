package com.elastic.book.sherlock.controller.vo;

/**
 * @author liguolin
 * @create 2018-03-29 17:35
 **/
public class PriceRangeInfos {

    private String priceRange;

    private int count;

    private int from;

    private int to;

    private Double avgPrice;

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

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }
}
