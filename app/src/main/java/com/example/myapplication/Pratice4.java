package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pratice4 extends AppCompatActivity {

    private RecyclerView pra4top;
    private RecyclerView pra4middle;
    private RecyclerView pra4bottom;

    private ArrayList<ProductModel> pra4TopList;
    private ArrayList<MallListModel> pra4MiddleList;
    private ArrayList<ProductModel> pra4BottomList;

    private DbOpenHelper mDbOpenHelper;

    private Product product;
    private Context context;

    private ImageButton homeButton;
    private ImageButton scanButton;

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<List<Product>> mProductlist;
    private List<Product> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratice4);

        pra4top = findViewById(R.id.pra4Top);
        pra4middle = findViewById(R.id.pra4Middle);
        pra4bottom = findViewById(R.id.pra4Bottom);

        homeButton= findViewById(R.id.pra4HomeButton);
        scanButton = findViewById(R.id.pra4ScanButton);

        pra4TopList = new ArrayList<>();
        pra4MiddleList = new ArrayList<>();
        pra4BottomList = new ArrayList<>();
        context = this;

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        Intent intent = getIntent();
        product =(Product) intent.getSerializableExtra("product");

        ProductModel productModel1 = new ProductModel(product.getTitle(),product.getImg_Url(),Float.valueOf(product.getTotal_score()));
        if(!pra4TopList.contains(productModel1))
        {
            pra4TopList.add(productModel1);
        }
        ProductAdapter pra4TopAdapter = new ProductAdapter(context,pra4TopList,pra4top);
        pra4top.setLayoutManager(new LinearLayoutManager(context));
        pra4top.setAdapter(pra4TopAdapter);

        if(product.getType().equals("book"))
        {
            for(int i =0;i<2;i++)
            {
                MallListModel mallListModel = new MallListModel(product.getMall_List().get(i).getScore(),product.getMall_List().get(i).getNumOfReview(),product.getMall_List().get(i).getName());
                if(!pra4MiddleList.contains(mallListModel))
                {
                    pra4MiddleList.add(mallListModel);
                }
            }
        }
        else{
            for(int i = 0 ; i<4;i++)
            {
                MallListModel mallListModel = new MallListModel(product.getMall_List().get(i).getScore(),product.getMall_List().get(i).getNumOfReview(),product.getMall_List().get(i).getName());
                if(!pra4MiddleList.contains(mallListModel))
                {
                    pra4MiddleList.add(mallListModel);
                }
            }
        }
        MallListAdapter pra4MiddleAdapter = new MallListAdapter(context,pra4MiddleList,pra4middle);
        pra4middle.setLayoutManager(new LinearLayoutManager(context));
        pra4middle.setAdapter(pra4MiddleAdapter);

        pra4MiddleAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getMall_List().get(position).getLink()));
                startActivity(intent1);
            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {

            }
        });

        ProductAdapter pra4BottomAdapter = new ProductAdapter(context,pra4BottomList,pra4bottom);
        pra4bottom.setLayoutManager(new LinearLayoutManager(context));
        pra4bottom.setAdapter(pra4BottomAdapter);
        Pratice1.searchADData(mDbOpenHelper,pra4BottomAdapter,pra4BottomList);
        mDbOpenHelper.close();
        pra4BottomAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                setRetrofitInit();
                ADcallProduct(pra4BottomList.get(position).getBarcode());
            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pratice1.class);
                startActivity(intent);
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pratice2.class);
                startActivity(intent);
            }
        });
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
}