package com.example.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Eyehunt Team on 03/07/18.
 */
public class MyContactsProvider extends ContentProvider {

    private static final String AUTHORITY = "in.eyehunt.mycontactcontentproviders";
    private static final String BASE_PATH = "contacts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH, CONTACTS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",CONTACT_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DatabaseHandler handler  = new DatabaseHandler(getContext());
        database = handler.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                cursor =  database.query(DatabaseHandler.TABLE_CONTACTS,DatabaseHandler.ALL_COLUMNS,
                        s,null,null,null,DatabaseHandler.CONTACT_NAME +" ASC");
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return "vnd.android.cursor.dir/contacts";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = database.insert(DatabaseHandler.TABLE_CONTACTS,null,contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                delCount =  database.delete(DatabaseHandler.TABLE_CONTACTS,s,strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
