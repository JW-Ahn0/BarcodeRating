package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pratice3 extends AppCompatActivity {
    private RecyclerView pra3top;
    private RecyclerView pra3middle;
    private RecyclerView pra3bottom;

    private ArrayList<ProductModel> pra3TopList;
    private ArrayList<ProductModel> pra3MiddleList;
    private ArrayList<ProductModel> pra3BottomList;

    private DbOpenHelper mDbOpenHelper;

    private List<Product> productList;
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
        setContentView(R.layout.activity_pratice3);

        pra3top = findViewById(R.id.pra3Top);
        pra3middle = findViewById(R.id.pra3Middle);
        pra3bottom = findViewById(R.id.pra3Bottom);
        homeButton= findViewById(R.id.pra3HomeButton);
        scanButton = findViewById(R.id.pra3ScanButton);

        pra3TopList = new ArrayList<>();
        pra3MiddleList = new ArrayList<>();
        pra3BottomList = new ArrayList<>();
        context = this;
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();
        Intent intent = getIntent();
        //인텐트로 전달받는 productList의 사이즈는 최소 2이상을 Main액티비티에서 보장, 아니면 이쪽화면으로 넘어오면 안됨.
        //바코드에 없는 물품찍었을때의 화면도 추가해야함.
        productList = (List<Product>) intent.getSerializableExtra("productList");


        ProductModel productModel1 = new ProductModel(productList.get(0).getTitle(),productList.get(0).getImg_Url(),Float.valueOf(productList.get(0).getTotal_score()));
        if(!pra3TopList.contains(productModel1))
        {
            pra3TopList.add(productModel1);
        }
        ProductAdapter pra3TopAdapter = new ProductAdapter(context,pra3TopList,pra3top);
        pra3top.setLayoutManager(new LinearLayoutManager(context));
        pra3top.setAdapter(pra3TopAdapter);
        pra3TopAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                Intent intent = new Intent(context,Pratice4.class);
                intent.putExtra("product",productList.get(position));
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {

            }
        });

        for(int i = 1 ; i < productList.size();i++){

            ProductModel productModel2 = new ProductModel(productList.get(i).getTitle(),productList.get(i).getImg_Url(),Float.valueOf(productList.get(i).getTotal_score()));

            if(!pra3MiddleList.contains(productModel2))
            {
                pra3MiddleList.add(productModel2);
            }

        }

        ProductAdapter pra3MiddleAdapter = new ProductAdapter(context,pra3MiddleList,pra3middle);
        pra3middle.setLayoutManager(new LinearLayoutManager(context));
        pra3middle.setAdapter(pra3MiddleAdapter);
        pra3MiddleAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                Intent intent = new Intent(context,Pratice4.class);
                intent.putExtra("product",productList.get(position+1));
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(ProductViewHolder holder, View view, int position) {

            }
        });



        ProductAdapter pra3BottomAdapter = new ProductAdapter(context,pra3BottomList,pra3bottom);
        pra3bottom.setLayoutManager(new LinearLayoutManager(context));
        pra3bottom.setAdapter(pra3BottomAdapter);
        Pratice1.searchADData(mDbOpenHelper,pra3BottomAdapter,pra3BottomList);
        mDbOpenHelper.close();
        pra3BottomAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                setRetrofitInit();
                ADcallProduct(pra3BottomList.get(position).getBarcode());
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