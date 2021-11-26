package com.example.myapplication;

import android.view.View;

public interface OnRecyclerViewItemClickListener {
    public void onItemClick(MallListViewHolder holder, View view, int position);
    public void onItemClick(ProductViewHolder holder, View view, int position);
    public void onItemLongClick(ProductViewHolder holder, View view, int position);
}
