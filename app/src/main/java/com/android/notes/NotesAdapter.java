package com.android.notes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.notes.data.NotesContract;

public class NotesAdapter extends CursorAdapter {

    public NotesAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.notes_item_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleTextView = view.findViewById(R.id.note_title);
        TextView descriptionTextView = view.findViewById(R.id.note_text);


        String titleText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.NOTES_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.NOTES_TEXT));

        titleTextView.setText(titleText);
        descriptionTextView.setText(description);


    }
}
