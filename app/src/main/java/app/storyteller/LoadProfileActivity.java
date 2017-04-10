package app.storyteller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.database.DBHandler;
import app.storyteller.fragments.MainHomeFragment;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.models.Account;

/**
 * This Activity is a "Transition Activity". Meaning that, when the
 * application is waiting for some events (e.g. Api response, Google
 * authentication process), this Activity is displayed.
 *
 * When the Application is ready, this Activity will proceed to
 * next Activity.
 */
public class LoadProfileActivity extends AppCompatActivity {

    /**
     * Extras.
     */
    private String account_id;
    private boolean isAccountInDb;


    //----------------------------------------------------------------------------
    // Constructor
;
    /**
     * TODO.
     **/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_profile);
        DBHandler.openConnection(getApplicationContext());

        // -- Get the next Activity.
        isAccountInDb = getIntent().getBooleanExtra("is_account_in_database", false);
        account_id = getIntent().getStringExtra ("account_id");

        if (isAccountInDb){
            loadAccountFromDatabase();
            fetchProfileImage();
        } else {
            createAccountOnApi();
        }
    }



    //----------------------------------------------------------------------------
    // Methods

    private void loadAccountFromDatabase(){
        StoryTellerManager.setAccount(DBHandler.getAccount(account_id));
        DBHandler.closeConnection();
    }

    /**
     *
     */
    private void createAccountOnApi(){
        DBHandler.closeConnection();
        Api.executeRequest(ApiRequests.createProfile(
                account_id,
                getIntent().getStringExtra("account_name"),
                getIntent().getStringExtra("account_image_url")
        ), this);
    }

    /**
     *
     */
    public void onAccountCreated(){
        fetchProfileImage();
    }

    /**
     *
     */
    public void onAccountReady() {
        startActivity(new Intent(this, MainActivity.class));
    }


    //-----------------------------------------------------------------------------------
    // Profile Image

    /**
     * This method fetches the current Google Account Avatar/Image
     * and stores it locally. If the image is already fetched. The
     * application launches normally.
     */
    private void fetchProfileImage(){
        new LoadImageFromUrl(StoryTellerManager.getAccount().getImageURL(),
                this).execute();
    }

    /**
     * Downloads the Google Profile image temporally and displays it in the
     * Profile's ImageView.
     */
    private class LoadImageFromUrl extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private LoadProfileActivity activity;

        LoadImageFromUrl(String url, LoadProfileActivity activity) {
            this.url = url;
            this.activity = activity;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap image = null;
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlConnection
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream input = conn.getInputStream();
                image = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            StoryTellerManager.setAccountImage(result);
            Api.executeRequest(ApiRequests.updateProfile(), activity);
            activity.onAccountReady();
        }
    }
}
