package com.github.simplenotes;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_KEY = "key";
    public static final String KEY_MODIFYDATE = "modifydate";
    public static final String KEY_CREATEDATE = "createdate";
    public static final String KEY_SYNCNUM = "syncnum";
    public static final String KEY_VERSION = "version";
    public static final String KEY_SYSTEMTAGS = "systemtags";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_CONTENT = "content";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " (" +
        KEY_ROWID + " integer primary key autoincrement, " +
        KEY_KEY + " text, " + 
        KEY_MODIFYDATE + " text, " +
        KEY_CREATEDATE + " text, " +
        KEY_SYNCNUM + " integer, " +
        KEY_VERSION + " integer, " +
        KEY_CONTENT + " text);";

    private final Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
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
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createNote(String content, List<String> tags) {
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, content);
        return mDb.insert(DATABASE_TABLE, null, values);
    }
}
