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
 * Class that detects shaking of the device
 * should be deregistered in activities in onPause, otherwise it will also register shakes when the app is closed
 */
public class ShakeDetector implements SensorEventListener {
    Activity activity;
    long lastTime = Long.MAX_VALUE;
    User user = User.getInstance();

    public ShakeDetector(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            long time=event.timestamp;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            float vector = x * x + y * y + z * z;
            if (vector > 500) {
                if(user.getShakeEnabled()&&lastTime-time>=1000){
                    System.out.println(lastTime+" is da time");
                    lastTime = time;
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
