package app.storyteller.database;

import android.provider.BaseColumns;

/**
 * Created by Mihai on 2017-03-08.
 */
public class Database {


    /**
     * Table : Profiles.
     * -- The profiles associated with the current device.
     */
    public static class AccountsTable implements BaseColumns {
        public static final String TABLE_NAME = "accounts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PROFILE_ID = "profile_id";
        public static final String COLUMN_GOOGLE_ID = "google_id";
        public static final String COLUMN_LAST_CONNECTED = "last_connected";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY,"
                    + COLUMN_PROFILE_ID + " INT NOT NULL,"
                    + COLUMN_GOOGLE_ID + " INT NOT NULL,"
                    + COLUMN_LAST_CONNECTED + " DATE NOT NULL,"   // TimeStamp.
                    + " FOREIGN KEY ("+COLUMN_PROFILE_ID+") REFERENCES "
                    + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+"),"
                    + " FOREIGN KEY ("+COLUMN_GOOGLE_ID+") REFERENCES "
                    + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_GOOGLE_ID+")"
                    + ");";
        }
    }

    /**
     * Table : Profiles.
     * -- The profiles associated with the current device.
     */
    public static class ProfilesTable implements BaseColumns {
        public static final String TABLE_NAME = "profiles";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_GOOGLE_ID = "google_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TOKENS = "tokens";
        public static final String COLUMN_IMAGE = "image_url";
        public static final String COLUMN_LAST_CONNECTED = "last_connected";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT NOT NULL,"
                    + COLUMN_GOOGLE_ID + " VARCHAR(21) NOT NULL,"
                    + COLUMN_NAME + " VARCHAR(15) NOT NULL,"
                    + COLUMN_TOKENS + " INT NOT NULL,"
                    + COLUMN_IMAGE + " TEXT NOT NULL,"
                    + COLUMN_LAST_CONNECTED + " DATE NOT NULL"   // TimeStamp.
                    + ");";
        }
    }

    /**
     * Table : Stories.
     */
    public static class StoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "stories";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_THEME= "theme";
        public static final String COLUMN_MAIN_CHARACTER= "main_character";
        public static final String COLUMN_CREATOR_ID= "creator_id";
        public static final String COLUMN_CREATION_DATE= "creation_date";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT NOT NULL,"
                    + COLUMN_TITLE + " VARCHAR(24) NOT NULL,"
                    + COLUMN_THEME + " VARCHAR(15) NOT NULL,"
                    + COLUMN_MAIN_CHARACTER + " VARCHAR(15) NOT NULL,"
                    + COLUMN_CREATOR_ID + " INT NOT NULL,"
                    + COLUMN_CREATION_DATE + " DATE NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_CREATOR_ID+") REFERENCES "
                    + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+")"
                    + ");";
        }
    }

    /**
     * Table : Sentences.
     */
    public static class SentencesTable implements BaseColumns {
        public static final String TABLE_NAME = "sentences";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR_ID = "author_id";
        public static final String COLUMN_STORY_ID = "story_id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_CREATION_DATE = "creation_date";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT NOT NULL,"
                    + COLUMN_AUTHOR_ID + " INT NOT NULL,"
                    + COLUMN_STORY_ID + " INT NOT NULL,"
                    + COLUMN_CONTENT + " TEXT NOT NULL,"
                    + COLUMN_CREATION_DATE + " DATE NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_AUTHOR_ID+") REFERENCES "
                        + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+"),"
                    + " FOREIGN KEY ("+COLUMN_STORY_ID+") REFERENCES "
                        + StoriesTable.TABLE_NAME+"("+StoriesTable.COLUMN_ID+")"
                    + ");";
        }
    }

    /**
     * Table : Favorites.
     */
    public static class FavoritesTable implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PROFILE_ID= "profile_id";
        public static final String COLUMN_STORY_ID= "story_id";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY,"
                    + COLUMN_PROFILE_ID + " INT NOT NULL,"
                    + COLUMN_STORY_ID + " INT NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_PROFILE_ID+") REFERENCES "
                    + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+"),"
                    + " FOREIGN KEY ("+COLUMN_STORY_ID+") REFERENCES "
                        + StoriesTable.TABLE_NAME+"("+StoriesTable.COLUMN_ID+")"
                    + ");";
        }
    }

    /**
     * Table : Collaborators.
     */
    public static class CollaboratorsTable implements BaseColumns {
        public static final String TABLE_NAME = "collaborators";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_STORY_ID = "story_id";
        public static final String COLUMN_PROFILE_ID = "profile_id";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT NOT NULL,"
                    + COLUMN_PROFILE_ID + " INT NOT NULL,"
                    + COLUMN_STORY_ID + " INT NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_PROFILE_ID+") REFERENCES "
                        + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+"),"
                    + " FOREIGN KEY ("+COLUMN_STORY_ID+") REFERENCES "
                        + StoriesTable.TABLE_NAME+"("+StoriesTable.COLUMN_ID+")"
                    + ");";
        }
    }
}
