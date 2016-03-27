package com.example.s135123.kitchener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
public class Favorites extends android.support.v4.app.Fragment {
    private TextView textView;
    ListView list;
    ArrayList<Recipe> recipes = new ArrayList<>();
    CompactBaseAdapter adapter;
    TextView noInternetTextFav;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.favorites_fragment, container, false);
        user = User.getInstance();
        noInternetTextFav = (TextView) v.findViewById(R.id.text_fav_no_internet);
        loadRecipes();
        list = (ListView) v.findViewById(R.id.listView_favorites);
        adapter = new CompactBaseAdapter(getActivity(), recipes);
        list.setAdapter(adapter);
        return v;
    }
    private void loadRecipes(){
        if (isNetworkAvailable()) {
            GetFavoritesTask task = new GetFavoritesTask();
            noInternetTextFav.setVisibility(View.GONE);
            task.execute((Void) null);

        } else {
            noInternetTextFav.setText("No network available to retrieve recipes. Tap to retry");
            noInternetTextFav.setVisibility(View.VISIBLE);
        }
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public class GetFavoritesTask extends AsyncTask<Void, Void, String> {

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
                String idUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken + "/specific/"+id;
                String response = sendRequest.sendGetRequest(idUrl);
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if(status != 200){
                        return "failed";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recipes.add(new Recipe(response));
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
                adapter.notifyDataSetChanged();
            }
        }

    }

}