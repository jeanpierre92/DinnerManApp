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
    long lastTime;
    User user = User.getInstance();

    public ShakeDetector(Activity activity, long lastTime) {
        this.activity = activity;
        this.lastTime = lastTime;
        System.out.println("lasttime: "+lastTime);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            long time=System.currentTimeMillis();
            float x = values[0];
            float y = values[1];
            float z = values[2];
            float vector = x * x + y * y + z * z;
            if (vector > 500) {
                if(user.getShakeEnabled()&&time -lastTime>=2000){
                    long dif = time-lastTime;
                    System.out.println("time since last shake: "+dif);
                    Thread thread = new RandomRecipeThread(activity);
                    if (new SendRequest().isNetworkAvailable(activity)) {
                        thread.start();
                        lastTime=time;
                    } else {
                        Toast toast = Toast.makeText(activity, "No network available to random a recipe", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
