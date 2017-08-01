package com.hyhy.contenttest.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hyhy.mylibrary.ArticleDbHelper;

import static com.hyhy.mylibrary.ArticleReaderContract.Articles.TABLE_NAME;


/**
 * Created by Administrator on 2017/7/31.
 */

public class ArticlesProvider extends ContentProvider {
    public static final String AUTHORITY = "com.hyhy.contenttest.db.ArticlesProvider";  //标识特定ContentProvider,使用包名使他唯一
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final UriMatcher uriMatcher;


    /**
     * Match Code
     */
    public static final int ARTICLE_ALL = 0;
    public static final int ARTICLE_SINGLE = 1;


    /**
     * MIME
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.hyhy.article";
    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.hyhy.article";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, ARTICLE_ALL);          //匹配记录集合
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", ARTICLE_SINGLE);    //匹配单条记录
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

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case ARTICLE_ALL:
                return CONTENT_TYPE;
            case ARTICLE_SINGLE:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        db = helper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case ARTICLE_ALL:
                //doesn't need any code in my provider.
                break;
            case ARTICLE_SINGLE:
//                long _id  = ContentUris.parseId()
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);
        if (match != ARTICLE_ALL) {
            throw new IllegalArgumentException("Wrong URI:" + uri);
        }
        db = helper.getWritableDatabase();
        long rowId = db.insert(TABLE_NAME, null, values);
        if (rowId > 0) {
            notifyDataChanged();
            return ContentUris.withAppendedId(uri, rowId);
        }
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
