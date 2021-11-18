package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MallListActivity extends AppCompatActivity {

    private RecyclerView proRecyclerView;
    private RecyclerView mallRecyclerView;

    private ArrayList<ProductModel> pList;
    private ArrayList<MallListModel> mList;

    private Product product;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_list);
        proRecyclerView = findViewById(R.id.mainRecyclerView);
        mallRecyclerView = findViewById(R.id.mallRecyclerView);

        pList = new ArrayList<>();
        mList = new ArrayList<>();
        context = this;
        Intent intent = getIntent();
        product =(Product) intent.getSerializableExtra("product");

        ProductModel productModel = new ProductModel(product.getTitle(),product.getImg_Url(),Float.valueOf(product.getTotal_score()));
        if(!pList.contains(productModel))
        {
            pList.add(productModel);
        }
        ProductAdapter pAdapter = new ProductAdapter(context,pList,proRecyclerView);
        proRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        proRecyclerView.setAdapter(pAdapter);

        if(product.getType().equals("book"))
        {
            for(int i =0;i<2;i++)
            {
                MallListModel mallListModel = new MallListModel(product.getMall_List().get(i).getScore(),product.getMall_List().get(i).getNumOfReview(),product.getMall_List().get(i).getName());
                if(!mList.contains(mallListModel))
                {
                    mList.add(mallListModel);
                }
            }
        }
        else{
            for(int i = 0 ; i<4;i++)
            {
                MallListModel mallListModel = new MallListModel(product.getMall_List().get(i).getScore(),product.getMall_List().get(i).getNumOfReview(),product.getMall_List().get(i).getName());
                if(!mList.contains(mallListModel))
                {
                    mList.add(mallListModel);
                }
            }
        }
        MallListAdapter mAdapter = new MallListAdapter(context,mList,mallRecyclerView);
        mallRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mallRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(MallListViewHolder holder, View view, int position) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getMall_List().get(position).getLink()));
                startActivity(intent1);
            }

            @Override
            public void onItemClick(ProductViewHolder holder, View view, int position) {

            }
        });
    }
}