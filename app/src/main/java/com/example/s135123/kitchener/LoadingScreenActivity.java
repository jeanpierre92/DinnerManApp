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
import android.widget.ViewSwitcher;

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
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
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
        protected Void doInBackground(Void... params)
        {
			/* This is just a code that delays the thread execution 4 times,
			 * during 850 milliseconds and updates the current progress. This
			 * is where the code that is going to be executed on a background
			 * thread must be placed.
			 */
            User user = User.getInstance();
            if(user.getUsername()==null && user.getPassword()==null) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
            else{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
            /*try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    //Initialize an integer (that will act as a counter) to zero
                    int counter = 0;
                    //While the counter is smaller than four
                    while(counter <= 12)
                    {
                        //Wait 850 milliseconds
                        this.wait(850);
                        //Increment the counter
                        counter++;
                        //Set the current progress.
                        //This value is going to be passed to the onProgressUpdate() method.
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }*/
            return null;
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
        protected void onPostExecute(Void result)
        {
			/* Initialize the application's main interface from the 'main.xml' layout xml file.
	         * Add the initialized View to the viewSwitcher.*/
            //viewSwitcher.addView(ViewSwitcher.inflate(LoadingScreenActivity.this, R.layout.activity_main, null));
            //Switch the Views
            //viewSwitcher.showNext();

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