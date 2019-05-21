package com.example.buran.myinventoryapplication.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by buran on 18.07.17.
 */

public class InventoryContract {

    /**
     * the empty constructor, to prevent someone from
     * accidentally instantiating the contract class
     */
    public InventoryContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.buran.myinventoryapplication";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "myinventoryapplication";

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table repres. a single item.
     */
    public static final class InventoryEntry implements BaseColumns {

        /**
         * The content URI to access the inventory data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;


        /**
         * name of database table for inventory items
         */
        public static final String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the item
         * Integer
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of a product/item
         * TEXT
         */
        public static final String COLUMN_NAME = "name";
        /**
         * Description of the item
         * TEXT
         */
        public static final String COLUMN_DESCRIPTION = "description";

        /**
         * Price of the item
         * INTEGER
         */
        public static final String COLUMN_PRICE = "price";

        /**
         * Quantity of the item
         * INTEGER
         */
        public static final String COLUMN_QUANTITY = "quantity";

        /**
         * Quantity of sold items
         * INTEGER
         */
        public static final String COLUMN_SOLD_ITEMS = "sale";

        /**
         * Name of a supplier
         * TEXT
         */
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Picture of the product
         */
        public static final String COLUMN_IMAGE = "image";

        //create a table
        public static final String CREATE_INVENTORY_TABLE = "CREATE TABLE " +
                InventoryEntry.TABLE_NAME + "(" +
                InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                InventoryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                InventoryEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, " +
                InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                InventoryEntry.COLUMN_SOLD_ITEMS + " INTEGER NOT NULL DEFAULT 0," +
                InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                InventoryEntry.COLUMN_IMAGE + " TEXT NOT NULL" + ");";
    }
}