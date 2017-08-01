package com.hyhy.contenttest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hyhy.contenttest.db.ArticleDbHelper;
import com.hyhy.contenttest.db.ArticleReaderContract;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArticleDbHelper mDbHelper = new ArticleDbHelper(this);
        db = mDbHelper.getWritableDatabase();
    }


}
