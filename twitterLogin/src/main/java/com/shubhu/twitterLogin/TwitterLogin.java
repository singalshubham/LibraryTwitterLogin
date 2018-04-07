package com.shubhu.twitterLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

/**
 * This is class for twitter login and logout using fabric
 * @Author : Ranosys Technologies
 */
public class TwitterLogin extends AppCompatActivity {

    //This button is used for twitter login
    TwitterLoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getIntent().getStringExtra(TwitterLibConstants.TWITTER_KEY),getIntent().getStringExtra(TwitterLibConstants.TWITTER_SECRET));
        Fabric.with(this, new Twitter(authConfig));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_twitter_integration);

        //This bundle is used for the passing userDetail as result to the calling activity
        final Bundle userDetail = new Bundle();

        //this is used for dailog activity is not closed at touching outside
        this.setFinishOnTouchOutside(false);
        loginButton = (TwitterLoginButton) findViewById(R.id.btn_twitter_login);
        //setting the callback with twitter button
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(final Result<TwitterSession> result) {
                AccountService ac = Twitter.getApiClient(result.data).getAccountService();
                Call<User> call = ac.verifyCredentials(true, false);

                call.enqueue(new Callback<User>() {
                    //if after successfull login user result is obtain
                    @Override
                    public void success(Result<User> userResult) {
                        userDetail.putString(TwitterLibConstants.USERNAME, userResult.data.name);
                        userDetail.putString(TwitterLibConstants.PROFILE_IMAGE_URL, userResult.data.profileImageUrl);
                        userDetail.putString(TwitterLibConstants.USER_ID, userResult.data.idStr);
                        userDetail.putString(TwitterLibConstants.LOCATION, userResult.data.location);

                        //request for user email after successful login
                        TwitterAuthClient authClient = new TwitterAuthClient();
                        authClient.requestEmail(result.data, new Callback<String>() {

                            //if user grant the permission then email is get
                            //if email is get then bundle userDetail is sent to the calling activity in RESULT_OK result code
                            //and userDetail contain username, profileImageUrl, userId, location and email
                            @Override
                            public void success(Result<String> result) {
                                userDetail.putString(TwitterLibConstants.EMAIL, result.data);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra(TwitterLibConstants.USER_DETAIL, userDetail);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                            //if user does not give the email permission then email not occcur and exception thrown to calling activity
                            //if email failure occour then bundle userDetail is sent to the calling activity in RESULT_OK result code
                            //and userDetail contain username, profileImageUrl, userId, location and email_error_reason
                            @Override
                            public void failure(TwitterException exception) {
                                Log.d(TwitterLibConstants.TAG, "error email failure " + exception);
                                userDetail.putString(TwitterLibConstants.EMAIL_ERROR_REASON, exception.toString());

                                Intent returnIntent = new Intent();
                                returnIntent.putExtra(TwitterLibConstants.USER_DETAIL, userDetail);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        });
                    }

                    //if after successfull login result is not get due to the token expire then exception is thrown to calling activity
                    //and reason of error getting result is thrown to calling activity with the TwitterLibConstants.RESULT_CODE_ERROR_FETCHING_BASIC result code
                    @Override
                    public void failure(TwitterException exception) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(TwitterLibConstants.ERROR_GETTING_BASIC_DETAIL, exception.toString());
                        setResult(TwitterLibConstants.RESULT_CODE_ERROR_FETCHING_BASIC, returnIntent);
                        finish();
                        Log.d(TwitterLibConstants.TAG, "Exception occoured during getting user result " + exception);
                    }
                });

            }
            //if login failure occur then come into this
            //if error occour during login then error is sent to the calling activity in the RESULT_CANCELD result code
            @Override
            public void failure(TwitterException exception) {
                Log.d(TwitterLibConstants.TAG, "Login with Twitter failure", exception);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(TwitterLibConstants.LOGIN_ERROR, exception.toString());
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        String action = getIntent().getStringExtra(TwitterLibConstants.ACTION);
        if (!TextUtils.isEmpty(action)) {
            if (action.equals(TwitterLibConstants.ACTION_LOG_IN))
                loginButton.performClick();
            else if (action.equals(TwitterLibConstants.ACTION_LOG_OUT)) {
                boolean isLogout = logoutFromTwitter(this.getApplicationContext());
                if (isLogout) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else
                {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }

            } else
                Toast.makeText(this, "Please pass correct action in intent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please pass action in intent ", Toast.LENGTH_SHORT).show();
            Log.d(TwitterLibConstants.TAG, "Please pass action in the intent ");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This function is used for the logout from twitter
     * @param context a application contect pass
     * @return true if logout successfull otherwise return false
     */
    private boolean logoutFromTwitter(Context context) {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            clearTwitterCookies(context);
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
            return true;
        }
        return false;
    }

    //this is method for clearing twitter cookies in the app
    private void clearTwitterCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }
        //for the lower version then LOLLiPOP_MR1 come into the else part
        else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
}


