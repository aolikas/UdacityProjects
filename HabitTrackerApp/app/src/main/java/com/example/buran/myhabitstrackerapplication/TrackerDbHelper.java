package com.example.buran.myhabitstrackerapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.buran.myhabitstrackerapplication.HTContract.HabitsTrackerEntry;

/**
 * Created by buran on 12.07.17.
 */

public class TrackerDbHelper extends SQLiteOpenHelper {

    /**
     * name of the database file
     */
    private static final String DATABASE_NAME = "habits.db";

    /**
     * Database version. If you change a schema, version will be incremented
     */
    private static final int DATABASE_VERSION = 1;

    //create a database
    private SQLiteDatabase mDb;

    /**
     * Create a new TrackerDbHelper object
     *
     * @param context is context of an app
     */
    public TrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //gets the database in write mode
        mDb = getWritableDatabase();
    }

    /**
     * This is called when the database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("onCreate", "true");
        final String SQL_CREATE_HABITS_TABLE =
                "CREATE TABLE " +
                        HabitsTrackerEntry.TRACKER_TABLE_NAME + " (" +
                        HabitsTrackerEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, " +
                        HabitsTrackerEntry.COLUMN_HABIT_DURATION + " INTEGER NOT NULL, " +
                        HabitsTrackerEntry.COLUMN_HABIT_COMMENT + " TEXT NOT NULL" +
                        ")";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    /**
     * This is called when the database will updated
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    /**
     * this method insert a new record to the table
     *
     * @param values is the ContentValues with the data to create the record
     */
    public void insertHabit(ContentValues values) {
        //gets the database in write mode
        mDb = getWritableDatabase();
        try {
            mDb.insert(HabitsTrackerEntry.TRACKER_TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDb.close();
    }

    /**
     * After I got a review I decided to make an experiment and
     * wrote a method getActivityDuration(int id) which is returned an int,
     * beside a Cursor object, because I wanted to have some particular action from my table.
     * And I though that a new method is more efficient.
     * <p>
     * Now this method return a Cursor object
     *
     * @return Cursor
     */
    public Cursor getAllActivityCursor() {
        // set parameters
        String[] columns = {
                HabitsTrackerEntry.COLUMN_HABIT_ID,
                HabitsTrackerEntry.COLUMN_HABIT_NAME,
                HabitsTrackerEntry.COLUMN_HABIT_DURATION,
                HabitsTrackerEntry.COLUMN_HABIT_COMMENT};

        // open a database
        mDb = getReadableDatabase();
        Cursor cursor = mDb.query(
                true,
                HabitsTrackerEntry.TRACKER_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * this method return a particular duration of activity(habits) by id
     * otherwise it returns -1
     *
     * @param id is the id of the record
     * @return duration (int) of habit.
     */
    public int getActivityDuration(int id) {
        // result
        int duration = -1;

        // set parameters
        String[] columns = {HabitsTrackerEntry.COLUMN_HABIT_DURATION};

        String selection = HabitsTrackerEntry.COLUMN_HABIT_ID + " = " + id;

        // open a database
        mDb = getReadableDatabase();
        Cursor cursor = mDb.query(
                true,
                HabitsTrackerEntry.TRACKER_TABLE_NAME,
                columns,
                selection,
                null,
                null,
                null,
                null,
                null);

        try {
            int durationColumnIndex = cursor.getColumnIndex(HabitsTrackerEntry.COLUMN_HABIT_DURATION);
            cursor.moveToFirst();
            duration = cursor.getInt(durationColumnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return duration;
    }
}




