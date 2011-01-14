package com.github.simplenotes.test;

import com.github.simplenotelib.Note;
import com.github.simplenotes.NotesDb;

import android.test.AndroidTestCase;

public class NotesDbTest extends AndroidTestCase {

    public void testCanCreateNoteWithoutTagsAndReadIt() {
        NotesDb db = new NotesDb(getContext());
        db.open();
        String content = "foo bar baz";
        long id = db.createNote(content, null);
        assertTrue("Note creation failed.", id != -1);
        Note note = db.getNote(id);
        assertEquals(content, note.getContent());
        assertEquals(false, note.isDeleted());
    }
}
