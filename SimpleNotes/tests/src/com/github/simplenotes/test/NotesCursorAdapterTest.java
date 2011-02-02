/*
  Copyright (C) 2011  Mattias Svala

  This file is part of SimpleNotes.

  SimpleNotes is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  package com.github.simplenotes;
*/

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
        public boolean isNull(int columnIndex) {
            return rows[position][columnIndex] == null;
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
