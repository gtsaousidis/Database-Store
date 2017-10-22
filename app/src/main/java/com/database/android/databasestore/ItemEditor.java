package com.database.android.databasestore;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.database.android.databasestore.data.ItemContract;
import com.database.android.databasestore.data.ItemDbHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class ItemEditor extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private final static int ACTIVITY_SELECT_IMAGE = 100;

    private static final int ITEM_LOADER = 0;

    // This will be true if the user updates part of the item form
    private boolean mHasItemChanged;

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

    // The uri that is returned for a specific item frm the list through the intent's data
    private Uri currentItemUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasItemChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentItemUri = intent.getData();

        if (currentItemUri == null) {
            // This is a new item so change the label to "Add an item"
            setTitle(R.string.edit_item);
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing item so the label must be "Edit item"
            setTitle(R.string.edit_item);
            getLoaderManager().initLoader(ITEM_LOADER, null, this);
        }

        // Find all the relevant views that is used to read input from user
        mNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mNameEditText.setOnTouchListener(mTouchListener);
        mVarietyEditText = (EditText) findViewById(R.id.edit_item_variety);
        mVarietyEditText.setOnTouchListener(mTouchListener);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText = (EditText) findViewById(R.id.edit_item_supplier);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mQuantityText = (EditText) findViewById(R.id.edit_quantity_item);
        mQuantityText.setOnTouchListener(mTouchListener);
        mEnterImageButton = (Button) findViewById(R.id.insert_image_button);
        mEnterImageButton.setOnTouchListener(mTouchListener);
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

    private void savePet() {

        ContentValues values = new ContentValues();
        if (currentItemUri == null) {

            String productName = mNameEditText.getText().toString().trim();
            String productVariety = mVarietyEditText.getText().toString().trim();
            String productPriceString = mPriceEditText.getText().toString().trim();
            int productPrice = 0;
            if (!TextUtils.isEmpty(productPriceString)) {
                productPrice = Integer.parseInt(productPriceString);
            }
            String productSupplier = mSupplierEditText.getText().toString().trim();
            String quantityProductString = mQuantityText.getText().toString().trim();
            int productQuantity = 0;
            if (!TextUtils.isEmpty(quantityProductString)) {
                productQuantity = Integer.parseInt(quantityProductString);
            }
            /**
             Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();

             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
             byte[] img = bos.toByteArray();
             */

            if (currentItemUri == null && TextUtils.isEmpty(productName)
                    && TextUtils.isEmpty(productVariety) && TextUtils.isEmpty(productPriceString) &&
                    TextUtils.isEmpty(productSupplier) && TextUtils.isEmpty(quantityProductString) &&
                    TextUtils.isEmpty(path))
            {return;}
            values.put(ItemContract.CustomersEntryProducts.COLUMN_NAME, productName);
            values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY, productVariety);
            values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE, productPrice);
            values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER, productSupplier);
            values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY, productQuantity);
            values.put(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE, path);

            Uri newUri = getContentResolver().insert(ItemContract.CustomersEntryProducts.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING item, so update the item with content URI: currentItemUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because currentItemUri will already identify the correct row in the database that
            // we want to modify.

            int rowsAffected = getContentResolver().update(currentItemUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_item_successfull),
                        Toast.LENGTH_SHORT).show();
            }
        }
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

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mHasItemChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
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
                savePet();
                finish();
            case R.id.action_delete:
                return true;
            case R.id.home:
                // If the item hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mHasItemChanged) {
                    NavUtils.navigateUpFromSameTask(ItemEditor.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ItemEditor.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ItemContract.CustomersEntryProducts._ID,
                ItemContract.CustomersEntryProducts.COLUMN_NAME,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER,
                ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE};
        return new CursorLoader(this, currentItemUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_NAME));
            String variety = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_QUANTITY));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PRICE));
            String supplier = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_SUPPLIER));
            String imageString = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE));

            mNameEditText.setText(name);
            mVarietyEditText.setText(variety);
            mQuantityText.setText(Integer.toString(quantity));
            mPriceEditText.setText(Integer.toString(price));
            mSupplierEditText.setText(supplier);
            if (imageString != null) {
                Uri image = Uri.parse(imageString);
                mImageView.setImageURI(image);
            }
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
            mNameEditText.setText(null);
            mVarietyEditText.setText(null);
            mQuantityText.setText(null);
            mPriceEditText.setText(null);
            mSupplierEditText.setText(null);
            mImageView.setImageURI(null);
    }
}