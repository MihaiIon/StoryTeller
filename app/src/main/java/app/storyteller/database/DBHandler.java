package app.storyteller.database;

import android.content.ContentValues;
import android.content.Context;
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



    //------------------------------------------------------------------------

    /**
     *
     * @param db    : TODO.
     * @param p     : TODO.
     */
    public static void createProfile(SQLiteDatabase db, Profile p){
        ContentValues values = new ContentValues();
        values.put(Database.ProfilesTable.COLUMN_ID, "");
        values.put(Database.ProfilesTable.COLUMN_NAME, "");
        values.put(Database.ProfilesTable.COLUMN_IMAGE, "");
        values.put(Database.ProfilesTable.COLUMN_TOKENS, "");
        values.put(Database.ProfilesTable.COLUMN_LAST_CONNECTED
            , new Timestamp(System.currentTimeMillis()).toString());
        db.insert(Database.ProfilesTable.TABLE_NAME, null, values);
    }


    /**
     *
     * @param db    : TODO.
     * @return      : TODO.
     */
    public static Profile getProfile(SQLiteDatabase db){

        return new Profile("hola", 25, 25, "allo", "allo",new ArrayList<Stories>());
    }
}
