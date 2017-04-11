package app.storyteller.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.api.GoogleApiClient;

import app.storyteller.models.Account;

/**
 * Created by Mihai on 2017-04-11.
 */

public class AccountManager {

    /**
     * Current logged-in Account OR the last Account that logged-in
     * since the last visit.
     */
    private Account account;

    /**
     * Current logged-in Account's Image.
     */
    private static Bitmap accountImage;

    /**
     *
     */
    private TokenManager tokenManager;

    /**
     *
     */
    public AccountManager(Account account){
        this.account = account;
        this.tokenManager = new TokenManager(account);
    }


    //-------------------------------------------------------
    // Getters and Setters

    public Account getAccount(){ return account; }
    public TokenManager getTokenManager() {return tokenManager; }
    public Bitmap getAccountImage(){ return accountImage; }

    public void setAccountImage(Bitmap bitmap){ accountImage = bitmap; }


    //-------------------------------------------------------
    // Methods



}
