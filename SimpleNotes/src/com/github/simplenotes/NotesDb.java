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

    public static final String KEY_NOTEID = "noteid";
    public static final String KEY_NAME = "name";
    public static final String KEY_POS = "pos";

    private static final String DATABASE_NAME = "notes.db";
    private static final String DATABASE_TABLE_NOTES = "notes";
    private static final String DATABASE_TABLE_TAGS = "tags";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_NOTES =
        "create table " + DATABASE_TABLE_NOTES + " (" +
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

    private static final String DATABASE_CREATE_TAGS =
        "create table " + DATABASE_TABLE_TAGS + " (" +
        KEY_ROWID + " integer primary key autoincrement, " +
        KEY_NAME + " text not null, " + 
        KEY_POS + " integer not null, " + 
        KEY_NOTEID + " integer not null, " +
        "foreign key(" + KEY_NOTEID + 
        ") references " + DATABASE_TABLE_NOTES + " (" + KEY_ROWID + "));";

    private static final String TAG = "NotesDb";

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
            db.execSQL(DATABASE_CREATE_NOTES);
            db.execSQL(DATABASE_CREATE_TAGS);
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

    private void addTags(long noteId, List<String> tags) {
        if (tags != null) {
            ListIterator<String> i = tags.listIterator();
            while (i.hasNext()) {
                int index = i.nextIndex();
                String tag = i.next();
                ContentValues tagValues = new ContentValues();
                tagValues.put(KEY_NAME, tag);
                tagValues.put(KEY_POS, index);
                tagValues.put(KEY_NOTEID, noteId);
                mDb.insertOrThrow(DATABASE_TABLE_TAGS, null, tagValues);
            }
        }
    }

    public long createNote(String content, List<String> tags) {
        mDb.beginTransaction();
        try {
            // Create note.
            ContentValues noteValues = new ContentValues();
            noteValues.put(KEY_CONTENT, content);
            long noteId = 
                mDb.insertOrThrow(DATABASE_TABLE_NOTES, null, noteValues); 
            addTags(noteId, tags);
            mDb.setTransactionSuccessful();
            return noteId;
        } finally {
            mDb.endTransaction();
        }
    }

    public long createNote(Note note) {
        mDb.beginTransaction();
        try {
            // Create note.
            ContentValues noteValues = new ContentValues();
            noteValues.put(KEY_KEY, note.getKey());
            noteValues.put(KEY_DELETED, note.isDeleted());
            noteValues.put(KEY_CREATEDATE, note.getCreateDate().getTime());
            noteValues.put(KEY_MODIFYDATE, note.getModifyDate().getTime());
            noteValues.put(KEY_SYNCNUM, note.getSyncNum());
            noteValues.put(KEY_VERSION, note.getVersion());
            noteValues.put(KEY_MINVERSION, note.getMinVersion());
            noteValues.put(KEY_SHAREKEY, note.getShareKey());
            noteValues.put(KEY_PUBLISHKEY, note.getPublishKey());
            noteValues.put(KEY_CONTENT, note.getContent());
            noteValues.put(KEY_PINNED, note.isPinned());
            noteValues.put(KEY_UNREAD, note.isUnread());
            long noteId = 
                mDb.insertOrThrow(DATABASE_TABLE_NOTES, null, noteValues); 
            addTags(noteId, note.getTags());
            mDb.setTransactionSuccessful();
            return noteId;
        } finally {
            mDb.endTransaction();
        }
    }

    private static final String QUERY_GET_NOTE =
        "select " +
        DATABASE_TABLE_NOTES + "." + KEY_ROWID + " as id, " +
        DATABASE_TABLE_NOTES + "." + KEY_KEY + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_DELETED + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_MODIFYDATE + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_CREATEDATE + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_SYNCNUM + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_VERSION + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_MINVERSION + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_SHAREKEY + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_PUBLISHKEY + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_CONTENT + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_PINNED + ", " +
        DATABASE_TABLE_NOTES + "." + KEY_UNREAD + ", " +
        DATABASE_TABLE_TAGS + "." + KEY_NAME +
        " from " + DATABASE_TABLE_NOTES + " left join " + DATABASE_TABLE_TAGS +
        " on id = " + DATABASE_TABLE_TAGS + "." + KEY_NOTEID + " " +
        "where id = ? order by " + DATABASE_TABLE_TAGS + "." + KEY_POS + ";";

    public Note getNote(long id) {
        Cursor cursor =
            mDb.rawQuery(QUERY_GET_NOTE, new String[] {String.valueOf(id)});
        if (!cursor.moveToFirst()) {
            // Cursor is probably empty.
            return null;
        }
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setKey(cursor.getString(1));
        note.setDeleted(cursor.getInt(2) != 0);
        note.setModifyDate(new Date(cursor.getLong(3)));
        note.setCreateDate(new Date(cursor.getLong(4)));
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
        while (!cursor.isAfterLast()) {
            String tag = cursor.getString(13);
            if (tag != null) {
                tags.add(tag);
            }
            cursor.moveToNext();
        }
        note.setTags(tags);

        cursor.close();
        return note;
    }
}
