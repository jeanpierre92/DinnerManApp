package com.example.s135123.kitchener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by s136693 on 5-3-2016.
 */

public class Tab_Schedule extends android.support.v4.app.Fragment {
    ListView list;
    ArrayList<Recipe> recipes = new ArrayList<>();
    ScheduleAdapter adapter;
    SeekBar seekBar;
    Button generateButton;
    int days=2;//number of days to generate a schedule for
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_schedule,container,false);
        list = (ListView) v.findViewById(R.id.listView_schedule);
        adapter = new ScheduleAdapter(getActivity(), recipes);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if(viewId==R.id.rerollImageView){
                    System.out.println("started rerolling");
                    if (isNetworkAvailable()) {
                        new RerollTask().execute(position);
                        System.out.println("started rerolling");
                    }else {
                        Toast toast = Toast.makeText(getContext(), "No network available to retrieve a new recipe", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else{
                    Intent i = new Intent(getContext(), RecipeInfoActivity.class);
                    i.putExtra("Recipe", recipes.get(position));
                    getContext().startActivity(i);
                }
            }
        });

        //load a schedule if there is one
        prefs = getActivity().getPreferences(getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        for(int i =0; i<7; i++){
            String json = prefs.getString("recipe"+i, null);
            System.out.println(json+"josn");
            if(json!=null) {
                System.out.println("not null");
                Recipe recipe = gson.fromJson(json, Recipe.class);
                recipes.add(recipe);
            }

        }
        generateButton = (Button) v.findViewById(R.id.gen_schedule_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(recipes.size());
                if (isNetworkAvailable()) {
                    if(recipes!=null) {
                        recipes.clear();
                    }
                    for(int i = 0; i<7; i++){
                        SharedPreferences.Editor prefsEditor = prefs.edit();
                        prefsEditor.putString("recipe"+i, null);
                        prefsEditor.commit();

                    }
                    new ScheduleTask().execute((Void) null);
                }else {
                    Toast toast = Toast.makeText(getActivity(), "No network available to retrieve a schedule", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        seekBar = (SeekBar) v.findViewById(R.id.schedule_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                generateButton.setText("Generate a schedule for " + Integer.toString(progress + 2) + " days");
                days = progress + 2;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return v;
        // return inflater.inflate(R.layout.tab_schedule, container, false);

    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public class ScheduleTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
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
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String recommendUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/"+user.getUsername()+"/"+authToken+"/schedule/"+days;
            String result = sendRequest.sendGetRequest(recommendUrl);
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            JSONObject resultJson =null;
            try {
                resultJson = new JSONObject (result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray recipeArray = null;
            try {
                recipeArray = resultJson.getJSONArray("schedule");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor prefsEditor = prefs.edit();
            Gson gson = new Gson();
            for(int i = 0; i<recipeArray.length(); i++){
                try {
                    Recipe recipe = new Recipe(recipeArray.get(i).toString());
                    recipes.add(recipe);
                    String json = gson.toJson(recipe);
                    System.out.println("json in schedule: "+json);
                    prefsEditor.putString("recipe"+i, json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
            //save the new schedule
            prefsEditor.commit();
        }
    }
    public class RerollTask extends AsyncTask<Integer, Void, String> {
        int position;

        @Override
        protected String doInBackground(Integer... params) {
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
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String cuisines = "reroll,";
            position = params[0];
            if(position>0&&position<recipes.size()-1){
                cuisines+=recipes.get(position-1).getCuisine()+","+recipes.get(position+1).getCuisine();
            }
            else if(position>0){
                cuisines+=recipes.get(position-1).getCuisine();
            }
            else if(position<recipes.size()-1){
                cuisines+=recipes.get(position+1).getCuisine();
            }
            String rerollUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/"+user.getUsername()+"/"+authToken+"/schedule/reroll/"+cuisines;
            String recipe = sendRequest.sendGetRequest(rerollUrl);
            System.out.println(rerollUrl);
            return recipe;
        }

        @Override
        protected void onPostExecute(final String recipeString) {
            Recipe recipe = new Recipe(recipeString);
            recipes.set(position,recipe);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(recipe);
            prefsEditor.putString("recipe"+position, json);
            prefsEditor.commit();
            adapter.notifyDataSetChanged();
        }
    }
}
