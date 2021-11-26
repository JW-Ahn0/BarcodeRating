package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pratice2 extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private RecyclerView pra2top;
    private ProductAdapter pra2TopAdapter;
    private ArrayList<ProductModel> pra2TopList;

    private Product product;
    private Context context;

    private ZXingScannerView scannerView;

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<List<Product>> mProductlist;

    private List<Product> result;
    private List<List<Product>> resultlist;

    private Button button;
    private String barcode;

    private DbOpenHelper mDbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratice2);
        pra2top = findViewById(R.id.pra2Top);
        pra2TopList = new ArrayList<>();
        context = this;

        scannerView = (ZXingScannerView) findViewById(R.id.pra2Scanner);
        context = this;
        resultlist = new ArrayList<>();

        button = (Button) findViewById(R.id.pra2Button);

        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(Pratice2.this);
                        scannerView.startCamera();
                        scannerView.setAutoFocus(true);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(Pratice2.this, "You must accept", Toast.LENGTH_SHORT).show();
                        scannerView.setResultHandler(Pratice2.this);
                        scannerView.startCamera();
                        scannerView.setAutoFocus(true);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


        /*
        ProductModel productModel1 = new ProductModel("삼양라면","https://www.samyangfoods.com/upload/product/20180913/20180913135237972039.jpg",5.0f);
        pra2TopList.add(productModel1);
        */
        pra2TopAdapter = new ProductAdapter(context,pra2TopList,pra2top);
        pra2top.setLayoutManager(new LinearLayoutManager(context));
        pra2top.setAdapter(pra2TopAdapter);

        pra2TopAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                try {
                    if (resultlist.get(position).get(0).getType().equals("book") || resultlist.get(position).size() == 1) {
                        Intent intent = new Intent(context, Pratice4.class);
                        intent.putExtra("product", resultlist.get(position).get(0));
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, Pratice3.class);
                        intent.putExtra("productList", (Serializable) resultlist.get(position));
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "터치 관련 에러입니다. 내부에 물품이 존재하지않아요", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Pratice2.this);
                dialog.setTitle("데이터 삭제")
                        .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n"  )
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Pratice2.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                resultlist.remove(position);
                                pra2TopList.remove(position);
                                pra2TopAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Pratice2.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbOpenHelper = new DbOpenHelper(context);
                mDbOpenHelper.open();
                mDbOpenHelper.create();
                for(int i = 0 ; i <pra2TopList.size();i++){
                    mDbOpenHelper.insertTimeLine(pra2TopList.get(i).getBarcode(),pra2TopList.get(i).getTitle(),pra2TopList.get(i).getImgurl(),String.valueOf(pra2TopList.get(i).getScore()));
                }
                mDbOpenHelper.close();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Pratice2.this);
                dialog.setTitle("데이터 추가됨")
                        .setMessage("데이터가 추가되었습니다." + "\n"  )
                        .create()
                        .show();
                Intent intent = new Intent(context, Pratice1.class);
                context.startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
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
            scannerView.resumeCameraPreview(Pratice2.this);
            scannerView.setAutoFocus(true);
        }
    };
    @Override
    public void handleResult(Result rawResult) {
        //한번하고 멈추니 다시 실행
        setRetrofitInit();
        callProduct(rawResult.getText());
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
        this.barcode = barcode;

    }

    private Callback<List<Product>> productCallback = new Callback<List<Product>>() {
        @Override
        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
            result = response.body();
            int i = 0;
            boolean dupliicate = false;
            try {
                while (true) {
                    if (i == resultlist.size()) {
                        break;
                    }
                    if (result.get(0).getTitle().equals(resultlist.get(i).get(0).getTitle())) {
                        dupliicate = true;
                        if (i != 0) {
                            pra2TopList.add(0,pra2TopList.get(i));
                            pra2TopList.remove(i+1);
                            resultlist.add(0,resultlist.get(i));
                            resultlist.remove(i+1);
                            pra2TopAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                    i++;
                }
            } catch (Exception e) {
                Toast.makeText(context, "JSON 리턴 타입이 이상합니다.2", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            try {
                if (!dupliicate) {
                    resultlist.add(0, result);
                    Product product = result.get(0);
                    if (product.getType().equals("barcode wrong or not in k-net")) {
                        Toast.makeText(getApplicationContext(), "코리안넷에 등록되지않은 상품입니다.", Toast.LENGTH_LONG).show();
                        resultlist.remove(0);
                    }
                    else {
                        ProductModel product_model = new ProductModel(product.getTitle(), product.getImg_Url(), Float.valueOf(product.getTotal_score()),barcode);
                        pra2TopList.add(0, product_model);
                        pra2TopAdapter.notifyDataSetChanged();
                        Log.d("checkt",pra2TopList.get(0).getBarcode());
                    }
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "터치 관련 에러입니다. 내부에 물품이 존재하지않아요", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFailure(Call<List<Product>> call, Throwable t) {
            t.printStackTrace();
            Log.d("check",t.getMessage());
            Toast.makeText(context, "JSON 리턴 타입이 이상합니다.", Toast.LENGTH_SHORT).show();
        }
    };
}