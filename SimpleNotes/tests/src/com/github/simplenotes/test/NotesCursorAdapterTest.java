package com.github.simplenotes.test;

import android.database.DataSetObserver;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.github.simplenotes.Note;
import com.github.simplenotes.NotesCursorAdapter;

public class NotesCursorAdapterTest extends AndroidTestCase {
    class TestCursor extends MockCursor {

        int position;
        Object[][] rows;

        public TestCursor(Object[][] rows) {
            this.position = -1;
            this.rows = rows;
        }

        @Override
        public boolean moveToFirst() {
            ++position;
            return true;
        }

        @Override
        public int getInt(int columnIndex) {
            return (Integer)rows[position][columnIndex];
        }

        @Override
        public long getLong(int columnIndex) {
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

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }
    }

    public void testHandlesNotesWithoutTagsCorrectly() {
        Object[][] noteRows = { 
            new Object[] {0L, "k0", 0, 10L, 20L, 1, 3, 2, "sk", "pk", 
                          "c0", 0, 0, null},
            new Object[] {1L, "k1", 0, 10L, 20L, 1, 3, 2, "sk", "pk", 
                          "c1", 0, 0, null},
        };
        TestCursor cursor = new TestCursor(noteRows);
        Object[][] countRow = {new Object[] {2}};
        TestCursor countCursor = new TestCursor(countRow);

        NotesCursorAdapter adapter = 
            new NotesCursorAdapter(new MockContext(), cursor, countCursor);
        Note n = adapter.getItem(1);
        assertEquals(noteRows[1][0], n.getId());
        n = adapter.getItem(0);
        assertEquals(noteRows[0][0], n.getId());
    }

}
