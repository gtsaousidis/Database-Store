package com.database.android.databasestore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.database.android.databasestore.data.ItemContract;
import com.database.android.databasestore.data.ItemDbHelper;

public class MainActivity extends AppCompatActivity {

    private ItemDbHelper mDbHelper;

    // LOG_TAG string for logging reasons
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addItemFab = (FloatingActionButton) findViewById(R.id.fab);

        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemEditor.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    /** Temporary helper method to display info in the onScreen TextView about the state of the database */
    private void displayDatabaseInfo() {

        // To access the database instantiate the subclass of SQLiteOpenHelper
        // and pass in the context, which is the current activity
        mDbHelper = new ItemDbHelper(this);

        // Create and/or open a database to read from
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ItemContract.CustomersEntryProducts._ID,
                ItemContract.CustomersEntryProducts.COLUMN_NAME,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE
        };

        Cursor cursor = db.query(
                ItemContract.CustomersEntryProducts.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayTextView = (TextView) findViewById(R.id.text_view_customer_item);

        try {

            displayTextView.setText("Number of rows in the database table " + cursor.getCount());
            displayTextView.append(ItemContract.CustomersEntryProducts._ID + " - " +
                    ItemContract.CustomersEntryProducts.COLUMN_NAME + " - " +
                    ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY + " - " +
                    ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE + " - " +
                    ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY + " - " +
                    ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER + " - " +
                    ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE);

            int idColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts._ID);
            int nameColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts.COLUMN_NAME);
            int varietyColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY);
            int priceColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER);
            int photoColumnIndex = cursor.getColumnIndex(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE);

            while (cursor.moveToNext()) {

                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentVariety = cursor.getString(varietyColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String blob = cursor.getString(photoColumnIndex);
                Uri pictureUri = Uri.parse(blob);
                ImageView image = (ImageView) findViewById(R.id.imageHolder);
                image.setImageURI(pictureUri);
                /**
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Bitmap currentPicture = BitmapFactory.decodeStream(inputStream);
                ImageView currentImageView = (ImageView) findViewById(R.id.imageHolder);
                currentImageView.setImageBitmap(currentPicture);
                 */

                displayTextView.append(("\n" + currentId + " - " +
                        currentName + " - " +
                        currentVariety + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " ));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertCustomerProduct() {
        mDbHelper = new ItemDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(ItemContract.CustomersEntryProducts.COLUMN_NAME, "Xiaomi Redmi 3 pro");
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY, "Mobile Phone");
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY, 1);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE, 160);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER, "Gearbest");

        long newRowId = db.insert(ItemContract.CustomersEntryProducts.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            // Respond to a click on the "insert dummy data" option
            case R.id.insert_dummy_data:
                insertCustomerProduct();
                return true;
            // Respond to a click on the "delete all entries" option
            case R.id.action_delete_all_entries:
                // Do nothing
                return true;
            case R.id.search_button:
                // Do nothing
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
