package com.tab.tw.mymap2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.tab.tw.mymap2.DbConstants.NUM;
import static com.tab.tw.mymap2.DbConstants.TITLE;
import static com.tab.tw.mymap2.DbConstants.CONTENT;
import static com.tab.tw.mymap2.DbConstants.IMAGE;
import static com.tab.tw.mymap2.DbConstants.DATE;
import static com.tab.tw.mymap2.DbConstants._TableName;
import static com.tab.tw.mymap2.DbConstants.DATABASE_NAME;
import static com.tab.tw.mymap2.DbConstants.VERSION;
/**
 * Created by Nevic on 2016/4/11.
 */
public class MyDBHelper extends SQLiteOpenHelper {


    // 資料庫物件，固定的欄位變數
    private  SQLiteDatabase db;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, null, VERSION);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                NUM + " VARCHAR," +
                TITLE + " VARCHAR," +
                CONTENT +" VARCHAR," +
                IMAGE + " VARCHAR," +
                DATE + " VARCHAR" +
                " );";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS inviteMoreLog"); //刪除舊有的資料表
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO 每次成功打開數據庫後首先被執行
        super.onOpen(db);
    }

    @Override
    public synchronized void close()
    {
        super.close();
    }

    public boolean delete(String rowId)
    {
       return db.delete(_TableName, _ID + "=" + rowId, null) > 0;
    }
}
