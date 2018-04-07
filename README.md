### API TwitterLogin
The api TwitterLoginlibrary use for twitter login using fabric(login and logout with twitter) in android app.
* if the login with twitter sucessfull using this library then and user also grant the email permission then bundle userDetail is sent to the calling activity in RESULT_OK result code , this userDetail Bundle contain username, profileImageUrl, userId, location and email.
* if the login with twitter sucessfull using this library then and user does not grant the email permission then bundle userDetail is sent to the calling activity in RESULT_OK result code , this userDetail Bundle contain username, profileImageUrl, userId, location and email_error_reason.
* if after successfull login result is not get due to the token expire then exception is thrown to calling activity  with the result code TwitterLibConstants.RESULT_CODE_ERROR_FETCHING_BASIC in the key TwitterLibConstants.ERROR_GETTING_BASIC_DETAIL.
* if login failure occur then reason is sent to the calling activity in the RESULT_CANCELD result code in the key TwitterLibConstants.LOGIN_ERROR.
         
                           
#### Impelementation steps

 - Copy folder in the project root directory.</br>
 for example </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project_Name/TwitterLogin 
- Now go in the <span style="color:green">settings.gradle</span> file and replace 

```sh
include ':app'
```
with 

```sh
include ':app',':twitterLogin'
```

- Open app's <span style="color:green">build.gradle</span> file and add the <span style="color:blue">compile project <span style="color:green">(path:':logger')</span></span> line in depencies module such as : </br> 

```sh
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:25.1.0'
    compile 'com.android.support:design:25.1.0'
    compile project(path: ':twitterLogin')

}
```
#### How to use this library for integrating twitter in app

#### Step 1
 - create a app <a href="https://apps.twitter.com">click here</a> and get your twitter key and secret
 - use this twitter key and secret for passing in the intent as shown in sample below

#### Step 2

#### For do login with twitter :

- for twitter login using this library user open activity for result
- sample for making intent of twitter login given below

#### How to make intent
	
```sh
	    Intent intent = new Intent(SampleActivity.this, TwitterIntegration.class);
        intent.putExtra(TwitterLibConstants.ACTION,TwitterLibConstants.ACTION_LOG_IN);
        intent.putExtra(TwitterLibConstants.TWITTER_KEY,"ElCcUEI80w7uaMdw1c1LN6PoZ");
        intent.putExtra(TwitterLibConstants.TWITTER_SECRET,"gytZJfcPu7S6GxjqQBtru1YJc3WMMKPQ8QmetEUj8iJOiSQWZR");
        startActivityForResult(intent, request_code_twitter_login);
```
#### Do changes in the onActivityResult for twitter login	

```sh
   if (requestCode == request_code_twitter_login) {
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
```
#### Step 3

#### For logout from twitter :

- for twitter logout using this library user open activity for result
- sample for making intent of twitter logout shown beow


#### How to make intent

```sh
        Intent intent = new Intent(SampleActivity.this, TwitterIntegration.class);
        intent.putExtra(TwitterLibConstants.ACTION, TwitterLibConstants.ACTION_LOG_OUT);
        intent.putExtra(TwitterLibConstants.TWITTER_KEY,"ElCcUEI80w7uaMdw1c1LN6PoZ");
        intent.putExtra(TwitterLibConstants.TWITTER_SECRET,"gytZJfcPu7S6GxjqQBtru1YJc3WMMKPQ8QmetEUj8iJOiSQWZR");
        startActivityForResult(intent, request_code_twitter_logout);
```
#### Do changes in the onActivityResult for twitter logout
```sh
    if (requestCode == request_code_twitter_logout) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Logout from twitter", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "no active session of twitter", Toast.LENGTH_LONG).show();
        }
```

