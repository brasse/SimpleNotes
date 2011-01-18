package com.github.simplenotes;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class NotesCursorAdapter implements ListAdapter {

    Context ctx;
    Cursor notes;
    int count;
    
    public NotesCursorAdapter(Context ctx, Cursor notes, int count) {
        this.ctx = ctx;
        this.notes = notes;
        this.count = count;
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
    public Object getItem(int position) {
        // try to leave unimplemented
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // check cache
        // return from cache or create new object
        LayoutInflater inflater = LayoutInflater.from(ctx);
        TextView view = (TextView)inflater.inflate(R.layout.notes_row, parent);
        view.setText("x");
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
