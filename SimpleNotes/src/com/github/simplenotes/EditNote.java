package com.github.simplenotes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditNote extends Activity {

    private NotesDb notesDb;
    private Note note;
    private EditText contentText;

    private static final String TAG = "simplenotes.EditNote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notesDb = new NotesDb(this);
        notesDb.open();
        setContentView(R.layout.editnote);
        contentText = (EditText) findViewById(R.id.content);

        Log.i(TAG, "saveInstanceState: " + 
                   (savedInstanceState != null ? "!null" : "null"));
        note = new Note();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long noteId = extras.getLong(NotesDb.KEY_ROWID);
            note.setId(noteId);
        }

        fillContent();
    }

    private void fillContent() {
        if (note.getId() != null) {
            note = notesDb.getNote(note.getId());
            contentText.setText(note.getContent());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        saveNote();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        fillContent();
    }

    private void saveNote() {
        String content = contentText.getText().toString();
        note.setContent(content);
        if (note.getId() == null) {
            long id = notesDb.createNote(note);
            if (id < 0) {
                Log.e(TAG, "Failed to create note.");
            }
        } else {
            notesDb.updateNote(note);
        }

    }
}
