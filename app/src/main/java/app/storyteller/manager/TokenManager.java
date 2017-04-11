package app.storyteller.manager;

import android.app.Activity;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.models.Account;

/**
 * Created by Mihai on 2017-04-11.
 */

public class TokenManager {

    private Account account;

    /**
     *
     * @param account
     */
    public TokenManager(Account account){
        this.account = account;
    }

    //----------------------------------------------------------------
    // Methods

    /**
     *
     * @param activity
     */
    public void grantToken(Activity activity){
        account.grantToken();
        Api.executeRequest(ApiRequests.updateProfile(), activity);
    }

    /**
     *
     * @param activity
     */
    public void consumeToken(Activity activity){
        account.consumeToken();
        Api.executeRequest(ApiRequests.updateProfile(), activity);
    }
}
