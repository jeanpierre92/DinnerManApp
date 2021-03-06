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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
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
    boolean scheduleButtonEnabled = true;
    int days;//number of days to generate a schedule for
    SharedPreferences prefs;
    User user = User.getInstance();
    ImageView questionMark;
    SendRequest sendRequest = new SendRequest();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_schedule, container, false);
        //load a schedule if there is one
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        days = 0;
        for (int i = 0; i < 7; i++) {
            String json = prefs.getString("recipe" + i, null);
            if (json != null) {
                days++;
                Recipe recipe = gson.fromJson(json, Recipe.class);
                recipes.add(recipe);
            }
        }
        days = Math.max(days, 2);
        list = (ListView) v.findViewById(R.id.listView_schedule);
        View header = inflater.inflate(R.layout.schedule_header, null);
        list.addHeaderView(header, null, false);
        adapter = new ScheduleAdapter(getActivity(), recipes);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.rerollImageView) {
                    if (sendRequest.isNetworkAvailable(getActivity())) {
                        new RerollTask(position).execute();
                    } else {
                        Toast toast = Toast.makeText(getContext(), "No network available to retrieve a new recipe", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else if (viewId == R.id.favoritesImageView) {
                    if (sendRequest.isNetworkAvailable(getActivity())) {
                        int recipeId = recipes.get(position).getId();
                        boolean addTofavorite = !user.getFavorites().contains(recipeId);
                        new FavoritesTask(position, getActivity(), recipeId, adapter).execute(addTofavorite);
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Unable to reach the server to modify favorites", Toast.LENGTH_LONG);
                        toast.show();
                    }

                } else {
                    if (getResources().getBoolean(R.bool.isPhone)) {
                        Intent i = new Intent(getContext(), RecipeInfoActivity.class);
                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.putExtra("Recipe", recipes.get(position));
                        getContext().startActivity(i);
                    } else {
                        new RecipeInfo(getActivity()).updateContents(recipes.get(position));
                    }
                }
            }
        });

        generateButton = (Button) v.findViewById(R.id.gen_schedule_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendRequest.isNetworkAvailable(getActivity())) {
                    if (scheduleButtonEnabled) {
                        scheduleButtonEnabled = false;
                        if (recipes != null) {
                            recipes.clear();
                            adapter.notifyDataSetChanged();
                        }
                        for (int i = 0; i < 7; i++) {
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.putString("recipe" + i, null);
                            prefsEditor.commit();
                        }
                        new ScheduleTask().execute((Void) null);
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), "No network available to retrieve a schedule", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        seekBar = (SeekBar) v.findViewById(R.id.schedule_seek_bar);
        seekBar.setProgress(days-2);
        generateButton.setText("Generate a schedule for " + days + " days");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                days = progress + 2;
                generateButton.setText("Generate a schedule for " + days + " days");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        questionMark = (ImageView) v.findViewById(R.id.question_mark_schedule);
        questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TutorialSchedule.class);
                startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!user.getDidScheduleTutorial()) {
                user.setDidScheduleTutorial(true);
                Intent intentTutorial = new Intent(getActivity(), TutorialSchedule.class);
                startActivity(intentTutorial);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public class ScheduleTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            //String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/test/test123";
            String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/" + user.getUsername() + "/" + user.getPassword();
            JSONObject authTokenJson = null;
            try {
                authTokenJson = new JSONObject(sendRequest.sendGetRequest(authTokenUrl));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (authTokenJson == null) {
                //something went wrong
                return "";
            }
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String recommendUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken + "/schedule/" + days;
            String result = sendRequest.sendGetRequest(recommendUrl);
            JSONObject resultJson = null;
            try {
                resultJson = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
            JSONArray recipeArray = null;
            try {
                recipeArray = resultJson.getJSONArray("schedule");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor prefsEditor = prefs.edit();
            Gson gson = new Gson();
            for (int i = 0; i < 7; i++) {
                prefsEditor.putString("recipe" + i, null);
            }
            for (int i = 0; i < recipeArray.length(); i++) {
                try {
                    Recipe recipe = new Recipe(recipeArray.get(i).toString());
                    recipes.add(recipe);
                    String json = gson.toJson(recipe);
                    prefsEditor.putString("recipe" + i, json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //save the new schedule
            prefsEditor.commit();
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("")) {
                Toast toast = Toast.makeText(getActivity(), "Unable to reach the server to retrieve a schedule", Toast.LENGTH_LONG);
                toast.show();
            } else {
                adapter.notifyDataSetChanged();
            }
            scheduleButtonEnabled = true;
        }
    }

    public class RerollTask extends AsyncTask<Void, Void, String> {
        int position;

        public RerollTask(int pos) {
            position = pos;
        }

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
            String cuisines = "";
            if (position > 0 && position < recipes.size() - 1) {
                cuisines += recipes.get(position - 1).getCuisine() + ",reroll," + recipes.get(position + 1).getCuisine();
            } else if (position > 0) {
                cuisines += recipes.get(position - 1).getCuisine() + ",reroll";
            } else if (position < recipes.size() - 1) {
                cuisines += "reroll," + recipes.get(position + 1).getCuisine();
            }
            String rerollUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken + "/schedule/reroll/" + cuisines;
            String recipeString = sendRequest.sendGetRequest(rerollUrl);
            Recipe recipe = new Recipe(recipeString);
            recipes.set(position, recipe);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(recipe);
            prefsEditor.putString("recipe" + position, json);
            prefsEditor.commit();
            return "success";
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("failed")) {
                Toast toast = Toast.makeText(getActivity(), "Unable to reach the server to retrieve a schedule", Toast.LENGTH_LONG);
                toast.show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }


}
