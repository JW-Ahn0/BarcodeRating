package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    Context context;
    ArrayList<ProductModel> list;
    RecyclerView recyclerView ;
    Product product;
    public ProductAdapter(Context context, ArrayList<ProductModel> list, RecyclerView recyclerView,Product product) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.product = product;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MallListActivity.class);
                intent.putExtra("product",product);
                v.getContext().startActivity(intent);
            }
        });
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product_model = list.get(position);
        String imageUrl = list.get(position).getImgurl();
        imageUrl = imageUrl.replaceAll("amp;","");
        Log.d("check",imageUrl);
        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.imgUrl);
        holder.titleText.setText((product_model.getTitle()));
        holder.score.setText((String.valueOf(product_model.getScore())));
        holder.ratingBar.setRating(product_model.getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
