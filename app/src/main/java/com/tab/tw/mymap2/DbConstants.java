package com.tab.tw.mymap2;

import android.provider.BaseColumns;

/**
 * Created by Nevic on 2016/4/12.
 */
public interface DbConstants extends BaseColumns {

    // 資料庫名稱
    public static final String DATABASE_NAME = "favorite.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    public static final String _TableName = "favor";
    public static final String NUM = "num";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String DATE = "created";
}