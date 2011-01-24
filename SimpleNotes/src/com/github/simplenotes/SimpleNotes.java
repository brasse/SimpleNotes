package com.github.simplenotes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class SimpleNotes extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    public static final int ADD_NOTE_ID = Menu.FIRST;
    public static final int SETTINGS_ID = Menu.FIRST + 1;

    protected NotesDb mNotesDb;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notelist);
        mNotesDb = new NotesDb(this);
        mNotesDb.open();

        // Fill list view with content.
        Cursor countCursor = mNotesDb.countAllNotesCursor();
        startManagingCursor(countCursor);
        Cursor notesCursor = mNotesDb.getAllNotes();
        startManagingCursor(notesCursor);
        NotesCursorAdapter adapter = 
            new NotesCursorAdapter(this, notesCursor, countCursor);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, ADD_NOTE_ID, 0, R.string.add_note);
        menu.add(0, SETTINGS_ID, 0, R.string.settings);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case ADD_NOTE_ID:
            createNote();
            return true;
        case SETTINGS_ID:
            startActivity(new Intent(this, EditPreferences.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, EditNote.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EditNote.class);
        i.putExtra(NotesDb.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

}
