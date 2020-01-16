package com.RenegadeSloth.mike.chipcounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.RenegadeSloth.mike.chipcounter.DatabaseDescription.*;

public class ChipCounterDatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChipCounter.db";

    public ChipCounterDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        //create the user table
        final String CREATE_AMOUNTS_TABLE =
                "CREATE TABLE " + Amounts.TABLE_NAME + " (" +
                        Amounts._ID + "INTEGER PRIMARY KEY," +
                        Amounts.COLUMN_RECORD + " INTEGER," +
                        Amounts.COLUMN_TWENTYFIVE + " INTEGER," +
                        Amounts.COLUMN_ONEHUNDRED + " INTEGER," +
                        Amounts.COLUMN_FIVEHUNDRED + " INTEGER," +
                        Amounts.COLUMN_ONETHOUSAND + " INTEGER," +
                        Amounts.COLUMN_FIVETHOUSAND + " INTEGER)";
        db.execSQL(CREATE_AMOUNTS_TABLE);

        //create the history table
        final String CREATE_HISTORY_TABLE =
                "CREATE TABLE " + History.TABLE_NAME + " (" +
                        History._ID + "INTEGER PRIMARY KEY," +
                        History.COLUMN_ID + " INTEGER," +
                        History.COLUMN_DATE + " STRING," +
                        History.COLUMN_SPENT + " DOUBLE," +
                        History.COLUMN_WON + " DOUBLE)";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){}
}
