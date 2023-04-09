package com.android.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.notes.data.NotesContract;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ImageView add_notes;
    NotesAdapter adapter;
    ListView notesListView;
    private static final int LOADER_ID = 1;
    TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_notes = findViewById(R.id.add_notes);
        notesListView = findViewById(R.id.notesListView);
        empty_view = findViewById(R.id.empty_view);

        adapter = new NotesAdapter(this,null,0);
        notesListView.setAdapter(adapter);
        notesListView.setEmptyView(empty_view);



        getSupportLoaderManager().initLoader(LOADER_ID,null,this);


        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Uri currentNoteUri = ContentUris.withAppendedId(NotesContract.Notes.CONTENT_URI,id);
                Intent intent = new Intent(MainActivity.this,AddEditNotes.class);
                intent.setData(currentNoteUri);
                startActivity(intent);

            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Do You Want To Delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri deleteUri = ContentUris.withAppendedId(NotesContract.Notes.CONTENT_URI,id);
                        int deletedRow = getContentResolver().delete(deleteUri,null,null);
                        if (deletedRow > 0) {
                            Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();

                return true;
            }
        });

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditNotes.class);
                startActivity(intent);
            }
        });

    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = new String[]{
                NotesContract.Notes._ID,
                NotesContract.Notes.NOTES_TITLE,
                NotesContract.Notes.NOTES_TEXT,
        };

        return new CursorLoader(this, NotesContract.Notes.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.deleteAllNotes) {
            int rowDeleted = getContentResolver().delete(NotesContract.Notes.CONTENT_URI,null,null);

            if (rowDeleted > 0) {
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}