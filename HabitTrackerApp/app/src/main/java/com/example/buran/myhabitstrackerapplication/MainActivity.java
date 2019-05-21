package com.example.buran.myhabitstrackerapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.buran.myhabitstrackerapplication.HTContract.HabitsTrackerEntry;

public class MainActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private TrackerDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to access the database we instantiate subclass of SQLiteOpenHelper
        //and pass the context , which currently activity
        mDbHelper = new TrackerDbHelper(this);

        // Create a new habit
        ContentValues value1 = new ContentValues();
        value1.put(HabitsTrackerEntry.COLUMN_HABIT_NAME, "Morning workout");
        value1.put(HabitsTrackerEntry.COLUMN_HABIT_DURATION, 30);
        value1.put(HabitsTrackerEntry.COLUMN_HABIT_COMMENT, "min 30 min for good mood");
        mDbHelper.insertHabit(value1);

        //Create another new habit
        ContentValues value2 = new ContentValues();
        value2.put(HabitsTrackerEntry.COLUMN_HABIT_NAME, "do programming");
        value2.put(HabitsTrackerEntry.COLUMN_HABIT_DURATION, 2);
        value2.put(HabitsTrackerEntry.COLUMN_HABIT_COMMENT, "2 hours for lectures");
        mDbHelper.insertHabit(value2);


        Cursor cursor = mDbHelper.getAllActivityCursor();
        int duration;

        try {
            int durationColumnIndex = cursor.getColumnIndex(HabitsTrackerEntry.COLUMN_HABIT_DURATION);
            cursor.moveToFirst();
            duration = cursor.getInt(durationColumnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }
}
