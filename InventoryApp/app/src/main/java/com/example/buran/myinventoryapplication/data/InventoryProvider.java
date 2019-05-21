package com.example.buran.myinventoryapplication.data;

import com.example.buran.myinventoryapplication.R;
import com.example.buran.myinventoryapplication.data.InventoryContract.InventoryEntry;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.util.Log;

/**
 * Created by buran on 18.07.17.
 */

public class InventoryProvider extends ContentProvider {

    /**
     * tag for the log msg
     */
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the inventory table
     */
    private static final int INVENTORY = 100;

    /**
     * URI matcher code for the content URI for a single product in the pets table
     */
    private static final int INVENTORY_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * the input passed into the constructor represents the code to return for the root URI
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * static initializer.This is run the first time anything is called from this class.
     */

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, INVENTORY);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#", INVENTORY_ID);
    }

    //Database helper object
    private InventoryDbHelper mDbHelper;

    /**
     * init the provider and the database helper object
     */
    @Override
    public boolean onCreate() {
        Log.i(LOG_TAG, "Prepare a DbHelper");
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    /**
     * perform the query for given Uri.
     * use the given projection,selection, selection arg, sort order
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //create a readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //this cursor will hold a result of a query
        Cursor cursor;

        //figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = db.query(InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query with unknown URI " + uri);
        }
        //set notification URI on the Cursor
        //so we know what content URI the Cursor was created for
        //if the data at this URI changes, then we know we need to update the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * insert a new data into provider with given values
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * insert product/item into a database with given content values.
     *
     * @return the new content URI for that specific row in the database.
     */
    private Uri insertItem(Uri uri, ContentValues contentValues) {
        //check that the name is not null
        String name = contentValues.getAsString(InventoryEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("This item requires a name");
        }

        // If the price is provided, check that it's greater than  0
        Integer price = contentValues.getAsInteger(InventoryEntry.COLUMN_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("This item requires a valid price");
        }

        //If the quantity is provided, check that it's greater than  0
        Integer quantity = contentValues.getAsInteger(InventoryEntry.COLUMN_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("This item requires a valid quantity");
        }

        //get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //insert the new item with the given values
        long id = db.insert(InventoryEntry.TABLE_NAME, null, contentValues);

        //if id is -1 then the insertion failed. Log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //notify all listeners that the data has changed for the inventory content URI
        getContext().getContentResolver().notifyChange(uri, null);
        Log.i(LOG_TAG, "Inserting a record");

        //return the new URI with the Id appended at the end
        return ContentUris.withAppendedId(uri, id);
    }


    /**
     * updates a data at the given selection and selectionArgs
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case INVENTORY_ID:
                //for the inventoryId code, extract out id from the URI,
                //so we know, which row to update.Selection will be "_id=?" and selection
                //arguments will be a String array containing the actual ID
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Updates items in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments.
     *
     * @return the number of rows that were successfully updated
     */
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //if the name of a item key is present,check that name value is not null
        if (values.containsKey(InventoryEntry.COLUMN_NAME)) {
            String name = values.getAsString(InventoryEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("The item requires a name");
            }
        }

        //if the price key is present, check that price value is valid
        if (values.containsKey(InventoryEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(InventoryEntry.COLUMN_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("The item requires a valid price");
            }
        }

        //if the quantity key is present, check that quantity value is valid
        if (values.containsKey(InventoryEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(InventoryEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("The item requires a valid quantity");
            }
        }

        //if there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // otherwise, get writable database to update data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //perform the update on the database and get the number of rows affected
        int rowUpdated = db.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        //if 1 or more rows were updated, then notify all listeners that the data at the given URI has changed
        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //return the number of rows updated
        return rowUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //track rhe number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                //delete all rows that match the selection and selectionArgs
                rowsDeleted = db.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                //delete a single row given by Id in the URI
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        //if 1 or more rows were deleted, then notify all listeners that the data at the given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //return the number of rows deleted
        return rowsDeleted;
    }

    //returns the MIME type of data for the content URI
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match" + match);
        }
    }
}