package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecommendListActivity extends AppCompatActivity {

    private RecyclerView proRecyclerView;
    private RecyclerView recRecyclerView;

    private ArrayList<ProductModel> pList;
    private ArrayList<ProductModel> rList;

    private List<Product> productList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);
        proRecyclerView = findViewById(R.id.mainRecyclerView2);
        recRecyclerView = findViewById(R.id.recRecyclerView);

        pList = new ArrayList<>();
        rList = new ArrayList<>();
        context = this;
        Intent intent = getIntent();
        //인텐트로 전달받는 productList의 사이즈는 최소 2이상을 Main액티비티에서 보장, 아니면 이쪽화면으로 넘어오면 안됨.
        //바코드에 없는 물품찍었을때의 화면도 추가해야함.
        productList = (List<Product>) intent.getSerializableExtra("productList");


        ProductModel productModel = new ProductModel(productList.get(0).getTitle(),productList.get(0).getImg_Url(),Float.valueOf(productList.get(0).getTotal_score()));
        if(!pList.contains(productModel))
        {
            pList.add(productModel);
        }
        ProductAdapter pAdapter = new ProductAdapter(context,pList,proRecyclerView);
        proRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        proRecyclerView.setAdapter(pAdapter);
        pAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                Intent intent = new Intent(context,MallListActivity.class);
                intent.putExtra("product",productList.get(position));
                context.startActivity(intent);
            }
        });
        for(int i = 1 ; i < productList.size();i++){

            productModel = new ProductModel(productList.get(i).getTitle(),productList.get(i).getImg_Url(),Float.valueOf(productList.get(i).getTotal_score()));

            if(!rList.contains(productModel))
            {
                rList.add(productModel);
            }

        }
        ProductAdapter rAdapter = new ProductAdapter(context,rList,recRecyclerView);
        recRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        recRecyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {

            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {
                Intent intent = new Intent(context,MallListActivity.class);
                intent.putExtra("product",productList.get(position+1));
                context.startActivity(intent);
            }
        });

    }

}