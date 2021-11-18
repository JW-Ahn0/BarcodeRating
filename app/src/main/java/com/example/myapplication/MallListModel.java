package com.example.myapplication;

public class MallListModel {
    private String score;
    private String review;
    private String mall;

    public MallListModel(String score, String review, String mall) {
        this.score = score;
        this.review = review;
        this.mall = mall;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
