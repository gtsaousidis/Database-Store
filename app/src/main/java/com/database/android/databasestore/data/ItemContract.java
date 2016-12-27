package com.database.android.databasestore.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ItemContract {

    private ItemContract() {}

    // Content Authority is used to help identify the content provider
    public static final String CONTENT_AUTHORITY = "com.database.android.databasestore";

    // Concatenate the CONTENT_AUTHORITY constant with the scheme "content://"
    // Create the base content URI which will be shared by every URI associated with PetContract
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // This constants stores the path for each of the tables which will be appended to the base content URI
    public static final String PATH_CUSTOMERS_ENTRY_PRODUCTS = "customersProducts";
    public static final String PATH_SHOP_ENTRY_PRODUCTS = "shopProducts";

    /**  Inner class that defines the first table from database */
    public static class CustomersEntryProducts implements BaseColumns {

        // Create a full URI for the class as a constant called CONTENT_URI.
        // The Uri.withAppendedPath() method appends the BASE_CONTENT_URI (which contains the scheme and the content authority) to the path segment
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CUSTOMERS_ENTRY_PRODUCTS);

        // The names of the columns in this table {customersProducts}
        public static final String TABLE_NAME = "customersProducts";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ITEM_VARIETY = "variety";
        public static final String COLUMN_ITEM_PRICE = "price";
        public static final String COLUMN_ITEM_QUANTITY = "quantity";
        public static final String COLUMN_ITEM_SUPPLIER = "supplier";
        public static final String COLUMN_ITEM_PICTURE = "picture";
    }

    /** Inner class that defines the second table from database */
    public static class ShopEntryContracts implements BaseColumns {

        // Create a full URI for the class as a constant called CONTENT_URI.
        // The Uri.withAppendedPath() method appends the BASE_CONTENT_URI (which contains the scheme and the content authority) to the path segment
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SHOP_ENTRY_PRODUCTS);

        // The names of the columns in this table {ShopProducts}
        public static final String TABLE_NAME = "shopProducts";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ITEM_VARIETY = "variety";
        public static final String COLUMN_ITEM_PRICE = "price";
        public static final String COLUMN_ITEM_QUANTITY = "quantity";
        public static final String COLUMN_ITEM_SUPPLIER = "supplier";
        public static final String COLUMN_ITEM_PICTURE = "picture";
    }
}
