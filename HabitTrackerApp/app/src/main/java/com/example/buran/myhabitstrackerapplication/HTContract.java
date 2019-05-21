package com.example.buran.myhabitstrackerapplication;

import android.provider.BaseColumns;

/**
 * Created by buran on 12.07.17.
 */

public class HTContract {

    //the empty constructor
    public HTContract() {
    }

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a single habit.
     */
    public static final class HabitsTrackerEntry implements BaseColumns {

        /**
         * name of database table for habits
         */
        public static final String TRACKER_TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit
         * Type - Integer
         */
        public static final String COLUMN_HABIT_ID = "rowID";

        /**
         * Name of a habit
         * Type - TEXT
         */
        public static final String COLUMN_HABIT_NAME = "name";

        /**
         * frequency of an activity
         * Type - INTEGER
         */
        public static final String COLUMN_HABIT_DURATION = "duration";

        /**
         * description of an activity
         * Type - TEXT
         */
        public static final String COLUMN_HABIT_COMMENT = "description";
    }
}


