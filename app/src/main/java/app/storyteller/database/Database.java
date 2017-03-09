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
    }
}
