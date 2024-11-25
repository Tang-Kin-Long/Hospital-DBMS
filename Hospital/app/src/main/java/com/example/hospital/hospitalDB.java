package com.example.hospital;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class hospitalDB extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DBNAME="hospital.db";
    private Context mContext;

    public hospitalDB(Context context){
        super(context,DBNAME,null,VERSION);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table user (uid int primary key, pwd varchar(20), role varchar(10))");
        db.execSQL("create table patient (pid int primary key, pName varchar(20), contact_number varchar(20), age int, sex varchar(7))");
        db.execSQL("create table doctor (did int primary key, dName varchar(20), contact_number varchar(20), age int, sex varchar(7), department varchar(20), ack int)");
        db.execSQL("create table nurse (nid int primary key, nName varchar(20), nurse_phone varchar(20))");
        db.execSQL("create table treatment (tid int primary key, date varchar(10), pid int, pName varchar(20), did int, dName varchar(20), pharmacy varchar(20), test_results varchar(20), bed_num int)");
        db.execSQL("create table room (bed_num int primary key, rName varchar(20), nid int, nName varchar(20))");
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        //db.execSQL("drop table if exists user");
        onCreate(db);
    }


}

