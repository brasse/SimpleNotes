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
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_KEY = "key";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_MODIFYDATE = "modifydate";
    public static final String KEY_CREATEDATE = "createdate";
    public static final String KEY_SYNCNUM = "syncnum";
    public static final String KEY_VERSION = "version";
    public static final String KEY_MINVERSION = "minversion";
    public static final String KEY_SHAREKEY = "sharekey";
    public static final String KEY_PUBLISHKEY = "publishkey";
    public static final String KEY_SYSTEMTAGS = "systemtags";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_DELETED = "deleted";
    public static final String KEY_PINNED = "pinned";
    public static final String KEY_UNREAD = "unread";

    public static final String KEY_NAME = "name";

    public static final String KEY_NOTEID = "noteid";
    public static final String KEY_TAGID = "tagid";
    public static final String KEY_IDX = "idx";

    private static final String DATABASE_NAME = "notes.db";
    private static final String DATABASE_TABLE_NOTE = "note";
    private static final String DATABASE_TABLE_TAG = "tag";
    private static final String DATABASE_TABLE_RELATION = "relation";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_NOTE =
        "create table " + DATABASE_TABLE_NOTE + " (" +
        KEY_ROWID + " integer primary key autoincrement, " +
        KEY_KEY + " text, " + 
        KEY_CONTENT + " text not null, " +
        KEY_MODIFYDATE + " text, " +
        KEY_CREATEDATE + " text, " +
        KEY_SYNCNUM + " integer, " +
        KEY_VERSION + " integer, " +
        KEY_MINVERSION + " integer, " +
        KEY_SHAREKEY + " text, " +
        KEY_PUBLISHKEY + " text, " +
        KEY_DELETED + " integer not null default 0, " +
        KEY_PINNED + " integer not null default 0, " +
        KEY_UNREAD + " integer not null default 0);";

    private static final String DATABASE_CREATE_TAG =
        "create table " + DATABASE_TABLE_TAG + " (" +
        KEY_ROWID + " integer primary key autoincrement, " +
        KEY_NAME + " text not null);";

    private static final String DATABASE_CREATE_RELATION =
        "create table " + DATABASE_TABLE_RELATION + " (" +
        KEY_ROWID + " integer primary key autoincrement, " +
        KEY_NOTEID + " integer not null, " +
        KEY_TAGID + " integer not null, " +
        KEY_IDX + " integer not null);";

    private static final String TAG = "simplenotes.NotesDb";

    private final Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating database.");
            db.execSQL(DATABASE_CREATE_NOTE);
            db.execSQL(DATABASE_CREATE_TAG);
            db.execSQL(DATABASE_CREATE_RELATION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    public NotesDb(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public NotesDb open() throws SQLException {
        Log.i(TAG, "Opening database.");
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    private void clearTags(long noteId) {
        mDb.delete(DATABASE_TABLE_RELATION, KEY_NOTEID + "=" + noteId, null);
    }

    private void addTags(long noteId, List<String> tags) {
        if (tags == null) {
            return;
        }
        ListIterator<String> i = tags.listIterator();
        while (i.hasNext()) {
            int index = i.nextIndex();
            String tagName = i.next();
            Cursor c = mDb.query(DATABASE_TABLE_TAG,
                                 new String[] {KEY_ROWID},
                                 KEY_NAME + "= ?", new String[] {tagName},
                                 null, null, null, null);
            long tagId;
            if (c.moveToFirst()) {
                tagId = c.getLong(0);
            } else {
                ContentValues values = new ContentValues();
                values.put(KEY_NAME, tagName);
                tagId = mDb.insertOrThrow(DATABASE_TABLE_TAG, null, values);
            }
            ContentValues relationValues = new ContentValues();
            relationValues.put(KEY_NOTEID, noteId);
            relationValues.put(KEY_TAGID, tagId);
            relationValues.put(KEY_IDX, index);
            mDb.insertOrThrow(DATABASE_TABLE_RELATION, null, relationValues);
        }
    }

    public void deleteNote(long id) {
        mDb.beginTransaction();
        try {
            mDb.delete(DATABASE_TABLE_RELATION, KEY_NOTEID + "=" + id, null);
            mDb.delete(DATABASE_TABLE_NOTE, KEY_ROWID + "=" + id, null);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    public void deleteAllNotes() {
        mDb.beginTransaction();
        try {
            mDb.delete(DATABASE_TABLE_TAG, null, null);
            mDb.delete(DATABASE_TABLE_NOTE, null, null);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    public long createNote(String content, List<String> tags) {
        mDb.beginTransaction();
        try {
            // Create note.
            ContentValues noteValues = new ContentValues();
            noteValues.put(KEY_CONTENT, content);
            long noteId = 
                mDb.insertOrThrow(DATABASE_TABLE_NOTE, null, noteValues); 
            addTags(noteId, tags);
            mDb.setTransactionSuccessful();
            return noteId;
        } finally {
            mDb.endTransaction();
        }
    }

    private ContentValues makeValues(Note note) {
        ContentValues noteValues = new ContentValues();
        noteValues.put(KEY_KEY, note.getKey());
        noteValues.put(KEY_DELETED, note.isDeleted());
        noteValues.put(KEY_CREATEDATE, dateToLong(note.getCreateDate()));
        noteValues.put(KEY_MODIFYDATE, dateToLong(note.getModifyDate()));
        noteValues.put(KEY_SYNCNUM, note.getSyncNum());
        noteValues.put(KEY_VERSION, note.getVersion());
        noteValues.put(KEY_MINVERSION, note.getMinVersion());
        noteValues.put(KEY_SHAREKEY, note.getShareKey());
        noteValues.put(KEY_PUBLISHKEY, note.getPublishKey());
        noteValues.put(KEY_CONTENT, note.getContent());
        noteValues.put(KEY_PINNED, note.isPinned());
        noteValues.put(KEY_UNREAD, note.isUnread());
        return noteValues;
    }

    private Long dateToLong(Date d) {
        return d != null ? d.getTime() : null;
    }

    public long createNote(Note note) {
        mDb.beginTransaction();
        try {
            // Create note.
            ContentValues noteValues = makeValues(note);
            long noteId = 
                mDb.insertOrThrow(DATABASE_TABLE_NOTE, null, noteValues); 
            addTags(noteId, note.getTags());
            mDb.setTransactionSuccessful();
            note.setId(noteId);
            return noteId;
        } finally {
            mDb.endTransaction();
        }
    }

    public void updateNote(Note note) {
        mDb.beginTransaction();
        try {
            ContentValues noteValues = makeValues(note);
            int rowsUpdated = mDb.update(DATABASE_TABLE_NOTE, noteValues,
                                         KEY_ROWID + "=" + note.getId(), null);
            if (rowsUpdated != 1) {
                throw new SQLException("Exactly 1 row should be updated.");
            }
            clearTags(note.getId());
            addTags(note.getId(), note.getTags());
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    public static Note noteFrom(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setKey(cursor.getString(1));
        note.setDeleted(cursor.getInt(2) != 0);
        if (!cursor.isNull(3)) {
            note.setModifyDate(new Date(cursor.getLong(3)));
        }
        if (!cursor.isNull(4)) {
            note.setCreateDate(new Date(cursor.getLong(4)));
        }
        note.setSyncNum(cursor.getInt(5));
        note.setVersion(cursor.getInt(6));
        note.setMinVersion(cursor.getInt(7));
        note.setShareKey(cursor.getString(8));
        note.setPublishKey(cursor.getString(9));
        note.setContent(cursor.getString(10));

        ArrayList<String> systemTags = new ArrayList<String>();
        if (cursor.getInt(11) != 0) {
            systemTags.add("pinned");
        }
        if (cursor.getInt(12) != 0) {
            systemTags.add("unread");
        }
        note.setSystemTags(systemTags);

        // Get the tags.
        ArrayList<String> tags = new ArrayList<String>();
        while (!cursor.isAfterLast() && cursor.getLong(0) == note.getId()) {
            String tag = cursor.getString(13);
            if (tag != null) {
                tags.add(tag);
            }
            cursor.moveToNext();
        }
        note.setTags(tags);

        return note;
    }

    private static final String QUERY_GET_NOTE =
        "select " +
        DATABASE_TABLE_NOTE + "." + KEY_ROWID + " as noteid, " +
        DATABASE_TABLE_NOTE + "." + KEY_KEY + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_DELETED + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_MODIFYDATE + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_CREATEDATE + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_SYNCNUM + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_VERSION + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_MINVERSION + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_SHAREKEY + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_PUBLISHKEY + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_CONTENT + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_PINNED + " as pinned, "+
        DATABASE_TABLE_NOTE + "." + KEY_UNREAD + ", " +
        DATABASE_TABLE_TAG + "." + KEY_NAME + ", " +
        DATABASE_TABLE_RELATION + "." + KEY_IDX + " as tagidx " +
        " from " + DATABASE_TABLE_NOTE + 
        " left join " + DATABASE_TABLE_RELATION +
        " on (" + DATABASE_TABLE_RELATION + "." + KEY_NOTEID + " = " +
        DATABASE_TABLE_NOTE + "." + KEY_ROWID + ") " +
        " left join " + DATABASE_TABLE_TAG +
        " on (" + DATABASE_TABLE_TAG + "." + KEY_ROWID + " = " +
        DATABASE_TABLE_RELATION + "." + KEY_TAGID +
        ") where " + DATABASE_TABLE_NOTE + "." + KEY_ROWID + " = ? " +
        "order by noteid, tagidx;";

    public Note getNote(long id) {
        Log.i(TAG, QUERY_GET_NOTE);
        Cursor cursor =
            mDb.rawQuery(QUERY_GET_NOTE, new String[] {String.valueOf(id)});
        if (!cursor.moveToFirst()) {
            // Cursor is probably empty.
            return null;
        }
        return noteFrom(cursor);
    }

    public Cursor countAllNotesCursor() {
        return mDb.rawQuery("select count(*) from " + DATABASE_TABLE_NOTE, 
                            null);
    }

    public int countAllNotes() {
        Cursor cursor = countAllNotesCursor();
        try {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }

    private static final String QUERY_GET_ALL_NOTES =
        "select " +
        DATABASE_TABLE_NOTE + "." + KEY_ROWID + " as noteid, " +
        DATABASE_TABLE_NOTE + "." + KEY_KEY + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_DELETED + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_MODIFYDATE + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_CREATEDATE + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_SYNCNUM + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_VERSION + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_MINVERSION + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_SHAREKEY + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_PUBLISHKEY + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_CONTENT + ", " +
        DATABASE_TABLE_NOTE + "." + KEY_PINNED + " as pinned, "+
        DATABASE_TABLE_NOTE + "." + KEY_UNREAD + ", " +
        DATABASE_TABLE_TAG + "." + KEY_NAME + ", " +
        DATABASE_TABLE_RELATION + "." + KEY_IDX + " as tagidx " +
        " from " + DATABASE_TABLE_NOTE + 
        " left join " + DATABASE_TABLE_RELATION +
        " on (" + DATABASE_TABLE_RELATION + "." + KEY_NOTEID + " = " +
        DATABASE_TABLE_NOTE + "." + KEY_ROWID + ") " +
        " left join " + DATABASE_TABLE_TAG +
        " on (" + DATABASE_TABLE_TAG + "." + KEY_ROWID + " = " +
        DATABASE_TABLE_RELATION + "." + KEY_TAGID +
        ") order by pinned desc, noteid, tagidx;";

    public Cursor getAllNotes() {
        return mDb.rawQuery(QUERY_GET_ALL_NOTES, null);
    }
}
