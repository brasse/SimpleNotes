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

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class MockCursor implements Cursor {

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deactivate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnIndex(String columnName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnIndexOrThrow(String columnName)
            throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnName(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getColumnNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDouble(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle getExtras() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFloat(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getInt(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLong(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getShort(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getString(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAfterLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBeforeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNull(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move(int offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean moveToFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean moveToLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean moveToNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean moveToPosition(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean moveToPrevious() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean requery() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle respond(Bundle extras) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new UnsupportedOperationException();
    }

}
