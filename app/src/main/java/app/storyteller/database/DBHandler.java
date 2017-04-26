package app.storyteller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.models.Account;
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
        db.execSQL(Database.ProfilesTable.getTableCreationStatement());
        db.execSQL(Database.AccountsTable.getTableCreationStatement());
        db.execSQL(Database.StoriesTable.getTableCreationStatement());
        db.execSQL(Database.FavoritesTable.getTableCreationStatement());
        db.execSQL(Database.SentencesTable.getTableCreationStatement());
        db.execSQL(Database.CollaboratorsTable.getTableCreationStatement());
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
     */
    public static void openConnection(Context ctx){
        db = new DBHandler(ctx).getWritableDatabase();
    }

    /**
     *
     */
    public static void closeConnection(){
        db.close();
        db = null;
    }



    //------------------------------------------------------------------------

    /**
     * Add to the local database the Profile "p" passed in the parameters and
     * create an Account entry in the AccountsTable referring to the Profile.
     *
     * When the application opens, it can check and see if there was a Google
     * Account used on this device.
     *
     * -- Tested: working - MI - 03/15/2017.
     */
    public static void createAccount(Account acc){
        addProfile(acc);
        addAccount(acc);
    }

    /**
     * Updates
     */
    public static void updateAccount(Account acc){
        ContentValues values = new ContentValues();
        values.put(Database.ProfilesTable.COLUMN_NAME, acc.getName());
        values.put(Database.ProfilesTable.COLUMN_TOKENS, acc.getTokens());
        values.put(Database.ProfilesTable.COLUMN_IMAGE, acc.getImageURL());
        values.put(Database.ProfilesTable.COLUMN_LAST_CONNECTED, acc.getLastConnected().toString());
        db.update(
                Database.ProfilesTable.TABLE_NAME,
                values,
                Database.ProfilesTable.COLUMN_GOOGLE_ID  + "=?",
                new String[]{acc.getGoogleId()});
    }

    /**
     * Add to the local database the Profile "p" passed in the parameters.
     * Tested : Working - MB - 3/15/2017
     * -- Tested: working - MI - 03/15/2017.
     */
    public static void addProfile(Profile p){
        ContentValues values = new ContentValues();
        values.put(Database.ProfilesTable.COLUMN_ID, p.getId());
        values.put(Database.ProfilesTable.COLUMN_GOOGLE_ID, p.getGoogleId());
        values.put(Database.ProfilesTable.COLUMN_NAME, p.getName());
        values.put(Database.ProfilesTable.COLUMN_TOKENS, p.getTokens());
        values.put(Database.ProfilesTable.COLUMN_IMAGE, p.getImageURL());
        values.put(Database.ProfilesTable.COLUMN_LAST_CONNECTED, p.getLastConnected().toString());
        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     * Add to the local database the account "p" passed in the parameters.
     * -- Tested: working - MI - 03/15/2017.
     */
    private static void addAccount(Account p){
        ContentValues values = new ContentValues();
        values.put(Database.AccountsTable.COLUMN_PROFILE_ID, p.getId());
        values.put(Database.AccountsTable.COLUMN_GOOGLE_ID, p.getGoogleId());
        values.put(Database.AccountsTable.COLUMN_LAST_CONNECTED, p.getLastConnected().toString());
        db.insert(Database.AccountsTable.TABLE_NAME, null, values);
    }

    /**
     * Add to the local database the Story "s" passed in the parameters. Also
     * insert all the sentences under the Story "s" in the database under the
     * sentences_table.
     * Tested: Working - MB - 3/15/2017
     */
    public static void addStory(Story s){
        StoryDetails st = s.getDetails();
        ContentValues values = new ContentValues();
        values.put(Database.StoriesTable.COLUMN_ID, s.getId());
        values.put(Database.StoriesTable.COLUMN_TITLE, st.getTitle());
        values.put(Database.StoriesTable.COLUMN_THEME, st.getTheme());
        values.put(Database.StoriesTable.COLUMN_MAIN_CHARACTER, st.getMainCharacter());
        values.put(Database.StoriesTable.COLUMN_CREATOR_ID, s.getCreator().getId());
        values.put(Database.StoriesTable.COLUMN_CREATION_DATE, s.getCreationDate().toString());
        db.insert(Database.StoriesTable.TABLE_NAME, null, values);

        // -- Add each sentence.
        for (Sentence sentence : s.getSentences()) {
            addSentence(s.getId(), sentence);
        }
    }

    /**
     * Add to the local database the Sentence "s" passed in the parameters, related
     * to the Story with id : "story_id".
     */
    private static void addSentence(int story_id, Sentence s){
        ContentValues values = new ContentValues();
        values.put(Database.SentencesTable.COLUMN_ID, s.getId());
        values.put(Database.SentencesTable.COLUMN_AUTHOR_ID, s.getAuthor().getId());
        values.put(Database.SentencesTable.COLUMN_STORY_ID, story_id);
        values.put(Database.SentencesTable.COLUMN_CONTENT, s.getContent());
        values.put(Database.SentencesTable.COLUMN_CREATION_DATE, s.getCreationDate().toString());
        db.insert(Database.SentencesTable.TABLE_NAME, null, values);
    }

    /**
     * TODO.
     */
    public static void addCollaborator(Profile p, Story s){
        ContentValues values = new ContentValues();
        values.put(Database.CollaboratorsTable.COLUMN_PROFILE_ID, p.getId());
        values.put(Database.CollaboratorsTable.COLUMN_STORY_ID, s.getId());
        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }

    /**
     * TODO.
     * Please take note that, story_id should be unique in the database
     */
    public static void addFavorite(int profile_id,int story_id){
        ContentValues values = new ContentValues();
        values.put(Database.FavoritesTable.COLUMN_STORY_ID, story_id);
        values.put(Database.FavoritesTable.COLUMN_PROFILE_ID, profile_id);
        db.insert(Database.FavoritesTable.TABLE_NAME, null, values);
    }

    /**
     * TODO
     *
     */
    public static void removeFavorite(int user_id,int story_id) {
        db.delete(Database.FavoritesTable.TABLE_NAME,
                "profile_id = " + user_id + " AND story_id = " + story_id, null);
    }


    //------------------------------------------------------------------------

    /**
     * Retrieves the Profile from the local database.
     * @param id : Profile's id.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static Profile getProfile(int id){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{
                Database.ProfilesTable.COLUMN_ID,
                Database.ProfilesTable.COLUMN_GOOGLE_ID,
                Database.ProfilesTable.COLUMN_NAME,
                Database.ProfilesTable.COLUMN_TOKENS,
                Database.ProfilesTable.COLUMN_IMAGE,
                Database.ProfilesTable.COLUMN_LAST_CONNECTED
            }
            ,Database.ProfilesTable.COLUMN_ID + "=?"
            ,new String[]{Integer.toString(id)},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        return new Profile(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2),
            Integer.parseInt(cursor.getString(3)),
            cursor.getString(4),
            Timestamp.valueOf(cursor.getString(5)),
            new ArrayList<Story>() // getfavoriteStories()
        );
    }


    /**
     * Retrieves the Profile from the local database.
     * @param google_id : Profile's google_id.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static Profile getProfile(String google_id){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{
                Database.ProfilesTable.COLUMN_ID,
                Database.ProfilesTable.COLUMN_GOOGLE_ID,
                Database.ProfilesTable.COLUMN_NAME,
                Database.ProfilesTable.COLUMN_TOKENS,
                Database.ProfilesTable.COLUMN_IMAGE,
                Database.ProfilesTable.COLUMN_LAST_CONNECTED
            }
            ,Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?"
            ,new String[]{google_id},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        return new Profile(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2),
            Integer.parseInt(cursor.getString(3)),
            cursor.getString(4),
            Timestamp.valueOf(cursor.getString(5)),
            new ArrayList<Story>() // getfavoriteStories()
        );
    }

    /**
     * Retrieves the Account from the local database.
     * @param google_id : Profile's google_id.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static Account getAccount(String google_id){
        Cursor cursor = db.query(
                Database.ProfilesTable.TABLE_NAME,
                new String[]{
                        Database.ProfilesTable.COLUMN_ID,
                        Database.ProfilesTable.COLUMN_GOOGLE_ID,
                        Database.ProfilesTable.COLUMN_NAME,
                        Database.ProfilesTable.COLUMN_TOKENS,
                        Database.ProfilesTable.COLUMN_IMAGE,
                        Database.ProfilesTable.COLUMN_LAST_CONNECTED
                }
                ,Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?"
                ,new String[]{google_id},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        return new Account(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                Timestamp.valueOf(cursor.getString(5)),
                new ArrayList<Story>() // getfavoriteStories()
        );
    }

    /**
     * Returns the number of stories available in the local database.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static int getProfileListSize(){
        return db.rawQuery("SELECT * FROM "+ Database.ProfilesTable.TABLE_NAME, null).getCount();
    }

    /**
     * Returns the number of stories available in the local database.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static int getAccountListSize(){
        return db.rawQuery("SELECT * FROM "+ Database.AccountsTable.TABLE_NAME, null).getCount();
    }

    /**
     * Retrieves the ID of the last Profile that was used on this device
     * (from the local database).
     * @return : The ID of the last connected Profile.
     *
     * TODO.
     */
    public static int getLastAccountConnected(){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{Database.ProfilesTable.COLUMN_GOOGLE_ID}
            ,Database.ProfilesTable.COLUMN_LAST_CONNECTED + "=?"
            ,new String[] {"lkjlkjlkj"},null,null,null,null);

        // SELECT * FROM x ORDER BY timestamp DESC LIMIT 1

        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }

    /**
     * Retrieves the Story from the local database corresponding to the id
     * passed in parameters.
     */
    public static Story getStory(int id){
        Cursor cursor = db.query(
            Database.StoriesTable.TABLE_NAME,
            new String[]{
                Database.StoriesTable.COLUMN_TITLE,
                Database.StoriesTable.COLUMN_THEME,
                Database.StoriesTable.COLUMN_MAIN_CHARACTER,
                Database.StoriesTable.COLUMN_CREATOR_ID,
                Database.StoriesTable.COLUMN_CREATION_DATE
            }
            ,Database.StoriesTable.COLUMN_ID + "=?"
            ,new String[]{String.valueOf(id)},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        // Get user_id corresponding to the google_id passed in parameters.
        int profile_id = Integer.parseInt(cursor.getString(3));

        return new Story(
            id,
            new StoryDetails(cursor.getString(0), cursor.getString(1), cursor.getString(2)),
            getProfile(profile_id),
            getSentences(id),
            Timestamp.valueOf(cursor.getString(4))
        );
    }

    /**
     * Returns the number of stories available in the local database.
     */
    public static int getStoryListSize(){
        return db.rawQuery("SELECT * FROM "+ Database.StoriesTable.TABLE_NAME, null).getCount();
    }

    /**
     * Provides the list of sentences related to the Story with the
     * id : story_id.
     */
    public static ArrayList<Sentence> getSentences(int story_id){
        //
        ArrayList<Sentence> sentences = new ArrayList<Sentence>();

        Cursor cursor = db.query(
            Database.SentencesTable.TABLE_NAME,
            new String[]{
                Database.SentencesTable.COLUMN_ID,
                Database.SentencesTable.COLUMN_AUTHOR_ID,
                Database.SentencesTable.COLUMN_CONTENT,
                Database.SentencesTable.COLUMN_CREATION_DATE
            }
            ,Database.SentencesTable.COLUMN_STORY_ID + "=?"
            ,new String[]{String.valueOf(story_id)},null,null,null,null);

        // Select the first element.
        cursor.moveToFirst();

        // Get one sentence at a time.
        do {
            // Get author.
            int profile_id = Integer.parseInt(cursor.getString(1));
            User author = getProfile(profile_id);
            sentences.add(new Sentence(
                Integer.parseInt(cursor.getString(0)),
                author,
                cursor.getString(2),
                Timestamp.valueOf(cursor.getString(3))
            ));
        }while(cursor.moveToNext());

        // Return sentences.
        return sentences;
    }

    /**
     *@param id id of the requested user
     *  Tested: Working - MB - 3/15/2017
     */
    public static ArrayList<Integer> getFavorites(int id) {

        ArrayList<Integer> favs = new ArrayList<>();

        Cursor cursor = db.query(
                Database.FavoritesTable.TABLE_NAME,
                new String[]{Database.FavoritesTable.COLUMN_STORY_ID}
                ,Database.FavoritesTable.COLUMN_PROFILE_ID+ "=?"
                ,new String[]{String.valueOf(id)},null,null,null,null);

        cursor.getCount();
        System.out.println("****************FAVORITE COUNT OF ID#"+id+": " +cursor.getCount() + "*****************");
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            favs.add(Integer.parseInt(cursor.getString(0)));
            cursor.moveToNext();
        }
        return favs;
    };

    /**
     * Returns the number of favorites
     */
    public static int getFavoriteListSize() {
        return db.rawQuery("SELECT * FROM "+ Database.FavoritesTable.TABLE_NAME, null).getCount();
    }

    //------------------------------------------------------------------------



    /**
     * Checks if there is a profile in the local database.
     * @param id : Profile's id.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static boolean profileExists(int id){
        Cursor cursor = db.query(
                Database.ProfilesTable.TABLE_NAME,
                new String[]{Database.ProfilesTable.COLUMN_ID},
                Database.ProfilesTable.COLUMN_ID + "=?",
                new String[]{Integer.toString(id)},null,null,null,null
        );
        return cursor.getCount() != 0;
    }

    /**
     * Checks if there is a profile in the local database.
     * @param google_id : Profile's google_id.
     * -- Tested: working - MI - 03/15/2017.
     */
    public static boolean profileExists(String google_id){
        Cursor cursor = db.query(
            Database.ProfilesTable.TABLE_NAME,
            new String[]{Database.ProfilesTable.COLUMN_ID},
            Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?",
            new String[]{google_id},null,null,null,null
        );
        return cursor.getCount() != 0;
    }

    /**
     * Verifies if there is at least one Account in the local database
     * @return true if there is and false otherwise.
     */
    public static boolean accountExists()
    {
        return getAccountListSize() != 0;
    }

    /**
     * Checks if there is a profile attached to the id (given by the API) passed in
     * the parameters.
     * TODO.
     */
    public static boolean accountExists(String google_id){
        Cursor cursor = db.query(
                Database.AccountsTable.TABLE_NAME,
                new String[]{Database.AccountsTable.COLUMN_ID},
                Database.ProfilesTable.COLUMN_GOOGLE_ID + "=?",
                new String[]{google_id},null,null,null,null
        );
        return cursor.getCount() != 0;
    }


    /**
     * Checks in the database and returns true or false whether the story exists or not.
     * Tested: Working - MB - 3/15/2017
     */
    public static boolean storyExists(int id){
        Cursor cursor = db.query(
                Database.StoriesTable.TABLE_NAME,
                new String[]{Database.StoriesTable.COLUMN_ID},
                Database.StoriesTable.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null
        );
        return cursor.getCount() != 0;
    }
}