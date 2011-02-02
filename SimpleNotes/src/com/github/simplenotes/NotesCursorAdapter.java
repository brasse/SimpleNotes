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

package com.github.simplenotes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesCursorAdapter extends BaseAdapter {

    private static final String TAG = "simplenotes.NotesCursorAdapter";

    class Observer extends DataSetObserver {
        boolean observingNotes;

        public Observer(boolean observingNotes) {
            this.observingNotes = observingNotes;
        }

        @Override
        public void onChanged() {
            Log.i(TAG, "Observer.onChange()");
            if (observingNotes) {
                notesUpdated = true;
            } else {
                countUpdated = true;
            }
            startOver();
        }

        @Override
        public void onInvalidated() {
            Log.i(TAG, "Observer.onInvalidated()");
            invalidate();
        }
    }

    private Context ctx;
    private boolean notesUpdated;
    private Cursor notesCursor;
    private boolean countUpdated;
    private Cursor countCursor;
    private int count;
    private List<Note> notes;
    private boolean invalidated;

    public NotesCursorAdapter(Context ctx,
                              Cursor notesCursor, Cursor countCursor) {
        this.ctx = ctx;
        this.notesCursor = notesCursor;
        this.notesUpdated = false;
        this.notesCursor.registerDataSetObserver(new Observer(true));
        this.countCursor = countCursor;
        this.countUpdated = false;
        this.countCursor.registerDataSetObserver(new Observer(false));
        this.count = 0;
        this.notes = new ArrayList<Note>();
        this.invalidated = true;
        initializeCursors();
    }

    private void startOver() {
        if (notesUpdated && countUpdated) {
            Log.i(TAG, "Both cursors updated.");
            notifyDataSetChanged();
            notesUpdated = false;
            countUpdated = false;
            initializeCursors();
            notifyDataSetChanged();
        }
    }

    private void invalidate() {
        if (!invalidated) {
            Log.i(TAG, "notify invalidated.");
            notifyDataSetInvalidated();
            invalidated = true;
        }
    }

    private void initializeCursors() {
        notes.clear();
        notesCursor.moveToFirst();
        countCursor.moveToFirst();
        count = countCursor.getInt(0);
        invalidated = false;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Note getItem(int position) {
        Log.i(TAG, "getItem(" + position + ")");
        if (position < notes.size()) {
            return notes.get(position);
        }

        int cursorPosition = notes.size();
        while (cursorPosition <= position) {
            Note note = NotesDb.noteFrom(notesCursor);
            notes.add(note);
            ++cursorPosition;
        }
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView(" + position + ", ...)");
        // Show contents, or at least start of content.
        Note note = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.notes_row, parent, false);
        TextView content = (TextView)view.findViewById(R.id.content);
        content.setText(note.getContent());

        // Create the tag row.
        ViewGroup layout = (ViewGroup)view.findViewById(R.id.tags);
        Iterator<String> i = note.getTags().iterator();
        while (i.hasNext()) {
            TextView tag = 
                (TextView)inflater.inflate(R.layout.tag, layout, false);
            tag.setText(i.next());
            layout.addView(tag);
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
