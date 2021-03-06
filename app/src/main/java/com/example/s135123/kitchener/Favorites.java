package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by s142451 on 16-3-2016.
 */
public class Favorites extends AppCompatActivity {
    ListView list;
    ArrayList<Recipe> recipes = new ArrayList<>();
    CompactBaseAdapter adapter;
    TextView noInternetTextFav;
    RelativeLayout favoritesLayout;
    User user;
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    SendRequest sendRequest = new SendRequest();
    boolean loaded = false; //indicates if the favorite recipes were retrieved from the server


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        if(getResources().getBoolean(R.bool.isPhone)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFavorites);
        setSupportActionBar(toolbar);
        favoritesLayout = (RelativeLayout) findViewById(R.id.favorites_layout);
        //reload recipes by tapping the activity (useful when there was no internet connection during onCreate())
        favoritesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loaded) {
                    loadRecipes();
                }
            }
        });
        user = User.getInstance();
        noInternetTextFav = (TextView) findViewById(R.id.text_fav_no_internet);
        list = (ListView) findViewById(R.id.listView_favorites);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.favoritesImageViewRec) {
                    if (sendRequest.isNetworkAvailable(Favorites.this)) {
                        int recipeId = recipes.get(position).getId();
                        boolean addTofavorite = !user.getFavorites().contains(recipeId);
                        new FavoritesTask(position, Favorites.this, recipeId, adapter).execute(addTofavorite);
                        if(!addTofavorite){
                            recipes.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        if(recipes.size()==0){
                            noInternetTextFav.setText("You currently have no favorite recipes");
                            noInternetTextFav.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast toast = Toast.makeText(Favorites.this, "Unable to reach the server to modify favorites", Toast.LENGTH_LONG);
                        toast.show();
                    }

                } else {
                    if(getResources().getBoolean(R.bool.isPhone)) {
                        Intent i = new Intent(Favorites.this, RecipeInfoActivity.class);
                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.putExtra("Recipe", recipes.get(position));
                        startActivity(i);
                    }
                    else{
                        new RecipeInfo(Favorites.this).updateContents(recipes.get(position));
                    }
                }
            }
        });
        adapter = new CompactBaseAdapter(this, recipes, false);
        list.setAdapter(adapter);
        loadRecipes();
        boolean isPhone = getResources().getBoolean(R.bool.isPhone);
        if(isPhone) {
            ViewGroup.LayoutParams paramsLinear = favoritesLayout.getLayoutParams();
            paramsLinear.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this, System.currentTimeMillis());
    }

    private void loadRecipes(){
        if (sendRequest.isNetworkAvailable(Favorites.this)) {
            recipes.clear();
            adapter.notifyDataSetChanged();
            GetFavoritesTask task = new GetFavoritesTask();
            noInternetTextFav.setVisibility(View.GONE);
            task.execute((Void) null);
        } else {
            noInternetTextFav.setText("No network available to retrieve recipes. Tap to retry");
            noInternetTextFav.setVisibility(View.VISIBLE);
        }
    }
    public class GetFavoritesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            User user = User.getInstance();
            String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/" + user.getUsername() + "/" + user.getPassword();
            JSONObject authTokenJson = null;
            try {
                authTokenJson = new JSONObject(sendRequest.sendGetRequest(authTokenUrl));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(authTokenJson==null){
                //something went wrong
                return "failed";
            }
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int id:user.getFavorites()) {
                String idUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken + "/specific/" + id;
                final String response = sendRequest.sendGetRequest(idUrl);
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status != 200) {
                        return "failed";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //data of the listview adapter must be changed from the UI thread
                runOnUiThread(new Runnable() {
                    public void run() {
                        recipes.add(new Recipe(response));
                    }

                });
            }
            return "success";
        }

        @Override
        protected void onPostExecute(final String result) {
            if(result.equals("failed")){
                noInternetTextFav.setText("Unable to reach the server. Tap to retry");
                noInternetTextFav.setVisibility(View.VISIBLE);
            }
            else {
                loaded = true;
                adapter.notifyDataSetChanged();
                if(recipes.size()==0){
                    noInternetTextFav.setText("You currently have no favorite recipes");
                    noInternetTextFav.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    @Override
    public void onPause(){
        super.onPause();
            sensorManager.unregisterListener(shakeDetector);
    }
    @Override
    public void onResume(){
        super.onResume();
            sensorManager.registerListener(shakeDetector,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

}