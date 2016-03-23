package com.example.s135123.kitchener;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by s136693 on 5-3-2016.
 */

public class Tab_Recommendations extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

    ListView list;
    ArrayList<Recipe> recipes = new ArrayList<>();
    CompactBaseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_recommendations, container, false);
        User user = User.getInstance();
        if (isNetworkAvailable()) {
            RecommendRecipeTask task = new RecommendRecipeTask();
            task.execute((Void) null);
        } else {
            //TODO: show no internet
        }
        list = (ListView) v.findViewById(R.id.listView_reccomendations);
        adapter = new CompactBaseAdapter(getActivity(), recipes);
        list.setAdapter(adapter);

        return v;
        // return inflater.inflate(R.layout.tab_recommendations, container, false);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Open new RecipeInfoActivity

    }// Method to check if there is a network available

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public class RecommendRecipeTask extends AsyncTask<Void, Void, String> {

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
            String recommendUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken + "/recommendation";
            String recipe = sendRequest.sendGetRequest(recommendUrl);
            return recipe;
        }

        @Override
        protected void onPostExecute(final String recipe) {
            recipes.add(new Recipe(recipe));
        }
    }

}