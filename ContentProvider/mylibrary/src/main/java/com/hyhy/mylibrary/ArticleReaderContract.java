package com.hyhy.mylibrary;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2017/7/31.
 */

public class ArticleReaderContract {


    public static abstract class Articles implements BaseColumns {
        public static final String TABLE_NAME = "article";
        public static final String COLUMN_NAME_ENTRY_ID = "_title";
        public static final String COLUMN_NAME_TITLE = "_abstract";
        public static final String COLUMN_NAME_SUBTITLE = "_url";
    }
}
