package com.android.notes.data;

import android.app.UiAutomation;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class NotesContract {

    public static final String  CONTENT_AUTHORITY = "com.android.notes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String NOTES_PATH = "notes";

    public static class Notes implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,NOTES_PATH);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                                                       CONTENT_AUTHORITY + "/" + NOTES_PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + NOTES_PATH;

        public static final String TABLE_NAME = "notes";

        public static final String _ID = BaseColumns._ID;
        public static final String  NOTES_TITLE = "notes_title";
        public static final String  NOTES_TEXT = "notes_text";

    }

}
