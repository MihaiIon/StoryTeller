package app.storyteller.database;

import android.provider.BaseColumns;

/**
 * Created by Mihai on 2017-03-08.
 */
public class Database {

    /**
     * Table : Profile.
     */
    public static class ProfilesTable implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_GOOGLE_ID = "google_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image_path";
        public static final String COLUMN_TOKENS = "tokens";
        public static final String COLUMN_LAST_CONNECTED = "last_connected";


        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY,"
                    + COLUMN_GOOGLE_ID + " INT NOT NULL,"
                    + COLUMN_NAME + " TEXT NOT NULL,"
                    + COLUMN_IMAGE + " TEXT NOT NULL,"
                    + COLUMN_TOKENS + " INT NOT NULL,"
                    + COLUMN_LAST_CONNECTED + " DATE"
                    + ");";
        }
    }

    /**
     * Table : Sentences.
     */
    public static class SentencesTable implements BaseColumns {
        public static final String TABLE_NAME = "sentences";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR= "author";
        public static final String COLUMN_STORY_ID= "story_id";
        public static final String COLUMN_CONTENT= "content";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY,"
                    + COLUMN_AUTHOR + " TEXT NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_AUTHOR+") REFERENCES "
                        + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+"),"
                    + COLUMN_STORY_ID + " INT NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_STORY_ID+") REFERENCES "
                        + StoriesTable.TABLE_NAME+"("+StoriesTable.COLUMN_ID+"),"
                    + COLUMN_CONTENT + " TEXT NOT NULL"
                    + ");";
        }
    }

    /**
     * Table : Stories.
     */
    public static class StoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "stories";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_THEME= "theme";
        public static final String COLUMN_CREATOR_ID= "creator_id";
        public static final String COLUMN_CREATION_DATE= "creation_date";
        public static final String COLUMN_MAIN_CHARACTER= "main_character";


        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY,"
                    + COLUMN_NAME + " TEXT NOT NULL,"
                    + COLUMN_THEME + " TEXT NOT NULL,"
                    + COLUMN_CREATOR_ID + " INT NOT NULL,"
                    + " FOREIGN KEY ("+COLUMN_CREATOR_ID+") REFERENCES "
                        + ProfilesTable.TABLE_NAME+"("+ProfilesTable.COLUMN_ID+"),"
                    + COLUMN_CREATION_DATE + " DATE NOT NULL,"
                    + COLUMN_MAIN_CHARACTER + " TEXT NOT NULL"
                    + ");";
        }
    }

    /**
     * Table : Favorites.
     */
    public static class FavoritesTable implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_STORY_ID= "story_id";

        public static String getTableCreationStatement(){
            return "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INT PRIMARY KEY,"
                    + COLUMN_STORY_ID + " INT NOT NULL,"
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
}
