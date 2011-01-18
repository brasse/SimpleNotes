package com.github.simplenotes.test;

import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import com.github.simplenotes.Note;
import com.github.simplenotes.NotesCursorAdapter;

public class NotesCursorAdapterTest extends AndroidTestCase {
    class TestCursor extends MockCursor {

        int position;
        Object[][] rows;

        public TestCursor(Object[][] rows) {
            this.position = 0;
            this.rows = rows;
        }

        @Override
        public int getInt(int columnIndex) {
            return (Integer)rows[position][columnIndex];
        }

        @Override
        public long getLong(int columnIndex) {
            Log.i("TEST", "A " + rows.length);
            Log.i("TEST", "B " + rows[0].length);
            Log.i("TEST", "C " + position + " " + columnIndex);
            return (Long)rows[position][columnIndex];
        }

        @Override
        public String getString(int columnIndex) {
            return (String)rows[position][columnIndex];
        }

        @Override
        public boolean isAfterLast() {
            return position >= rows.length;
        }

        @Override
        public boolean moveToNext() {
            position++;
            return true;
        }
    }

    public void testHandlesNotesWithoutTagsCorrectly() {
        Object[][] rows = { 
            new Object[] {0L, "k0", 0, 10L, 20L, 1, 3, 2, "sk", "pk", 
                          "c0", 0, 0, null},
            new Object[] {1L, "k1", 0, 10L, 20L, 1, 3, 2, "sk", "pk", 
                          "c1", 0, 0, null},
        };
        TestCursor cursor = new TestCursor(rows);
        NotesCursorAdapter adapter = 
            new NotesCursorAdapter(new MockContext(), cursor, rows.length);
        Note n = adapter.getItem(1);
        assertEquals(rows[1][0], n.getId());
        n = adapter.getItem(0);
        assertEquals(rows[0][0], n.getId());
    }

}
