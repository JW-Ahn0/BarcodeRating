package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> implements OnRecyclerViewItemClickListener {
    Context context;
    ArrayList<ProductModel> list;
    RecyclerView recyclerView ;
    OnRecyclerViewItemClickListener listener;
    public ProductAdapter(Context context, ArrayList<ProductModel> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false);
        return new ProductViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product_model = list.get(position);
        String imageUrl = list.get(position).getImgurl();
        imageUrl = imageUrl.replaceAll("amp;","");
        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.imgUrl);
        holder.titleText.setText((product_model.getTitle()));
        holder.score.setText((String.valueOf(product_model.getScore())));
        holder.ratingBar.setRating(product_model.getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
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

    @Override
    public void onItemLongClick(ProductViewHolder holder, View view, int position) {
        if(listener!=null)
        {
            listener.onItemLongClick(holder,view,position);
        }
    }

}
