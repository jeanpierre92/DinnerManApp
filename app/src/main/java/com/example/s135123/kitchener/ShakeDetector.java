package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by s130604 on 26-3-2016.
 */
public class ShakeDetector implements SensorEventListener {
    Activity activity;
    long lastTime;

    public ShakeDetector(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            float vector = x * x + y * y + z * z;
            if (vector > 500) {
                if(System.currentTimeMillis()-lastTime<1000){
                    //don't random a new recipe to avoid randoming multiple times
                }
                else {
                    lastTime = System.currentTimeMillis();
                    Thread thread = new RandomRecipeThread(activity);
                    if (isNetworkAvailable()) {
                        thread.start();
                    } else {
                        Toast toast = Toast.makeText(activity, "No network available to random a recipe", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        }
    }
    // Method to check if there is a network available
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
