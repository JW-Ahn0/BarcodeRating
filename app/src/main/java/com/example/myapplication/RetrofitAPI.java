package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {
    @GET("/sr/{barcode}")
    Call<List<Product>> getProduct(@Path("barcode") String barcode);

}
