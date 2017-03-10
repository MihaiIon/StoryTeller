package app.storyteller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.models.Profile;
import app.storyteller.models.Sentence;
import app.storyteller.models.Story;
import app.storyteller.models.StoryDetails;
import app.storyteller.models.User;

/**
 * Created by Mihai on 2017-03-08.
 */
public class DBHandler extends SQLiteOpenHelper {

    /*
     * Attributes.
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "data.db";
    private static SQLiteDatabase db = null;

    /**
     * Constructor.
     *
     * @param context : TODO.
     */
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create all the tables in the database.
     *
     * @param db : TODO.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Database.UsersTable.getTableCreationStatement());
        db.execSQL(Database.ProfilesTable.getTableCreationStatement());
        /*db.execSQL(Database.StoriesTable.getTableCreationStatement());
        db.execSQL(Database.FavoritesTable.getTableCreationStatement());
        db.execSQL(Database.SentencesTable.getTableCreationStatement());
        db.execSQL(Database.CollaboratorsTable.getTableCreationStatement());*/
    }

    /**
     * Delete all tables and recreate it.
     *
     * @param db            : TODO.
     * @param oldVersion    : TODO.
     * @param newVersion    : TODO.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     *
     *
     * @param context : TODO.
     */
    public static void openConnection(Context context){
        db = new DBHandler(context).getWritableDatabase();
    }

    /**
     *
     */
    public static void closeConnection(){
        db = null;
    }



    //------------------------------------------------------------------------

    /**
     * Add to the local database the Profile "p" passed in the parameters.
     */
    public static void addProfileToDB(Profile p){
        /*
         * First, add the profile as a User in the "Users Table".
         */
        ContentValues values = new ContentValues();
        values.put(Database.UsersTable.COLUMN_ID, p.getId());
        values.put(Database.UsersTable.COLUMN_GOOGLE_ID, p.getGoogleId());
        values.put(Database.UsersTable.COLUMN_NAME, p.getName());
        values.put(Database.UsersTable.COLUMN_IMAGE, p.getImageURL());
        values.put(Database.UsersTable.COLUMN_LAST_CONNECTED
                ,p.updateLastConnected().getLastConnected().toString());
        db.insert(Database.UsersTable.TABLE_NAME, null, values);

        /*
         * Then, add the information related to the local profile in
         * the "Profiles Table".
         */
        values = new ContentValues();
        values.put(Database.ProfilesTable.COLUMN_USER_ID, p.getId());
        values.put(Database.ProfilesTable.COLUMN_GOOGLE_ID, p.getGoogleId());
        values.put(Database.ProfilesTable.COLUMN_TOKENS, p.getTokens());
        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     * Add to the local database the Story "s" passed in the parameters. Also
     * insert all the sentences under the Story "s" in the database under the
     * sentences_table.
     */
    public static void addToStories(Story s){
        StoryDetails st = s.getDetails();
        ContentValues values = new ContentValues();
        values.put(Database.StoriesTable.COLUMN_NAME, st.getTitle());
        values.put(Database.StoriesTable.COLUMN_CREATOR_ID, s.getCreator().getGoogleId());
        values.put(Database.StoriesTable.COLUMN_CREATION_DATE, new Timestamp(System.currentTimeMillis()).toString());
        values.put(Database.StoriesTable.COLUMN_MAIN_CHARACTER, st.getMainCharacter());
        values.put(Database.StoriesTable.COLUMN_THEME, st.getTheme());
        db.insert(Database.StoriesTable.TABLE_NAME, null, values);

        // -- Add each sentence.
        for (Sentence sentence : s.getSentences()) {
            addToSentences(s.getId(), sentence);
        }
    }

    /**
     *
     *
     * @param story_id  : TODO.
     * @param s         : TODO.
     */
    public static void addToSentences(int story_id, Sentence s){
        ContentValues values = new ContentValues();
        values.put(Database.CollaboratorsTable.COLUMN_STORY_ID, story_id);
        values.put(Database.SentencesTable.COLUMN_AUTHOR_ID, s.getAuthor().getId());
        values.put(Database.SentencesTable.COLUMN_CONTENT, s.getContent());
        db.insert(Database.SentencesTable.TABLE_NAME, null, values);
    }

    /**
     *
     *
     * @param p     : TODO.
     * @param s     : TODO.
     */
    public static void addToCollaborators(Profile p, Story s){
        ContentValues values = new ContentValues();
        values.put(Database.CollaboratorsTable.COLUMN_PROFILE_ID, p.getId());
        values.put(Database.CollaboratorsTable.COLUMN_STORY_ID, s.getId());

        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     *
     *
     * @param s     : TODO.
     */
    public static void addToFavorites(Story s){
        ContentValues values = new ContentValues();
        values.put(Database.FavoritesTable.COLUMN_STORY_ID, s.getId());
        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }



    //------------------------------------------------------------------------

    /**
     * Retrieves the Profile from the database corresponding to the
     * google_id.
     */
    public static Profile getProfile(int google_id){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{
                Database.ProfilesTable.COLUMN_USER_ID,
                Database.ProfilesTable.COLUMN_TOKENS
            }
            ,Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?"
            ,new String[]{String.valueOf(google_id)},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        // Get user_id corresponding to the google_id passed in parameters.
        int user_id = Integer.parseInt(cursor.getString(0));

        return new Profile(
            getUser(user_id),
            Integer.parseInt(cursor.getString(1)),
            new ArrayList<Story>() // getfavoriteStories(user_id)
        );
    }

    /**
     * Retrieves the Profile from the database corresponding to the
     * google_id.
     */
    public static User getUser(int id){
        Cursor cursor = db.query(
            Database.UsersTable.TABLE_NAME,
            new String[]{
                Database.UsersTable.COLUMN_GOOGLE_ID,
                Database.UsersTable.COLUMN_NAME,
                Database.UsersTable.COLUMN_IMAGE,
                Database.UsersTable.COLUMN_LAST_CONNECTED
            }
            ,Database.UsersTable.COLUMN_ID + "=?"
            ,new String[]{String.valueOf(id)},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        return new User(
            id,
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2),
            Timestamp.valueOf(cursor.getString(3))
        );
    }

    //------------------------------------------------------------------------

    /**
     * Checks if there is a profile attached to the google_id passed in
     * the parameters.
     */
    public static boolean profileExists(int google_id){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{Database.ProfilesTable.COLUMN_ID},
            Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?",
            new String[]{String.valueOf(google_id)},null,null,null,null
        );
        return cursor.getCount() != 0;
    }
}