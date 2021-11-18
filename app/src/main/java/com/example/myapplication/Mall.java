package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Mall implements Serializable {
    @SerializedName("name")
    private String name ;
    @SerializedName("score")
    private String score;
    @SerializedName("review")
    private String numOfReview;
    @SerializedName("link")
    private String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNumOfReview() {
        return numOfReview;
    }

    public void setNumOfReview(String numOfReview) {
        this.numOfReview = numOfReview;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
