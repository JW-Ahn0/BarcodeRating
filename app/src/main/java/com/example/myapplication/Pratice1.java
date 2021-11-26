package com.example.myapplication;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pratice1 extends AppCompatActivity {
    private RecyclerView pra1top;
    private RecyclerView pra1bottom;

    private ArrayList<ProductModel> pra1TopList;
    private ArrayList<ProductModel> pra1BottomList;
    private Context context;
    private ImageButton textView;

    private DbOpenHelper mDbOpenHelper;

    ProductAdapter pra1TopAdapter;
    ProductAdapter pra1BottomAdapter;

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<List<Product>> mProductlist;
    private List<Product> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratice1);
        pra1top = findViewById(R.id.pra1Top);
        pra1bottom = findViewById(R.id.pra1Bottom);
        textView = findViewById(R.id.pra1ScanButton);

        pra1TopList = new ArrayList<>();
        pra1BottomList = new ArrayList<>();
        context = this;
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        /*
        mDbOpenHelper.insertTimeLine("1111","삼일라면","https://www.samyangfoods.com/upload/product/20180913/20180913135237972039.jpg","4.5");
        */

        pra1TopAdapter = new ProductAdapter(context,pra1TopList,pra1top);
        pra1top.setLayoutManager(new LinearLayoutManager(context));
        pra1top.setAdapter(pra1TopAdapter);

        pra1TopAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                setRetrofitInit();
                TimeLinecallProduct(pra1TopList.get(position).getBarcode());
            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Pratice1.this);
                dialog.setTitle("데이터 삭제")
                        .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n"  )
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Pratice1.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                        mDbOpenHelper.deleteTimeLineName(pra1TopList.get(position).getTitle());
                                        pra1TopList.remove(position);
                                        pra1TopAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Pratice1.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
            }

        });

        /*
        ProductModel productModel2 = new ProductModel("삼다수","http://img.danawa.com/prod_img/500000/821/745/img/11745821_1.jpg?shrink=330:330&_v=20210902124904"
                ,4.5f);
        pra1BottomList.add(productModel2);
        */
        //mDbOpenHelper.insertAD("8808244201045","삼다수","http://img.danawa.com/prod_img/500000/821/745/img/11745821_1.jpg?shrink=330:330&_v=20210902124904","4.0");
        searchTimeLineData(mDbOpenHelper,pra1TopAdapter,pra1TopList);

        pra1BottomAdapter = new ProductAdapter(context,pra1BottomList,pra1bottom);
        pra1bottom.setLayoutManager(new LinearLayoutManager(context));
        pra1bottom.setAdapter(pra1BottomAdapter);
        searchADData(mDbOpenHelper,pra1BottomAdapter,pra1BottomList);
        pra1BottomAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                setRetrofitInit();
                ADcallProduct(pra1BottomList.get(position).getBarcode());
            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {
            }

        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pratice2.class);
                startActivity(intent);
            }
        });
        mDbOpenHelper.close();
    }
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        mDbOpenHelper.open();
        mDbOpenHelper.create();
        pra1TopList.clear();
        searchTimeLineData(mDbOpenHelper,pra1TopAdapter,pra1TopList);
    }
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        mDbOpenHelper.open();
        mDbOpenHelper.create();
        pra1TopList.clear();
        searchTimeLineData(mDbOpenHelper,pra1TopAdapter,pra1TopList);

    }
    public void searchTimeLineData(DbOpenHelper mDbOpenHelper,ProductAdapter praAdapter,ArrayList<ProductModel> productList){
        mDbOpenHelper.open();
        Cursor iCursor = mDbOpenHelper.selectTimeLine();
        Log.d("check", "DB Size: " + iCursor.getCount());
        while(iCursor.moveToNext()){
            @SuppressLint("Range") String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            @SuppressLint("Range") String tempBarcode = iCursor.getString(iCursor.getColumnIndex("barcode"));
            @SuppressLint("Range") String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            @SuppressLint("Range") String tempScore = iCursor.getString(iCursor.getColumnIndex("score"));
            @SuppressLint("Range") String tempImgUrl = iCursor.getString(iCursor.getColumnIndex("imgurl"));
            ProductModel productModel = new ProductModel(tempName,tempImgUrl,Float.valueOf(tempScore),tempBarcode);
            productList.add(productModel);
        }
        praAdapter.notifyDataSetChanged();
    }
    public static void searchADData(DbOpenHelper mDbOpenHelper,ProductAdapter praAdapter,ArrayList<ProductModel> productList){
        mDbOpenHelper.open();
        Cursor iCursor = mDbOpenHelper.selectAD();
        Log.d("check", "DB Size: " + iCursor.getCount());
        while(iCursor.moveToNext()){
            @SuppressLint("Range") String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            @SuppressLint("Range") String tempBarcode = iCursor.getString(iCursor.getColumnIndex("barcode"));
            @SuppressLint("Range") String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            @SuppressLint("Range") String tempScore = iCursor.getString(iCursor.getColumnIndex("score"));
            @SuppressLint("Range") String tempImgUrl = iCursor.getString(iCursor.getColumnIndex("imgurl"));
            ProductModel productModel = new ProductModel(tempName,tempImgUrl,Float.valueOf(tempScore),tempBarcode);
            productList.add(productModel);
        }
        praAdapter.notifyDataSetChanged();
    }
    private void setRetrofitInit(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://20.194.25.208")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
    }
    private void ADcallProduct(String barcode) {

        mProductlist = mRetrofitAPI.getProduct(barcode);
        mProductlist.enqueue(ADCallback);

    }
    private void TimeLinecallProduct(String barcode) {

        mProductlist = mRetrofitAPI.getProduct(barcode);
        mProductlist.enqueue(TimeLineCallback);

    }
    private Callback<List<Product>> ADCallback = new Callback<List<Product>>() {
        @Override
        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
            result = response.body();
            Intent intent = new Intent(context, Pratice4.class);
            intent.putExtra("product", result.get(0));
            context.startActivity(intent);
        }
        @Override
        public void onFailure(Call<List<Product>> call, Throwable t) {
            t.printStackTrace();
            Log.d("check",t.getMessage());
            Toast.makeText(context, "JSON 리턴 타입이 이상합니다.", Toast.LENGTH_SHORT).show();
        }
    };
    private Callback<List<Product>> TimeLineCallback = new Callback<List<Product>>() {
        @Override
        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
            result = response.body();
            Product product = result.get(0);
            if (product.getType().equals("barcode wrong or not in k-net")) {
                Toast.makeText(getApplicationContext(), "코리안넷에 등록되지않은 상품입니다.", Toast.LENGTH_LONG).show();
            }
            else{
                if (product.getType().equals("book") ||result.size()==1) {
                    Intent intent = new Intent(context, Pratice4.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, Pratice3.class);
                    intent.putExtra("productList", (Serializable) result);
                    context.startActivity(intent);
                }
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