package com.database.android.databasestore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Products.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_CUSTOMERS_PRODUCTS_TABLE = "CREATE TABLE " + ItemContract.CustomersEntryProducts.TABLE_NAME +
            "(" + ItemContract.CustomersEntryProducts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ItemContract.CustomersEntryProducts.COLUMN_NAME + " TEXT NOT NULL, "
            + ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY + " TEXT NOT NULL, "
            + ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
            + ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL, "
            + ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER + " TEXT NOT NULL, "
            + ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE + " BLOB);";

    private static final String SQL_CREATE_SHOP_PRODUCTS_TABLE = "CREATE TABLE " + ItemContract.ShopEntryContracts.TABLE_NAME + "("
            + ItemContract.ShopEntryContracts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ItemContract.ShopEntryContracts.COLUMN_NAME + " TEXT NOT NULL, "
            + ItemContract.ShopEntryContracts.COLUMN_ITEM_VARIETY + " TEXT NOT NULL, "
            + ItemContract.ShopEntryContracts.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
            + ItemContract.ShopEntryContracts.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL, "
            + ItemContract.ShopEntryContracts.COLUMN_ITEM_SUPPLIER + " TEXT NOT NULL, "
            + ItemContract.ShopEntryContracts.COLUMN_ITEM_PICTURE + " BLOB);";



    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_CUSTOMERS_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_SHOP_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
