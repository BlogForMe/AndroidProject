package com.hyhy.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hyhy.mylibrary.ArticleReaderContract;

import static com.hyhy.mylibrary.ArticleReaderContract.Articles.COLUMN_NAME_TITLE;
import static com.hyhy.mylibrary.ArticleReaderContract.Articles.TABLE_NAME;


public class MainActivity extends AppCompatActivity {
    private static final String AUTHORITY = "com.hyhy.contenttest.db.ArticlesProvider";
    private static final Uri ARTICLE_ALL_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resolver = getContentResolver();
    }


    public void addDb(View v) {
        ContentValues values;
        for (int i = 1; i < 5; i++) {
            values = new ContentValues();
            values.put(ArticleReaderContract.Articles.COLUMN_NAME_ENTRY_ID, i);
            values.put(ArticleReaderContract.Articles.COLUMN_NAME_TITLE, "heh " + i);
            values.put(ArticleReaderContract.Articles.COLUMN_NAME_SUBTITLE, "describe " + i);
            resolver.insert(ARTICLE_ALL_URI, values);
        }
        Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
    }

    public void btQueryAll(View v) {
        Cursor c = resolver.query(ARTICLE_ALL_URI, null, null, null, null);
        while (c.moveToNext()) {
            System.out.println(c.getString(c.getColumnIndex(COLUMN_NAME_TITLE)));
        }
    }
}
