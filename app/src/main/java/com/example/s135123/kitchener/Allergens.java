package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Allergens extends AppCompatActivity  {

    ListView list;
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    private ArrayList<String> allergens = new ArrayList<>();
    private static final ArrayList<String> allAllergens = new ArrayList<>(Arrays.asList("potatoes", "pepper", "vanilla", "coconut", "cream", "cheese", "leeks", "ginger", "eggs", "salt", "paprika", "fish", "beef", "tomatoes", "cabbage", "spinach", "sugar", "shrimp", "milk", "rice", "peanut", "onions", "mushrooms", "soy sauce", "chocolate", "mutton", "apples", "honey", "lemons", "broccoli", "carrots", "chicken", "garlic", "pasta", "mustard", "cucumber", "pork", "limes", "noodles"));

    AllergenAdapter adapter;

    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergens);
        if(getResources().getBoolean(R.bool.isPhone)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        user = User.getInstance();
        allergens = user.getAllergies();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_allergens);
        setSupportActionBar(toolbar);
        // Initialising Views
        list = (ListView) findViewById(R.id.listView_allergens);
        adapter = new AllergenAdapter(this,allergens);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.allergen_checkbox) {
                    //add the allergen if it is not in allergens, remove it otherwise
                    boolean add = !user.getAllergies().contains(allAllergens.get(position));
                    new AllergensTask(allAllergens.get(position), allergens, adapter, Allergens.this).execute(add);
                }
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this, System.currentTimeMillis());
    }




    @Override
    public void onPause() {
        super.onPause();
        if (user.getShakeEnabled()) {
            sensorManager.unregisterListener(shakeDetector);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}