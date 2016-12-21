package com.database.android.databasestore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.database.android.databasestore.data.ItemContract;
import com.database.android.databasestore.data.ItemDbHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ItemEditor extends AppCompatActivity {

    private final static int ACTIVITY_SELECT_IMAGE = 100;

    // EditText for entering name, variety, price and supplier
    private EditText mNameEditText, mVarietyEditText, mPriceEditText, mSupplierEditText, mQuantityText;

    // Button for entering the picture
    private Button mEnterImageButton;

    // ImageView for showing the picture
    private ImageView mImageView;

    // Quantity for the item. Default quantity: 0
    private int mQuantity = 0;

    private ItemDbHelper mDbHelper;

    private String path = "";

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

        mEnterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK);
                imageIntent.setType("image/*");
                startActivityForResult(imageIntent, ACTIVITY_SELECT_IMAGE);
            }
        });
    }

    private void insertPet() {
        mDbHelper = new ItemDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String productName = mNameEditText.getText().toString().trim();
        String productVariety = mVarietyEditText.getText().toString().trim();
        String productPriceString = mPriceEditText.getText().toString().trim();
        int productPrice = Integer.parseInt(productPriceString);
        String productSupplier = mSupplierEditText.getText().toString().trim();
        String quantityProductString = mQuantityText.getText().toString().trim();
        int productQuantity = Integer.parseInt(quantityProductString);

        /**
        Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] img = bos.toByteArray();
         */

        ContentValues values = new ContentValues();
        values.put(ItemContract.CustomersEntryProducts.COLUMN_NAME, productName);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY, productVariety);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE, productPrice);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER, productSupplier);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY, productQuantity);
        values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE, path);

        long newRowId = db.insert(ItemContract.CustomersEntryProducts.TABLE_NAME, null, values);

        Toast.makeText(getApplicationContext(), "Item inserted in database with id " + newRowId, Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case ACTIVITY_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    path = selectedImage.toString();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                    mEnterImageButton.setVisibility(View.GONE);
                    mImageView.setImageBitmap(yourSelectedImage);
                }
        }
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
                insertPet();
                finish();
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
