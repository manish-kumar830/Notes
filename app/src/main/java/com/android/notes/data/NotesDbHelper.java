package com.android.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.android.notes.data.NotesContract.Notes;

import androidx.annotation.Nullable;

public class NotesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notesDataBase";
    private static final int DATABASE_VERSION = 1;

    public NotesDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_database = "CREATE TABLE " + Notes.TABLE_NAME + " ( "
                + Notes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Notes.NOTES_TITLE + " TEXT,"
                + Notes.NOTES_TEXT + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(create_database);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Notes.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }


}
