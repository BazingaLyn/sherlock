package com.elastic.book.sherlock.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-21 19:22
 **/
public class Book extends ElasticsearchBaseObject implements Serializable {

    private Integer id;

    private String isbn10;

    private String isbn13;

    private String title;

    private String origin_title;

    private String alt_title;

    private String subtitle;

    private String url;

    private String alt;

    private String image;

    private BookImage images;

    private List<String> author;

    private List<String> translator;

    private String publisher;

    private String pubdate;

    private BookRating rating;

    private List<BookTag> tags;

    private String binding;

    private String price;

    private BookSeries series;

    private String pages;

    private String author_intro;

    private String summary;

    private String catalog;

    private String ebook_url;

    private String ebook_price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BookImage getImages() {
        return images;
    }

    public void setImages(BookImage images) {
        this.images = images;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public List<String> getTranslator() {
        return translator;
    }

    public void setTranslator(List<String> translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public BookRating getRating() {
        return rating;
    }

    public void setRating(BookRating rating) {
        this.rating = rating;
    }

    public List<BookTag> getTags() {
        return tags;
    }

    public void setTags(List<BookTag> tags) {
        this.tags = tags;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BookSeries getSeries() {
        return series;
    }

    public void setSeries(BookSeries series) {
        this.series = series;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEbook_url() {
        return ebook_url;
    }

    public void setEbook_url(String ebook_url) {
        this.ebook_url = ebook_url;
    }

    public String getEbook_price() {
        return ebook_price;
    }

    public void setEbook_price(String ebook_price) {
        this.ebook_price = ebook_price;
    }

    private static class BookTag implements Serializable {

        private Integer count;

        private String name;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class BookRating implements Serializable {

        private String max;

        private Integer numRaters;

        private String average;

        private Integer min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public Integer getNumRaters() {
            return numRaters;
        }

        public void setNumRaters(Integer numRaters) {
            this.numRaters = numRaters;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }
    }



    public static class BookImage implements Serializable {

        private String small;

        private String large;

        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
