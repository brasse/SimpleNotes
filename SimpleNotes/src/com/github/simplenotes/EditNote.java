package com.github.simplenotes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditNote extends Activity {

    private NotesDb notesDb;
    private Long noteId;
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
        noteId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getLong(NotesDb.KEY_ROWID);
        }

        fillContent();
    }

    private void fillContent() {
        if (noteId != null) {
            Note note = notesDb.getNote(noteId);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

}
