package com.android.notes;

public class NotesModel {

    String note_title;
    String note_text;

    public NotesModel(String note_title, String note_text) {
        this.note_title = note_title;
        this.note_text = note_text;
    }

    public String getNoteTitle() {
        return note_title;
    }


    public String getNoteText() {
        return note_text;
    }
}
