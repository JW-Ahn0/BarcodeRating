package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgUrl;
    public TextView titleText;
    public RatingBar ratingBar;
    public TextView score;

    public ProductViewHolder(@NonNull View itemView,final OnRecyclerViewItemClickListener listener) {
        super(itemView);
        this.titleText = itemView.findViewById(R.id.title);
        this.imgUrl = itemView.findViewById(R.id.productImage);
        this.ratingBar = itemView.findViewById(R.id.ratingStar);
        this.score = itemView.findViewById(R.id.score);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(listener !=null){
                    listener.onItemClick(ProductViewHolder.this,v,position);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = getAdapterPosition();
                if(listener!=null){
                    listener.onItemLongClick(ProductViewHolder.this,v,position);
                }
                return true;
            }
        });
    }
}
