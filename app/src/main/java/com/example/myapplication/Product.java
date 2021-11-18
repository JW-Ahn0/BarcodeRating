package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {


    @SerializedName("type")
    private String type;
    @SerializedName("img_Url")
    private String img_Url;
    @SerializedName("List")
    private List<Mall> mall_List = null;
    @SerializedName("title")
    private String title;
    @SerializedName("total_score")
    private String total_score;
    @SerializedName("total_review")
    private String total_review ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getTotal_review() {
        return total_review;
    }

    public void setTotal_review(String total_review) {
        this.total_review = total_review;
    }

    public List<Mall> getMall_List() {
        return mall_List;
    }

    public void setMall_List(List<Mall> mall_List) {
        this.mall_List = mall_List;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_Url() {
        return img_Url;
    }

    public void setImg_Url(String img_Url) {
        this.img_Url = img_Url;
    }

}