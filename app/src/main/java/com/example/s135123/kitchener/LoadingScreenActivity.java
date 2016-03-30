package com.example.s135123.kitchener;

import android.app.Activity;

/**
 * Created by s135123 on 22-3-2016.
 */

import android.content.Context;
import android.content.Intent;
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

public class LoadingScreenActivity extends Activity
{
    //creates a ViewSwitcher object, to switch between Views
    private ViewSwitcher viewSwitcher;

    public static Context applicationContext;   //used for shared preferences
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        applicationContext=getApplicationContext();

        //Initialize a LoadViewTask object and call the execute() method
        new LoadViewTask().execute();
    }

    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Boolean>
    {
        //A TextView object and a ProgressBar object
        private TextView tv_progress;
        private ProgressBar pb_progressBar;

        //Before running code in the separate thread
        @Override
        protected void onPreExecute()
        {
            //Initialize the ViewSwitcher object
            //viewSwitcher = new ViewSwitcher(LoadingScreenActivity.this);
	        /* Initialize the loading screen with data from the 'loadingscreen.xml' layout xml file.
	         * Add the initialized View to the viewSwitcher.*/
            //viewSwitcher.addView(ViewSwitcher.inflate(LoadingScreenActivity.this, R.layout.loadingscreen, null));
            setContentView(R.layout.loadingscreen);

            //Initialize the TextView and ProgressBar instances - IMPORTANT: call findViewById() from viewSwitcher.
            tv_progress = (TextView) findViewById(R.id.tv_progress);
            pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
            //Sets the maximum value of the progress bar to 100
            pb_progressBar.setMax(100);

            //Set ViewSwitcher instance as the current View.
            //setContentView(viewSwitcher);
        }

        //The code to be executed in a background thread.
        @Override
        protected Boolean doInBackground(Void... params)
        {
            User user = User.getInstance();
            if(user.getUsername()==null && user.getPassword()==null) {
                System.out.println("allebei null");
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            }
            else{
                String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/" + user.getUsername() + "/" + user.getPassword();
                JSONObject authTokenJson = null;
                SendRequest sendRequest = new SendRequest();
                try {
                    authTokenJson = new JSONObject(sendRequest.sendGetRequest(authTokenUrl));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                publishProgress(25);
                if(authTokenJson==null){
                    System.out.println("could not authenticate");
                    return false;
                }
                String authToken = null;
                try {
                    authToken = authTokenJson.getString("authToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String profileUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/"+user.getUsername()+"/"+authToken+"/profile";
                String profileString = sendRequest.sendGetRequest(profileUrl);
                publishProgress(50);
                JSONObject profile = null;
                try {
                    profile = new JSONObject(profileString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray favoritesJsonArray = null;
                try {
                    favoritesJsonArray = profile.getJSONArray("favorites");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int length = favoritesJsonArray.length();
                for(int i = 0; i < length; i++){
                    publishProgress(50+50*i/length);
                    try {
                        user.addToFavorites(Integer.parseInt(favoritesJsonArray.get(i).toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            }
            //return false;
        }

        //Update the TextView and the progress at progress bar
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //Update the progress at the UI if progress value is smaller than 100
            if(values[0] <= 100)
            {
                tv_progress.setText("Progress: " + Integer.toString(values[0]) + "%");
                pb_progressBar.setProgress(values[0]);
            }
        }

        //After executing the code in the thread
        @Override
        protected void onPostExecute(Boolean result)
        {
            if(!result){
                //internet connection was lost while authenticating, go to login
                Toast toast = Toast.makeText(LoadingScreenActivity.this, "Unable to reach the server to authenticate", Toast.LENGTH_LONG);
                toast.show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }

    //Override the default back key behavior
    @Override
    public void onBackPressed()
    {
        //Emulate the progressDialog.setCancelable(false) behavior
        //If the first view is being shown
        if(viewSwitcher.getDisplayedChild() == 0)
        {
            //Do nothing
            return;
        }
        else
        {
            //Finishes the current Activity
            super.onBackPressed();
        }
    }
}