package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeInfoActivity extends AppCompatActivity {
    User user = User.getInstance();

    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        if(getResources().getBoolean(R.bool.isPhone)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        new RecipeInfo(this).updateContents(recipe);

    }

    @Override
    public void onPause(){
        super.onPause();
        if (user.getShakeEnabled()) {
            sensorManager.unregisterListener(shakeDetector);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (user.getShakeEnabled()) {
            sensorManager.registerListener(shakeDetector,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

}
