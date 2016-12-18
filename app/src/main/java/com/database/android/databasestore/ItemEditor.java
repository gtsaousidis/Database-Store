package com.database.android.databasestore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ItemEditor extends AppCompatActivity {

    // EditText for entering name, variety, price and supplier
    private EditText mNameEditText, mVarietyEditText, mPriceEditText, mSupplierEditText, mQuantityText;

    // Button for entering the picture
    private Button mEnterImageButton;

    // ImageView for showing the picture
    private ImageView mImageView;

    // Quantity for the item. Default quantity: 0
    private int mQuantity = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all the relevant views that is used to read input from user
        mNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mVarietyEditText = (EditText) findViewById(R.id.edit_item_variety);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mSupplierEditText = (EditText) findViewById(R.id.edit_item_supplier);
        mQuantityText = (EditText) findViewById(R.id.edit_quantity_item);
        mEnterImageButton = (Button) findViewById(R.id.insert_image_button);
        mImageView = (ImageView) findViewById(R.id.edit_item_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                return true;
            case R.id.action_delete:
                return true;
            case R.id.home:
                // Navigate back to the parent activity
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
