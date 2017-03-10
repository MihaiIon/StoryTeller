package app.storyteller.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.models.Profile;
import app.storyteller.models.Stories;

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
        db.execSQL(SQLRequests.CREATE_ALL_TABLES);
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
        db.execSQL(SQLRequests.DELETE_ALL_TABLES);
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
     *
     *
     * @param p     : TODO.
     */
    public static void addToProfile(Profile p){
        ContentValues values = new ContentValues();
        values.put(Database.ProfilesTable.COLUMN_GOOGLE_ID, p.getId());
        values.put(Database.ProfilesTable.COLUMN_NAME, p.getName());
        values.put(Database.ProfilesTable.COLUMN_IMAGE, p.getImagepath());
        values.put(Database.ProfilesTable.COLUMN_TOKENS, p.getTokens());
        values.put(Database.ProfilesTable.COLUMN_LAST_CONNECTED
                , new Timestamp(System.currentTimeMillis()).toString());

        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     *
     *
     * @param s     : TODO.
     */
    public static void addToStories(Stories s){
        ContentValues values = new ContentValues();
        values.put(Database.StoriesTable.COLUMN_NAME, s.getName());
        values.put(Database.StoriesTable.COLUMN_CREATOR_ID, s.getCreator());
        values.put(Database.StoriesTable.COLUMN_CREATION_DATE, new Timestamp(System.currentTimeMillis()).toString());
        values.put(Database.StoriesTable.COLUMN_MAIN_CHARACTER, s.getMainCharacter());
        values.put(Database.StoriesTable.COLUMN_THEME, s.getTheme());

        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     *
     *
     * @param p     : TODO.
     * @param s     : TODO.
     */
    public static void addToCollaborators(Profile p, Stories s){
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
    public static void addToFavorites(Stories s){
        ContentValues values = new ContentValues();
        values.put(Database.FavoritesTable.COLUMN_STORY_ID, s.getId());

        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     *
     *
     * @param p     : TODO.
     * @param s     : TODO.
     */
    public static void addToSentences(Profile p, Stories s){
        ContentValues values = new ContentValues();
        values.put(Database.SentencesTable.COLUMN_AUTHOR, p.getName());
        values.put(Database.SentencesTable.COLUMN_CONTENT, s.getContent());
        values.put(Database.CollaboratorsTable.COLUMN_STORY_ID, s.getId());

        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    //------------------------------------------------------------------------

    /**
     *
     *
     * @return      : TODO.
     */
    public static Profile getProfile(int google_id){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{
                Database.ProfilesTable.COLUMN_GOOGLE_ID,
                Database.ProfilesTable.COLUMN_NAME,
                Database.ProfilesTable.COLUMN_TOKENS,
                Database.ProfilesTable.COLUMN_IMAGE,
                Database.ProfilesTable.COLUMN_LAST_CONNECTED,
            }
            ,Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?"
            , new String[]{String.valueOf(google_id)},null,null,null,null);

        if(cursor != null)  cursor.moveToFirst();
        else                System.out.println("**********CURSOR NULL AFTER PROFILE QUERY************");

        return new Profile(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            Integer.parseInt(cursor.getString(2)),
            cursor.getString(3),
            cursor.getString(4),
            new ArrayList<Stories>()
        );
    }

    //------------------------------------------------------------------------

    /**
     * Checks if there is a profile attached to the current google ID.
     *
     * @param google_id : TODO.
     * @return          : TODO.
     */
    public static boolean profileExists(int google_id){

        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{
                Database.ProfilesTable.COLUMN_ID,
            }
            , Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?"
            , new String[]{String.valueOf(google_id)},null,null,null,null
        );

        return cursor != null;
    }
}