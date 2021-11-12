package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

    @SerializedName("title")
    private String title;
    @SerializedName("img_Url")
    private String img_Url;
    @SerializedName("total")
    private double total;
    @SerializedName("mall_List")
    private List<Mall> mall_List = null;

    @Override
    public String toString() {
        return this.getTitle() +"\n" + this.getImg_Url() +"\n" + this.getTotal()+"\n";
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
