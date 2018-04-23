package com.elastic.book.sherlock.model;

/**
 * @author liguolin
 * @create 2018-04-11 16:51
 **/
public class ElasticsearchBaseObject {

    private boolean is_recommendation = true;

    private float recommendation_score = 1.0f;

    private int is_delete = 0; // 0 未删除 1 删除

    private long version;

    private float score;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }
}
