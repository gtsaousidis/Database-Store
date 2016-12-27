package com.database.android.databasestore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class ItemProvider extends ContentProvider {

    private ItemDbHelper mDbHelper;

    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;

    // Creates a UriMatcher object
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /**
         * The calls to addURI() go here, for all of the content uri patterns that the provider
         * should recognise. For this snippet, only the calls to CustomersEntryProducts are implemented
         */
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_CUSTOMERS_ENTRY_PRODUCTS, ITEMS);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_CUSTOMERS_ENTRY_PRODUCTS + "/#", ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = database.query(ItemContract.CustomersEntryProducts.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                Log.i("ItemProvider", "this is the case + " + ITEMS);
                break;
            case ITEM_ID:
                Log.i("ItemProvider", "this is the case + " + ITEM_ID);
                selection = ItemContract.CustomersEntryProducts._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals ? to return a
                // Cursor containing that row of the table.
                cursor = database.query(ItemContract.CustomersEntryProducts.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
