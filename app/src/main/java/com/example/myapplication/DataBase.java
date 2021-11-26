package com.example.myapplication;

import android.provider.BaseColumns;

public final class DataBase {
    public static final class CreateDB implements BaseColumns {
        public static final String BARCODE = "barcode";
        public static final String NAME = "name";
        public static final String IMGURL = "imgurl";
        public static final String SCORE = "score";

        public static final String TIMELINE = "timeline";
        public static final String CREATTIMELINE = "create table if not exists " + TIMELINE + "("
                + _ID + " integer primary key autoincrement, "
                + BARCODE + " text not null , "
                + NAME + " text not null , "
                + IMGURL + " text not null , "
                + SCORE + " text not null );";

        public static final String AD = "ad";
        public static final String CREATAD = "create table if not exists " + AD + "("
                + _ID + " integer primary key autoincrement, "
                + BARCODE + " text not null , "
                + NAME + " text not null , "
                + IMGURL + " text not null , "
                + SCORE + " text not null );";
    }
}
