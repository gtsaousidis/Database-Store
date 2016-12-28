package com.database.android.databasestore.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.database.android.databasestore.R;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvVariety = (TextView) view.findViewById(R.id.variety);
        ImageView imgItem = (ImageView) view.findViewById(R.id.image_item);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_NAME));
        String variety = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_VARIETY));
        String photo = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.CustomersEntryProducts.COLUMN_ITEM_PICTURE));
        Uri imageUri = Uri.parse(photo);

        tvName.setText(name);
        tvVariety.setText(variety);
        imgItem.setImageURI(imageUri);
    }
}
