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
                    boolean add = !user.getAllergies().contains(allAllergens.get(position));
                    new AllergensTask(allAllergens.get(position)).execute(add);
                }
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);

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

    private class AllergensTask extends AsyncTask<Boolean, Void, String> {
        String allergen;

        public AllergensTask(String allergen) {
            this.allergen = allergen;
        }

        @Override
        protected String doInBackground(Boolean... params) {
            User user = User.getInstance();
            //String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/test/test123";
            String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/" + user.getUsername() + "/" + user.getPassword();
            JSONObject authTokenJson = null;
            SendRequest sendRequest = new SendRequest();
            try {
                authTokenJson = new JSONObject(sendRequest.sendGetRequest(authTokenUrl));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (authTokenJson == null) {
                //something went wrong
                return "failed";
            }
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (params[0]) {
                //add to allergens
                String addToAllergensUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/addAllergens";
                int statusCode = sendRequest.sendPostRequest(addToAllergensUrl, allergen);
                if (statusCode != 200) {
                    return "failed";
                } else {
                    if (!user.getAllergies().contains(allergen)) {
                        user.addAllergy(allergen);
                        allergens.add(allergen);
                    }
                }
            } else {
                //remove from favorites
                String addToAllergensUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/deleteAllergens";
                int statusCode = sendRequest.sendDeleteRequest(addToAllergensUrl, allergen);
                System.out.println("allergens delete url: " + addToAllergensUrl);
                System.out.println("allergens statuscode: " + statusCode);
                if (statusCode != 200) {
                    return "failed";
                } else {
                    user.removeAllergy(allergen);
                    allergens.remove(allergen);
                }
            }
            return "success";
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("failed")) {
                Toast toast = Toast.makeText(Allergens.this, "Unable to reach the server to modify allergens", Toast.LENGTH_LONG);
                toast.show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}