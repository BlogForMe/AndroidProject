package com.hyhy.contenttest.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.hyhy.contenttest.db.ArticleReaderContract.Articles.TABLE_NAME;

/**
 * Created by Administrator on 2017/7/31.
 */

public class ArticlesProvider extends ContentProvider {
    public static final String AUTHORITY = "com.hyhy.contenttest.db";  //标识特定ContentProvider,使用包名使他唯一
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final UriMatcher uriMatcher;


    /**
     * Match Code
     */
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;
    public static final int ITEM_POS = 3;


    /**
     * MIME
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.hyhy.article";
    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.hyhy.article";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "item", ITEM);          //匹配记录集合
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", ITEM_ID);    //匹配单条记录
        uriMatcher.addURI(AUTHORITY, "pos/#", ITEM_POS);
    }

    private ArticleDbHelper helper;
    private SQLiteDatabase db;
    private ContentResolver resolver;

    @Override
    public boolean onCreate() {
        resolver = getContext().getContentResolver();
        helper = new ArticleDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        db = helper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case ITEM:
                //doesn't need any code in my provider.
                break;
            case ITEM_ID:
                break;
            case ITEM_POS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case ITEM:
                return CONTENT_TYPE;
            case ITEM_ID:
            case ITEM_POS:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void notifyDataChanged() {
        getContext().getContentResolver().notifyChange(NOTIFY_URI, null);
    }
}
