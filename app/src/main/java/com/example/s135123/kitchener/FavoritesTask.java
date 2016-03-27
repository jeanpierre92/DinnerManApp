package com.example.s135123.kitchener;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by s130604 on 27-3-2016.
 */
public class FavoritesTask extends AsyncTask<Boolean, Void, String> {
    int position;
    Activity activity;
    int id;
    BaseAdapter adapter;

    public FavoritesTask(int pos, Activity a, int id, BaseAdapter adapter) {
        this.id = id;
        position = pos;
        activity=a;
        this.adapter = adapter;
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
            //add to favorites
            String addToFavoritesUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/addFavorites";
            int statusCode = sendRequest.sendPostRequest(addToFavoritesUrl, Integer.toString(id));
            if (statusCode != 200) {
                return "failed";
            } else {
                if (!user.getFavorites().contains(id)) {
                    user.addToFavorites(id);
                }
            }
        } else {
            //remove from favorites
            String addToFavoritesUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/deleteFavorites";
            int statusCode = sendRequest.sendDeleteRequest(addToFavoritesUrl, Integer.toString(id));
            if (statusCode != 200) {
                return "failed";
            } else {
                user.removeFromFavorites(id);
            }
        }
        return "success";
    }

    @Override
    protected void onPostExecute(final String result) {
        if (result.equals("failed")) {
            Toast toast = Toast.makeText(activity, "Unable to reach the server to modify favorites", Toast.LENGTH_LONG);
            toast.show();
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
