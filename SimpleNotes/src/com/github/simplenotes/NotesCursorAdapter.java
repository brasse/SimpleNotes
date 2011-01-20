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
import android.widget.ListAdapter;
import android.widget.TextView;

public class NotesCursorAdapter implements ListAdapter {

    private static final String TAG = "NotesCursorAdapter";

    Context ctx;
    Cursor cursor;
    int count;
    List<Note> notes;
    
    public NotesCursorAdapter(Context ctx, Cursor notes, int count) {
        this.ctx = ctx;
        this.cursor = notes;
        this.count = count;
        this.notes = new ArrayList<Note>();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
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
            Note note = NotesDb.noteFrom(cursor);
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
        return 0;
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
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        // TODO Auto-generated method stub
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        // TODO Auto-generated method stub
    }

}
