package com.shubhu.twitterLogin;

/**
 * Created by Ranosys Technologies on 9/2/17.
 */

public class TwitterLibConstants {


    static String TAG = "TwitterLogin";
    //this string is use as the key for passing the value of twitter_key using intent
    public static final String TWITTER_KEY="twitter_key";
    //this string is use as the key for passing the value of twitter_secret using intent
    public static final String TWITTER_SECRET="twitter_secret";
    public static final String USERNAME = "user name";
    public static final String PROFILE_IMAGE_URL = "profile image url";
    public static final String USER_ID = "user id ";
    public static final String LOCATION = "user id ";
    public static final String EMAIL = "email";
    public static final String EMAIL_ERROR_REASON = "email_error_reason";
    //This is key for containg user basic detail bundle
    public static final String USER_DETAIL = "user detail";
    //This is error when login is failure
    public static final String LOGIN_ERROR = "login error";
    //This is error when getting result after successfull login
    public static final String ERROR_GETTING_BASIC_DETAIL = "error getting detail";
    //This is result code for when after successfull login no user data is come
    public static final int RESULT_CODE_ERROR_FETCHING_BASIC = 1234;
    //this is key for specify action before calling TwitterLogin activity
    //value of action can be logout or login
    public static final String ACTION = "action";
    public static final String ACTION_LOG_IN = "log in";
    public static final String ACTION_LOG_OUT = "log_out";
}
