package app.storyteller.database;

import android.provider.BaseColumns;

/**
 * Created by Mihai on 2017-03-08.
 */
public class Database {

    /**
     * Table : Profile.
     */
    public static class ProfileTable implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image_path";
        public static final String COLUMN_TOKENS = "tokens";
        public static final String COLUMN_LAST_CONNECTED = "last_connected";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY NOT NULL,"
                    + COLUMN_NAME + " CHAR(25) NOT NULL,"
                    + COLUMN_IMAGE + " CHAR(1000) NOT NULL,"
                    + COLUMN_TOKENS + " INT NOT NULL,"
                    + COLUMN_LAST_CONNECTED + " DATE"
                    + ");";
        }
    }

    /**
     * Table : Lala.
     */
    public static class Lala implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image_path";
        public static final String COLUMN_TOKENS = "tokens";
        public static final String COLUMN_LAST_CONNECTED = "last_connected";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY NOT NULL,"
                    + COLUMN_NAME + " CHAR(25) NOT NULL,"
                    + COLUMN_IMAGE + " CHAR(1000) NOT NULL,"
                    + COLUMN_TOKENS + " INT NOT NULL,"
                    + COLUMN_LAST_CONNECTED + " DATE"
                    + ");";
        }
    }
}
