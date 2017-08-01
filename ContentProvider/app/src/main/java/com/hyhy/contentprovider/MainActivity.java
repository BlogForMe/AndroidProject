package com.hyhy.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String AUTHORITY = "com.hyhy.contenttest.db.FeedProvider";
    private static final Uri ENTRY_ALL_URI = Uri.parse("content://" + AUTHORITY + "/entry");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void addDb(View v) {
        ContentValues values;
//        for (int i = 1; i < 5; i++) {
//            values = new ContentValues();
//            values.put(ArticleReaderContract.Articles.COLUMN_NAME_ENTRY_ID, i);
//            values.put(ArticleReaderContract.Articles.COLUMN_NAME_TITILE, "heh " + i);
//            values.put(ArticleReaderContract.Articles.COLUMN_NAME_SUBTITLE, "describe " + i);
//            long newRowId = db.insert(ArticleReaderContract.Articles.TABLE_NAME, ArticleReaderContract.Articles.TABLE_NAME, values);
//            if (newRowId != -1)
//                Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
//        }
    }

    public void btQuery(View v) {
        Cursor c = getContentResolver().query(ENTRY_ALL_URI, null, null, null, null);
        while (c.moveToNext()) {
            System.out.println(c.getString(c.getColumnIndex("title")));
        }
    }


}
