package com.example.buran.myinventoryapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.buran.myinventoryapplication.data.InventoryContract.InventoryEntry;

/**
 * Created by buran on 18.07.17.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    /**
     * name of the database file
     */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change a schema, version will increment
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Create a new InventoryDbHelper object
     *
     * @param context is context of an app
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create and execute the SQL statement
        db.execSQL(InventoryEntry.CREATE_INVENTORY_TABLE);

    }

    /**
     * This is called when the database will update
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME);
        onCreate(db);
    }
}