package com.RenegadeSloth.mike.chipcounter;

import android.provider.BaseColumns;

public class DatabaseDescription {

    private DatabaseDescription(){}

    public static final class Amounts implements BaseColumns{
        public static final String TABLE_NAME = "amounts";

        public static final String COLUMN_RECORD = "record";
        public static final String COLUMN_TWENTYFIVE = "twentyfive";
        public static final String COLUMN_ONEHUNDRED = "onehundred";
        public static final String COLUMN_FIVEHUNDRED = "fivehundred";
        public static final String COLUMN_ONETHOUSAND = "onethousand";
        public static final String COLUMN_FIVETHOUSAND = "fivethousand";
    }

    public static final class History implements BaseColumns{
        public static final String TABLE_NAME = "history";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SPENT = "spent";
        public static final String COLUMN_WON = "won";
    }
}

