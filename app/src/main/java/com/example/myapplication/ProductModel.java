package com.example.myapplication;

public class ProductModel {

    String title;
    String imgurl;
    float score;
    String barcode;

    public ProductModel(String title, String imgurl, float score, String barcode) {
        this.title = title;
        this.imgurl = imgurl;
        this.score = score;
        this.barcode = barcode;
    }

    public ProductModel(String title, String imgurl, float score) {
        this.title = title;
        this.imgurl = imgurl;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
