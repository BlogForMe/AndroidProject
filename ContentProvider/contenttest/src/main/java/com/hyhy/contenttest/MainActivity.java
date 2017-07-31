package com.hyhy.contenttest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hyhy.contenttest.db.FeedReaderContract;
import com.hyhy.contenttest.db.FeedReaderDbHelper;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        db = mDbHelper.getWritableDatabase();
    }

    public void addDb(View v) {
        ContentValues values;
        for (int i = 1; i < 5; i++) {
            values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COULUMN_NAME_ENTRY_ID, i);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITILE, "heh " + i);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "describe " + i);
            long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.TABLE_NAME, values);
            if (newRowId != -1)
                Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
        }
    }
}
