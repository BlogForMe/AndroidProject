package com.us.eoe.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.us.eoe.database.DBHelper;

/**
 * Created by  on 2017/7/21.
 */

public class DeviceInfoProvider extends ContentProvider {
    public static final String AUTHRITY = "com.us.eoe.utils.DeviceInfoProvider";
    public static final Uri LOCATION_CONTENT_URI = Uri.parse("content://" + AUTHRITY + "/location");
    public static final Uri DEVICE_CONTENT_URI = Uri.parse("content://" + AUTHRITY + "/device");
    SQLiteDatabase database;
    private final String table_name = "device";
    private final String location_table = "location";

    @Override
    public boolean onCreate() {
        database = new DBHelper(getContext()).getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        if(uri.equals(DEVICE_CONTENT_URI))
            return database.query(table_name,strings,s,strings1,null,null,null);
        else
            return database.query(location_table,strings,s,strings1,null,null,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        database.insert(location_table,null,contentValues);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int u = database.update(location_table,contentValues,s,strings);
        return u;
    }
}
