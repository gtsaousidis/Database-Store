package com.database.android.databasestore.data;

import android.provider.BaseColumns;

public final class ItemContract {

    private ItemContract() {}

    /**  Inner class that defines the first table from database */
    public static class CustomersEntryProducts implements BaseColumns {

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
