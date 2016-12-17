package com.database.android.databasestore;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

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
                // Do nothing
            }
        });
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
                // Do nothing
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
