package com.github.simplenotes;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MyAdapter implements ListAdapter {

    Context ctx;
    String[] data;
    
    public MyAdapter(Context ctx) {
        this.ctx = ctx;
        data = new String[3];
        data[0] = "foo";
        data[1] = "bar";
        data[2] = "baz";
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
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
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
        Log.e("FOO", "getView!!!");
        LayoutInflater inflater = LayoutInflater.from(ctx);
        TextView view = (TextView)inflater.inflate(R.layout.notes_row, parent);
        view.setText(data[position]);
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
        return false;
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
