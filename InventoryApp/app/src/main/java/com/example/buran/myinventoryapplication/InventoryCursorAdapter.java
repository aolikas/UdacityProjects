package com.example.buran.myinventoryapplication;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buran.myinventoryapplication.data.InventoryContract.InventoryEntry;
import com.bumptech.glide.Glide;

/**
 * Created by buran on 18.07.17.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = InventoryCursorAdapter.class.getSimpleName();

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    //this methods binds the inventory data to the given layout
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //find id view that want to modify in the list layout
        TextView nameTextView = (TextView) view.findViewById(R.id.list_item_product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_item_product_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.list_item_product_quantity);
        TextView soldTextView = (TextView) view.findViewById(R.id.list_item_product_sold);

        ImageView pictureImageView = (ImageView) view.findViewById(R.id.list_item_image_view);
        ImageView buttonImageView = (ImageView) view.findViewById(R.id.list_item_image_button);

        //find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
        int soldColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SOLD_ITEMS);
        int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_IMAGE);

        //read the item attributes from the cursor for the current pet
        int id = cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));
        final String productName = cursor.getString(nameColumnIndex);
        String productPrice = "$ " + cursor.getString(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        final int productSold = cursor.getInt(soldColumnIndex);
        Uri productImage = Uri.parse(cursor.getString(imageColumnIndex));


        final Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
        Log.d(LOG_TAG, "gen Uri: " + currentProductUri + " Product name: " + productName + " id: " + id);

        //update the TextView and ImageView with the attributes for the current item
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(String.valueOf(productQuantity));
        soldTextView.setText(String.valueOf(productSold));

        //using Glide to import photo images
        Glide.with(context).load(productImage).
                placeholder(R.mipmap.ic_launcher).
                crossFade().
                centerCrop().
                into(pictureImageView);

        buttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, productName + " quantity= " + productQuantity);
                ContentResolver contentResolver = v.getContext().getContentResolver();
                ContentValues contentValues = new ContentValues();
                if (productQuantity > 0) {
                    //var for quantity
                    int q = productQuantity;
                    //var for sold items
                    int s = productSold;
                    Log.d(LOG_TAG, "new quantity = " + q);
                    contentValues.put(InventoryEntry.COLUMN_QUANTITY, --q);
                    contentValues.put(InventoryEntry.COLUMN_SOLD_ITEMS, ++s);
                    contentResolver.update(currentProductUri, contentValues, null, null);
                    context.getContentResolver().notifyChange(currentProductUri, null);
                } else {
                    Toast.makeText(context, R.string.adapter_toast_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}