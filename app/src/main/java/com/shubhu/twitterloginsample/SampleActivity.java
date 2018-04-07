package com.shubhu.twitterloginsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shubhu.twitterLogin.TwitterLibConstants;
import com.shubhu.twitterLogin.TwitterLogin;

public class SampleActivity extends AppCompatActivity {
    private static final String TAG ="Sample Activity" ;
    private final int TWITTER_LOGIN_REQUEST = 4;
    private final int TWITTER_LOGOUT_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    //this function is called when press login with twitter button
    public void twitterLogin(View v) {
        Intent intent = new Intent(SampleActivity.this, TwitterLogin.class);
        intent.putExtra(TwitterLibConstants.ACTION, TwitterLibConstants.ACTION_LOG_IN);
        intent.putExtra(TwitterLibConstants.TWITTER_KEY,"ElCcUEI80w7uaMdw1c1LN6PoZ");
        intent.putExtra(TwitterLibConstants.TWITTER_SECRET,"gytZJfcPu7S6GxjqQBtru1YJc3WMMKPQ8QmetEUj8iJOiSQWZR");
        startActivityForResult(intent, TWITTER_LOGIN_REQUEST);
    }

    //this function is called when press logout with twitter button
    public void twitterLogout(View v) {
        Intent intent = new Intent(SampleActivity.this, TwitterLogin.class);
        intent.putExtra(TwitterLibConstants.ACTION, TwitterLibConstants.ACTION_LOG_OUT);
        intent.putExtra(TwitterLibConstants.TWITTER_KEY,"ElCcUEI80w7uaMdw1c1LN6PoZ");
        intent.putExtra(TwitterLibConstants.TWITTER_SECRET,"gytZJfcPu7S6GxjqQBtru1YJc3WMMKPQ8QmetEUj8iJOiSQWZR");
        startActivityForResult(intent, TWITTER_LOGOUT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //if request for twitter login
        if (requestCode == TWITTER_LOGIN_REQUEST) {
            //This result is come when login is successfull and we get the data of user
            if (resultCode == Activity.RESULT_OK) {

                Bundle userDetail = data.getBundleExtra(TwitterLibConstants.USER_DETAIL);
                Log.d(TAG, "username is  " + userDetail.getString(TwitterLibConstants.USERNAME));
                Log.d(TAG, "profile image url  " + userDetail.getString(TwitterLibConstants.PROFILE_IMAGE_URL));
                Log.d(TAG, "user id   " + userDetail.getString(TwitterLibConstants.USER_ID));
                Log.d(TAG, "location of user is " + userDetail.getString(TwitterLibConstants.LOCATION));
                String email = userDetail.getString(TwitterLibConstants.EMAIL);
                if (!TextUtils.isEmpty(email))
                    Log.d(TAG, "email of user is " + email);
                else
                    Log.d(TAG, "the reason for null email is " + userDetail.get(TwitterLibConstants.EMAIL_ERROR_REASON));
            }
            //This result is come when login is successfull and no result of user is obtain and give the reason also
            else if (resultCode == TwitterLibConstants.RESULT_CODE_ERROR_FETCHING_BASIC) {
                Log.d(TAG, "error getting user basic result" + data.getStringExtra(TwitterLibConstants.ERROR_GETTING_BASIC_DETAIL));
            }
            //This is result when login failure is occour
            else {
                String loginFailure = data.getStringExtra(TwitterLibConstants.LOGIN_ERROR);
                Log.d(TAG, "login failure" + loginFailure);
            }
        }

        //if request for twitter logout
        if (requestCode == TWITTER_LOGOUT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Logout from twitter", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "no active session of twitter", Toast.LENGTH_LONG).show();
        }
    }
}
