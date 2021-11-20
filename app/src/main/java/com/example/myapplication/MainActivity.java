package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<List<Product>> mProductlist;

    private ZXingScannerView scannerView;

    private RecyclerView recyclerView;
    private ArrayList<ProductModel> list;
    private TextView scanwaiting ;
    private Context context;
    private List<Product> result;
    private List<List<Product>> resultlist;


    ProductAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        scanwaiting = (TextView) findViewById(R.id.scanwaiting) ;
        recyclerView = findViewById(R.id.recyclerView);
        context = this;
        list = new ArrayList<>();
        resultlist = new ArrayList<>();
        adapter = new ProductAdapter(context, list, recyclerView);
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                        scannerView.setAutoFocus(true);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must accept", Toast.LENGTH_SHORT).show();
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                        scannerView.setAutoFocus(true);
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
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Handler mHandler = new Handler();
        mHandler.postDelayed(myTask, 1000);
    }
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        Handler mHandler = new Handler();
        mHandler.postDelayed(myTask, 1000);
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            scannerView.resumeCameraPreview(MainActivity.this);
            scannerView.setAutoFocus(true);
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

    private Callback<List<Product>> productCallback = new Callback<List<Product>>() {

        @Override
        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
            result = response.body();

            int i = 0;
            boolean dupliicate = false;
            try {
                while (true) {
                    Log.d("check", String.valueOf(resultlist.size()) + "and " + String.valueOf(i));
                    if (i == resultlist.size()) {
                        break;
                    }
                    if (result.get(0).getTitle().equals(resultlist.get(i).get(0).getTitle())) {
                        dupliicate = true;
                        if (i != 0) {
                            Collections.swap(list, i, 0);
                            Collections.swap(resultlist, i, 0);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    }
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!dupliicate) {
                    resultlist.add(0, result);
                    Product product = result.get(0);

                    if (product.getType().equals("barcode wrong or not in k-net")) {
                        scanwaiting.setText("존재하지 않는 상품입니다.");
                    } else {
                        ProductModel product_model = new ProductModel(product.getTitle(), product.getImg_Url(), Float.valueOf(product.getTotal_score()));
                        list.add(0, product_model);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapter);
                        scanwaiting.setText("");
                        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(MallListViewHolder holder, View view, int position) {
                            }

                            @Override
                            public void onItemClick(ProductViewHolder holder, View view,
                                                    int position) {
                                Log.d("check", String.valueOf(position));
                                try {
                                    if (resultlist.get(position).get(0).getType().equals("book") || resultlist.get(position).size() == 1) {
                                        Intent intent = new Intent(context, MallListActivity.class);
                                        intent.putExtra("product", resultlist.get(position).get(0));
                                        context.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(context, RecommendListActivity.class);
                                        intent.putExtra("productList", (Serializable) resultlist.get(position));
                                        context.startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    Log.d("check", "터치관련에러");
                                }

                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            catch (Exception e){
                scanwaiting.setText("다시 스캔해주세요!");
            }
        }
        @Override
        public void onFailure(Call<List<Product>> call, Throwable t) {
            t.printStackTrace();
            Log.d("check",t.getMessage());
        }
    };
}