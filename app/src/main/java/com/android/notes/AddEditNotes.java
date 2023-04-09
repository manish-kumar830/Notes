package com.android.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.notes.data.NotesContract;

public class AddEditNotes extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText title, description;
    Intent intent;
    Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_notes);

        intent = getIntent();
        currentUri = intent.getData();


        if (currentUri == null) {
            setTitle("Add Note");
        } else {
            setTitle("Edit Note");
            getSupportLoaderManager().initLoader(4,null,this);
        }

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save_btn) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveNote() {

        if (currentUri == null) {

            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();

            if (!titleText.isEmpty() && !descriptionText.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put(NotesContract.Notes.NOTES_TITLE, titleText);
                values.put(NotesContract.Notes.NOTES_TEXT, descriptionText);
                Uri uri = getContentResolver().insert(NotesContract.Notes.CONTENT_URI, values);

                if (uri != null) {
                    Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please Fill Title And Description", Toast.LENGTH_SHORT).show();
            }

        } else {

            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();

            if (!titleText.isEmpty() && !descriptionText.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put(NotesContract.Notes.NOTES_TITLE, titleText);
                values.put(NotesContract.Notes.NOTES_TEXT, descriptionText);
                int updatedRow = getContentResolver().update(currentUri, values, null, null);

                if (updatedRow == 0) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } else {
                Toast.makeText(this, "Please Fill Title And Description", Toast.LENGTH_SHORT).show();
            }

        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = new String[]{
                NotesContract.Notes.NOTES_TITLE,
                NotesContract.Notes.NOTES_TEXT
        };

        return new CursorLoader(this, currentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {


        while (cursor.moveToNext()) {
            String titleText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.NOTES_TITLE));
            String descriptionText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.NOTES_TEXT));

            title.setText(titleText);
            description.setText(descriptionText);
        }



    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        title.setText("");
        description.setText("");

    }
}