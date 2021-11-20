package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MallListAdapter extends RecyclerView.Adapter<MallListViewHolder> implements OnRecyclerViewItemClickListener{
    Context context;
    ArrayList<MallListModel> mallList;
    RecyclerView recyclerView;
    OnRecyclerViewItemClickListener listener;

    public MallListAdapter(Context context, ArrayList<MallListModel> mallList, RecyclerView recyclerView) {
        this.context = context;
        this.mallList = mallList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mall_recycler,parent,false);
        return new MallListViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull MallListViewHolder holder, int position) {
        MallListModel mallListModel = mallList.get(position);
        holder.ratingBar.setRating(Float.valueOf(mallListModel.getScore()));
        holder.score.setText("별점 : " + mallListModel.getScore());
        holder.review.setText("리뷰 수 : " + mallListModel.getReview());
        Drawable drawable = context.getResources().getDrawable(R.drawable.naver);
        Log.d("check",mallListModel.getMall());
        if(mallListModel.getMall().equals("auction")){
            drawable = context.getResources().getDrawable(R.drawable.auction);

        }
        else if(mallListModel.getMall().equals("coupang")){
            drawable = context.getResources().getDrawable(R.drawable.coupang);
        }
        else if(mallListModel.getMall().equals("gmarket")){
            drawable = context.getResources().getDrawable(R.drawable.gmarket);
        }
        else if(mallListModel.getMall().equals("yes24")){
            drawable = context.getResources().getDrawable(R.drawable.yes24);
        }
        else if(mallListModel.getMall().equals("kyobo")){
            drawable = context.getResources().getDrawable(R.drawable.kyobo);
        }
        holder.mall.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mallList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onItemClick(MallListViewHolder holder, View view, int position) {
        if(listener!=null)
        {
            listener.onItemClick(holder,view,position);
        }
    }

    @Override
    public void onItemClick(ProductViewHolder holder, View view, int position) {
        if(listener!=null)
        {
            listener.onItemClick(holder,view,position);
        }
    }
}
