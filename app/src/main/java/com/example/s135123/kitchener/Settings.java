package com.example.s135123.kitchener;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Settings extends AppCompatActivity {

    CheckBox shakeActive;
    User user = User.getInstance();
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(getResources().getBoolean(R.bool.isPhone)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        shakeActive = (CheckBox) findViewById(R.id.shakeActive);
        shakeActive.setChecked(user.getShakeEnabled());
        shakeActive.setOnClickListener(mShakeListener);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this, System.currentTimeMillis());

    }

    private View.OnClickListener mShakeListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (shakeActive.isChecked()) {
                user.setShakeEnabled(true);
            } else {
                user.setShakeEnabled(false);
            }
        }
    };

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
            sensorManager.registerListener(shakeDetector,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }
    // Change standard amount of days for schedule

    // Remove all favorites

}