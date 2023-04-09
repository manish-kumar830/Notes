package com.android.notes.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;
import com.android.notes.data.NotesContract.Notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesProvider extends ContentProvider {

    private NotesDbHelper dbHelper;
    private static final int WITHOUT_ID = 100;
    private static final int WITH_ID = 101;


    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY,NotesContract.NOTES_PATH,WITHOUT_ID);
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY,NotesContract.NOTES_PATH+"/#",WITH_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new NotesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        sortOrder = Notes._ID + " DESC";
        
        switch (match){
            case WITHOUT_ID:
                cursor = database.query(Notes.TABLE_NAME,
                         projection,
                         null,
                         null,
                        null,
                        null,
                        sortOrder);
                break;

            case WITH_ID:
                selection = Notes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(Notes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + match);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);
        switch (match){

            case WITH_ID:
                return Notes.CONTENT_ITEM_TYPE;
            case WITHOUT_ID:
                return Notes.CONTENT_LIST_TYPE;
            default:
                throw new IllegalStateException("Unexpected value: ");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        long result;

        switch (match){
            case WITHOUT_ID:
                SQLiteDatabase helper = dbHelper.getWritableDatabase();
                result = helper.insert(NotesContract.Notes.TABLE_NAME,null,contentValues);
                if (result == -1) {
                    return null;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + match);
        }


        getContext().getContentResolver().notifyChange(uri,null);


        return ContentUris.withAppendedId(uri,result);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int deleted = 0;

        switch (match){
            case WITH_ID:
                where = Notes._ID + "=?";
                whereArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deleted = database.delete(Notes.TABLE_NAME,where,whereArgs);
                break;

            case WITHOUT_ID:
                deleted = database.delete(Notes.TABLE_NAME,null,null);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + match);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int updatedRowId = 0;
        switch (match){

            case WITHOUT_ID:
                updatedRowId = update(uri,contentValues,null,null);
                break;

            case WITH_ID:
                where = Notes._ID+"=?";
                whereArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                updatedRowId = database.update(Notes.TABLE_NAME,contentValues,where,whereArgs);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + match);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return updatedRowId;
    }
}
