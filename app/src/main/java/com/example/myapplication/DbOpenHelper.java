package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBase.CreateDB.CREATTIMELINE);
            db.execSQL(DataBase.CreateDB.CREATAD);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBase.CreateDB.CREATTIMELINE);
            db.execSQL("DROP TABLE IF EXISTS "+DataBase.CreateDB.CREATAD);
            onCreate(db);
        }
    }
    public DbOpenHelper(Context context){
        this.mCtx = context;
    }
    public DbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public long insertTimeLine(String barcode, String name, String imgurl , String score){
        ContentValues values = new ContentValues();
        values.put(DataBase.CreateDB.BARCODE, barcode);
        values.put(DataBase.CreateDB.NAME, name);
        values.put(DataBase.CreateDB.IMGURL, imgurl);
        values.put(DataBase.CreateDB.SCORE, score);
        return mDB.insert(DataBase.CreateDB.TIMELINE, null, values);
    }
    public long insertAD(String barcode, String name, String imgurl , String score){
        ContentValues values = new ContentValues();
        values.put(DataBase.CreateDB.BARCODE, barcode);
        values.put(DataBase.CreateDB.NAME, name);
        values.put(DataBase.CreateDB.IMGURL, imgurl);
        values.put(DataBase.CreateDB.SCORE, score);
        return mDB.insert(DataBase.CreateDB.AD, null, values);
    }
    public Cursor selectTimeLine(){
        return mDB.query(DataBase.CreateDB.TIMELINE, null, null, null, null, null, null);
    }
    public Cursor selectAD(){
        return mDB.query(DataBase.CreateDB.AD, null, null, null, null, null, null);
    }
    public void deleteTimeLine() {
        mDB.delete(DataBase.CreateDB.TIMELINE, null, null);
    }

    public boolean deleteTimeLineCol(long id){
        return mDB.delete(DataBase.CreateDB.TIMELINE, "_id="+id, null) > 0;
    }
    public void deleteAD() {
        mDB.delete(DataBase.CreateDB.AD, null, null);
    }

    public boolean deleteADCol(long id){
        return mDB.delete(DataBase.CreateDB.AD, "_id="+id, null) > 0;
    }
    public boolean deleteTimeLineName(String name){
        Cursor cursor = selectTimeLine();
        while(cursor.moveToNext()){
            @SuppressLint("Range") String tempName = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String tempID = cursor.getString(cursor.getColumnIndex("_id"));
            if(tempName.equals(name)){
                return mDB.delete(DataBase.CreateDB.TIMELINE, "_id="+tempID, null) > 0;
            }
        }
        return false;
    }
    public boolean deleteADName(String name){
        Cursor cursor = selectAD();
        while(cursor.moveToNext()){
            @SuppressLint("Range") String tempName = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String tempID = cursor.getString(cursor.getColumnIndex("_id"));
            if(tempName.equals(name)){
                return mDB.delete(DataBase.CreateDB.AD, "_id="+tempID, null) > 0;
            }
        }
        return false;
    }
}
