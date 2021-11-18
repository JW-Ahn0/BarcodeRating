package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {
    @GET("/sr/{barcode}")
    Call<Product> getProduct(@Path("barcode") String barcode);

}
