package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<Product> mProductlist;
    private Call<BookList> mBooklist;

    private ZXingScannerView scannerView;
    private TextView txtResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        txtResult = (TextView) findViewById(R.id.txt_result);

        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must accept", Toast.LENGTH_SHORT).show();
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onDestroy(){
        scannerView.stopCamera();
        super.onDestroy();
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            scannerView.resumeCameraPreview(MainActivity.this);
        }
    };
    @Override
    public void handleResult(Result rawResult) {
        //here we can receive rawresult
        txtResult.setText(rawResult.getText());

        setRetrofitInit(); //1107추가
        if(rawResult.getText().charAt(0)=='9')
        {
            Log.d("check","콜백 출발");
            callBook(rawResult.getText());
        }
        else{
            callProduct(rawResult.getText());
        }
        //한번하고 멈추니 다시 실행
        Handler mHandler = new Handler();
        mHandler.postDelayed(myTask, 1000);

    }

    /*1112 추가
     */
    private void setRetrofitInit(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://20.194.25.208/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
    }

    private void callProduct(String barcode) {

        mProductlist = mRetrofitAPI.getProduct(barcode);
        mProductlist.enqueue(productCallback);

    }
    private void callBook(String barcode) {

        mBooklist = mRetrofitAPI.getBook(barcode);
        mBooklist.enqueue(bookCallBack);

    }
    private Callback<BookList> bookCallBack = new Callback<BookList>(){


        @Override
        public void onResponse(Call<BookList> call, Response<BookList> response) {
            BookList result = response.body();
            List<Book> book = result.getBookList();
            Log.d("check",book.get(0).getTitle());
        }

        @Override
        public void onFailure(Call<BookList> call, Throwable t) {

        }
    };
    private Callback<Product> productCallback = new Callback<Product>() {

        @Override
        public void onResponse(Call<Product> call, Response<Product> response) {
            Product result = response.body();
            List<Mall> res = result.getMall_List();
            Log.d("check",result.getTitle());
            Log.d("check",result.getImg_Url());
            Log.d("check",Double.toString(result.getTotal()));
            List<Mall> mallList =  result.getMall_List();
            Log.d("check",mallList.get(0).getName());
            Log.d("check",String.valueOf(mallList.get(0).getNumOfReview()) );
            Log.d("check",Double.toString(mallList.get(0).getScore()));
            Log.d("check",mallList.get(0).getLink());
        }
        @Override
        public void onFailure(Call<Product> call, Throwable t) {
            t.printStackTrace();
            Log.d("check",t.getMessage());
        }
    };
}