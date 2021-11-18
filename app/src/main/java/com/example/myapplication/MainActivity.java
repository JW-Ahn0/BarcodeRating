package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.content.Context;
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

import java.util.ArrayList;
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

    private ZXingScannerView scannerView;

    private RecyclerView recyclerView;
    private ArrayList<ProductModel> list;
    private TextView scanwaiting ;
    private Context context;
    private Product result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        scanwaiting = (TextView) findViewById(R.id.scanwaiting) ;
        context = this;
        list = new ArrayList<>();
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

        setRetrofitInit();
        callProduct(rawResult.getText());
        //한번하고 멈추니 다시 실행
        Handler mHandler = new Handler();
        mHandler.postDelayed(myTask, 1000);

    }

    private void setRetrofitInit(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://20.194.25.208")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
    }

    private void callProduct(String barcode) {

        mProductlist = mRetrofitAPI.getProduct(barcode);
        mProductlist.enqueue(productCallback);

    }

    private Callback<Product> productCallback = new Callback<Product>() {

        @Override
        public void onResponse(Call<Product> call, Response<Product> response) {
            result = response.body();
            Log.d("check",result.getImg_Url());
            recyclerView = findViewById(R.id.recyclerView);
            ProductModel product_model = new ProductModel(result.getTitle(),result.getImg_Url(),Float.valueOf(result.getTotal_score()));
            list.add(product_model);
            ProductAdapter adapter = new ProductAdapter(context,list,recyclerView,result);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            scanwaiting.setText("");
        }
        @Override
        public void onFailure(Call<Product> call, Throwable t) {
            t.printStackTrace();
            Log.d("check",t.getMessage());
        }
    };
}