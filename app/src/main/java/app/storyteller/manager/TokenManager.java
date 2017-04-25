package app.storyteller.manager;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.Timer;
import java.util.TimerTask;

import app.storyteller.MainActivity;
import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;

/**
 * Created by Mihai on 2017-04-11.
 *
 * Manages all that related to the tokens of the current logged in Account.
 */
public class TokenManager {

    /**
     * Provide access to the current logged in account.
     */
    private AccountManager accountManager;

    /**
     * Async Task that will that will grant tokens after each minute.
     */
    private TokensWatcher tokensWatcher;

    /**
     * Constructor.
     */
    public TokenManager(AccountManager accountManager){
        this.accountManager = accountManager;
    }

    //----------------------------------------------------------------
    // Methods

    /**
     * Checks if the maximum/minimum numbers of tokens allowed is reached.
     */
    public boolean isMaximumReached(){ return accountManager.getAccount().maxTokensReached();}
    public boolean isMinimumReached(){ return accountManager.getAccount().minTokensReached();}

    /**
     *
     * @return
     */
    public int getTokens(){ return accountManager.getAccount().getTokens(); }

    /**
     *
     * @param activity
     */
    public void grantToken(Activity activity){
        accountManager.getAccount().grantToken();
        Api.executeRequest(ApiRequests.updateProfile(), activity);
    }

    /**
     *
     * @param activity :
     */
    public void consumeToken(Activity activity){
        accountManager.getAccount().consumeToken();
        Api.executeRequest(ApiRequests.updateProfile(), activity);
    }

    /**
     *
     * @param activity :
     */
    public void startTokensWatcher(Activity activity){
        tokensWatcher = new TokensWatcher(activity);
    }

    /**
     *
     */
    public void stopTokensWatcher(){
        tokensWatcher.cancel(true);
        tokensWatcher = null;
    }


    //-----------------------------------------------------------------
    // Async Task

    private class TokensWatcher extends AsyncTask<Integer, Long, Integer> {

        /**
         *
         */
        private static final int THRESHOLD = 60;

        /**
         * Current Activity.
         */
        private Activity activity;

        /**
         * Constructor.
         */
        TokensWatcher(Activity act){
            activity = act;
            execute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            new Reminder(1);
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);

            // --
            if (!isMaximumReached()){
                long time = System.currentTimeMillis();
                long diff = (time - values[0])/1000;
                String timeDisplayed = "";
                if(60-diff != 0)
                    timeDisplayed = "More in: "+getFormatedTime(60-diff);
                else
                    timeDisplayed = "";
                onRefreshRemainingTime(timeDisplayed);

                // --
                if(diff >= THRESHOLD) {
                    grantToken(activity);
                    onRefreshTokens(getTokens());
                }
            }
            else
            {
                String timeDisplayed = "";
                onRefreshRemainingTime(timeDisplayed);
            }
        }

        private class Reminder {
            Timer timer;
            Reminder(int seconds) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new RemindTask(), 0,seconds * 1000);}
            class RemindTask extends TimerTask {
                public void run(){
                    publishProgress(accountManager.getAccount()
                            .getLastConnected().getTime());
                }}
        }


        //--------------------------------------------------------------------
        // Methods

        /**
         *
         */
        private String getFormatedTime(long seconds){
            return seconds < 60 ? String.format("%1ds", seconds)
                    : String.format("%1dm %02ds", (long)Math.floor(seconds/60), seconds%60);
        }

        /**
         * Updates the "remaining time" in the UI.
         * @param time : text to be displayed.
         */
        private void onRefreshRemainingTime(String time){

            System.out.println(time);
            if(activity instanceof  MainActivity)
            {
                ((MainActivity)activity).onRefreshTimerText(time);
            }
        }

        /**
         * Updates the nb of tokens available to the current User.
         * @param value : Number of tokens to display.
         */
        private void onRefreshTokens(int value){
            if (activity instanceof MainActivity){
                System.out.println("********* Can be cast to MainActivity");
                ((MainActivity)activity).onRefreshUI(value);

            } else {
                System.out.println("********* Can be cast to StoryChooserActivity");
                //((StoryChooserActivity)activity).onRefreshUI(value);
            }
        }
    }
}
