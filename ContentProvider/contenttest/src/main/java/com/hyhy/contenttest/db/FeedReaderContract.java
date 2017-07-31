package com.hyhy.contenttest.db;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2017/7/31.
 */

public class FeedReaderContract {

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COULUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITILE  = "title";
        public static final String COLUMN_NAME_SUBTITLE  = "subtilte";
    }
}
