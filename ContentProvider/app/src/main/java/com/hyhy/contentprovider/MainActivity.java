package com.hyhy.contentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private static final String AUTHORITY = "com.hyhy.contenttest.db.FeedProvider";
    private static final Uri ENTRY_ALL_URI = Uri.parse("content://" + AUTHORITY + "/entry");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btQuery(View v) {
        Cursor c = getContentResolver().query(ENTRY_ALL_URI, null, null, null, null);
        while (c.moveToNext()) {
            System.out.println(c.getString(c.getColumnIndex("title")));
        }
    }


}
