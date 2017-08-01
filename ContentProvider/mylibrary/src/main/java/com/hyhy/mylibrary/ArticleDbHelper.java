package com.hyhy.mylibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2017/7/31.
 */

public class ArticleDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Articles.db";

    private static final String TEXT_TYPE = " TEXT";  //千万注意  引号后面的空格，这样系统才知道这个是表示数据类型的
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ArticleReaderContract.Articles.TABLE_NAME + " (" +
            ArticleReaderContract.Articles._ID + " INTEGER PRIMARY KEY," +
            ArticleReaderContract.Articles.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
            ArticleReaderContract.Articles.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            ArticleReaderContract.Articles.COLUMN_NAME_SUBTITLE + TEXT_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ArticleReaderContract.Articles.TABLE_NAME;

    public ArticleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
