package com.github.simplenotes.test;

import java.util.Arrays;
import java.util.List;

import android.test.AndroidTestCase;

import com.github.simplenotes.Note;
import com.github.simplenotes.NotesDb;

public class NotesDbTest extends AndroidTestCase {

    private NotesDb db;

    public void setUp() {
        db = new NotesDb(getContext());
        db.open();
    }

    public void tearDown() {
        db.close();
    }

    public void testCanCreateNoteWithoutTagsAndReadIt() {
        String content = "foo bar baz";
        long id = db.createNote(content, null);
        assertTrue("Note creation failed.", id != -1);
        Note note = db.getNote(id);
        assertEquals(content, note.getContent());
        assertEquals(0, note.getTags().size());
    }

    public void testCanCreateNoteWithTagsAndReadIt() {
        String content = "foo bar baz";
        List<String> tags = Arrays.asList(new String[] {"foo", "bar"});
        long id = db.createNote(content, tags);
        assertTrue("Note creation failed.", id != -1);
        Note note = db.getNote(id);
        assertEquals(content, note.getContent());
        List<String> noteTags = note.getTags();
        assertEquals(tags.size(), noteTags.size());
        assertEquals(tags.get(0), noteTags.get(0));
        assertEquals(tags.get(1), noteTags.get(1));
    }
}
