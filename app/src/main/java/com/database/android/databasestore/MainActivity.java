package com.database.android.databasestore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.database.android.databasestore.data.ItemContract;
import com.database.android.databasestore.data.ItemCursorAdapter;
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

        Cursor cursor = getContentResolver().query(ItemContract.CustomersEntryProducts.CONTENT_URI, projection, null, null, null);

        ListView listItems = (ListView) findViewById(R.id.list);

        ItemCursorAdapter adapter = new ItemCursorAdapter(this, cursor);

        listItems.setAdapter(adapter);

    }

    private void insertCustomerProduct() {

        ContentValues values = new ContentValues();
        values.put(ItemContract.CustomersEntryProducts.COLUMN_NAME, "Xiaomi Redmi 3 pro");
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY, "Mobile Phone");
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY, 1);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE, 160);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER, "Gearbest");

        Uri newUri = getContentResolver().insert(ItemContract.CustomersEntryProducts.CONTENT_URI, values);
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
