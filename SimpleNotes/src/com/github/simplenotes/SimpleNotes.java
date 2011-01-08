package com.github.simplenotes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SimpleNotes extends ListActivity {

    public static final int ADD_NOTE_ID = Menu.FIRST;
    public static final int SETTINGS_ID = Menu.FIRST + 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notelist);
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
            startActivity(new Intent(this, EditNote.class));
            return true;
        case SETTINGS_ID:
            startActivity(new Intent(this, EditPreferences.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
