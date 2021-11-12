package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Mall {
    @SerializedName("name")
    private String name ;
    @SerializedName("score")
    private double score;
    @SerializedName("review")
    private int numOfReview;
    @SerializedName("link")
    private String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumOfReview() {
        return numOfReview;
    }

    public void setNumOfReview(int numOfReview) {
        this.numOfReview = numOfReview;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
