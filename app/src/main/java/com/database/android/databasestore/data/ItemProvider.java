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

    private static final String LOG_TAG = ItemProvider.class.getSimpleName();

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
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for" + uri);
        }
    }

    /**
     * Insert an item into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertItem(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(ItemContract.CustomersEntryProducts.COLUMN_NAME);
        String variety = values.getAsString(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY);
        String supplier = values.getAsString(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER);
        if (name == null) {
            throw new IllegalArgumentException("Item must have a name and variety ");
        }

        Integer price = values.getAsInteger(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE);
        Integer quantity = values.getAsInteger(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY);
        if (price != null && price < 0 ) {
            throw new IllegalArgumentException("Item requires valid price!");
        }

        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Item requires valid quantity!");
        }


        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the new item with the given values
        long id = db.insert(ItemContract.CustomersEntryProducts.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
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
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ItemContract.CustomersEntryProducts._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link CustomersEntryProducts#COLUMN_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ItemContract.CustomersEntryProducts.COLUMN_NAME)) {
            String name = values.getAsString(ItemContract.CustomersEntryProducts.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        // If the {@link CustomersEntryProducts#COLUMN_PRICE} key is present,
        // check that the price value is valid.
        if (values.containsKey(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE)) {
            Integer price = values.getAsInteger(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Item requires valid price");
            }
        }

        // If the {@link CustomersEntryProducts#COLUMN_QUANTITY} key is present,
        // check that the quantity value is valid.
        if (values.containsKey(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY)) {
            // Check that the quantity is greater than or equal to 0 kg
            Integer quantity = values.getAsInteger(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Item requires valid quantity");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(ItemContract.CustomersEntryProducts.TABLE_NAME, values, selection, selectionArgs);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
