package com.example.s135123.kitchener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_schedule,container,false);

        list = (ListView) v.findViewById(R.id.listView_schedule);
        adapter = new ScheduleAdapter(getActivity(), recipes);
        list.setAdapter(adapter);

        generateButton = (Button) v.findViewById(R.id.gen_schedule_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipes.clear();
                new ScheduleTask().execute((Void) null);
            }
        });
        seekBar = (SeekBar) v.findViewById(R.id.schedule_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                generateButton.setText("Generate a schedule for "+Integer.toString(progress + 2)+" days");
                days=progress+2;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return v;
        // return inflater.inflate(R.layout.tab_schedule, container, false);

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
            for(int i = 0; i<recipeArray.length(); i++){
                try {
                    recipes.add(new Recipe(recipeArray.get(i).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
