package com.github.simplenotes.test;

import android.test.AndroidTestCase;
import com.github.simplenotes.NotesDb;

public class NotesDbTest extends AndroidTestCase {

    public void testCanCreateNoteWithoutTags() {
        NotesDb db = new NotesDb(getContext());
        db.open();
        long id = db.createNote("foo bar baz", null);
        assertTrue("Note creation failed.", id != -1);
    }
}
