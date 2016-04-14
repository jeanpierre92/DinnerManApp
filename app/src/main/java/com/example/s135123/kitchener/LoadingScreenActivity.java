package com.example.s135123.kitchener;

import android.app.Activity;

/**
 * Created by s135123 on 22-3-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadingScreenActivity extends Activity {
    //creates a ViewSwitcher object, to switch between Views
    private ViewSwitcher viewSwitcher;
    User user;

    public static Context applicationContext;   //used for shared preferences in User

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.isPhone)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        applicationContext = getApplicationContext();
        user = User.getInstance();
        if (user.getUsername() == null && user.getPassword() == null) {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            new LoadViewTask().execute();
        }
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Boolean> {
        private TextView tv_progress;
        private ProgressBar pb_progressBar;

        @Override
        protected void onPreExecute() {

            setContentView(R.layout.loadingscreen);

            tv_progress = (TextView) findViewById(R.id.tv_progress);
            pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
            pb_progressBar.setMax(100);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = User.getInstance();
            if (user.getUsername() == null && user.getPassword() == null) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            } else {
                //show our logo for a second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //try to authenticate the user
                String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/" + user.getUsername() + "/" + user.getPassword();
                JSONObject authTokenJson = null;
                SendRequest sendRequest = new SendRequest();
                try {
                    authTokenJson = new JSONObject(sendRequest.sendGetRequest(authTokenUrl));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                publishProgress(25);
                if (authTokenJson == null) {
                    System.out.println("could not authenticate");
                    return false;
                }
                String authToken = null;
                try {
                    authToken = authTokenJson.getString("authToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String profileUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/profile";
                String profileString = sendRequest.sendGetRequest(profileUrl);
                publishProgress(50);
                JSONObject profile = null;
                try {
                    profile = new JSONObject(profileString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //get the favorites so all recipes are displayed correctly (with a full heart if favorite)
                JSONArray favoritesJsonArray = null;
                try {
                    favoritesJsonArray = profile.getJSONArray("favorites");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int length = favoritesJsonArray.length();
                for (int i = 0; i < length; i++) {
                    publishProgress(50 + 25 * i / length);
                    try {
                        user.addToFavorites(Integer.parseInt(favoritesJsonArray.get(i).toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //load the allergens
                JSONArray allergensJsonArray = null;
                try {
                    allergensJsonArray = profile.getJSONArray("allergens");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int lengthAllergens = allergensJsonArray.length();
                for (int i = 0; i < lengthAllergens; i++) {
                    publishProgress(75 + 25 * i / lengthAllergens);
                    try {
                        user.addAllergy(allergensJsonArray.getJSONObject(i).getString("allergen"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //Update the progress at the UI if progress value is smaller than 100
            if (values[0] <= 100) {
                tv_progress.setText("Progress: " + Integer.toString(values[0]) + "%");
                pb_progressBar.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                //internet connection was lost while authenticating, go to login
                Toast toast = Toast.makeText(LoadingScreenActivity.this, "Unable to reach the server to authenticate", Toast.LENGTH_LONG);
                toast.show();
                user.setUsername(null);
                user.setPassword(null);
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }

    //Override the default back key behavior
    @Override
    public void onBackPressed() {
        if (viewSwitcher.getDisplayedChild() == 0) {
            //Do nothing
            return;
        } else {
            //Finishes the current Activity
            super.onBackPressed();
        }
    }
}