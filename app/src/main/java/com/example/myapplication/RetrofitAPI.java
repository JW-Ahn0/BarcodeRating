package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {
    @GET("/search/{barcode}")
    Call<Product> getProduct(@Path("barcode") String barcode);

    @GET("/book/{barcode}")
    Call<BookList> getBook(@Path("barcode") String barcode);

    @GET("/")
    Call<Product> gettest();

}
