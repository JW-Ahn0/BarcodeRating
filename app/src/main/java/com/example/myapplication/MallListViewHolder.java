package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MallListViewHolder extends RecyclerView.ViewHolder{

    ImageView mall;
    TextView score;
    TextView review;
    RatingBar ratingBar;
    public MallListViewHolder(@NonNull View itemView,final OnRecyclerViewItemClickListener listener) {
        super(itemView);
        this.mall = itemView.findViewById(R.id.mallLogo);
        this.score = itemView.findViewById(R.id.mallScore);
        this.review = itemView.findViewById(R.id.mallReview);
        this.ratingBar = itemView.findViewById(R.id.mallRatingStar);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(listener !=null){
                    listener.onItemClick(MallListViewHolder.this,v,position);
                }
            }
        });
    }
}
