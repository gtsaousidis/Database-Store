package com.database.android.databasestore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.database.android.databasestore.data.ItemContract;
import com.database.android.databasestore.data.ItemCursorAdapter;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    ItemCursorAdapter mCursorAdapter;
    private static final int ITEM_LOADER = 0;

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

        View emptyView = findViewById(R.id.empty_view);
        ListView listItems = (ListView) findViewById(R.id.list);
        listItems.setEmptyView(emptyView);

        mCursorAdapter = new ItemCursorAdapter(this, null);
        listItems.setAdapter(mCursorAdapter);

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, ItemEditor.class);

                    //
                    Uri currentPetUri = ContentUris.withAppendedId(ItemContract.CustomersEntryProducts.CONTENT_URI, id);
                    intent.setData(currentPetUri);
                    startActivity(intent);
            }
        });

        getLoaderManager().initLoader(ITEM_LOADER, null, this);
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

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ItemContract.CustomersEntryProducts._ID,
                ItemContract.CustomersEntryProducts.COLUMN_NAME,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE
        };

        return new CursorLoader(this,
                ItemContract.CustomersEntryProducts.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
            mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
            mCursorAdapter.swapCursor(null);
    }
}