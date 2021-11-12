package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookList {
    @SerializedName("book")
    private List<Book> bookList;

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
