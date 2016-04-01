package com.example.s135123.kitchener;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * add to or remove allergens from the profile
 */
public class AllergensTask extends AsyncTask<Boolean, Void, String> {
    String allergen;
    ArrayList<String> allergens;
    BaseAdapter adapter;
    Activity activity;

    public AllergensTask(String allergen, ArrayList<String> allergens, BaseAdapter adapter, Activity activity) {
        this.allergen = allergen;
        this.allergens = allergens;
        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Boolean... params) {
        User user = User.getInstance();
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
            //remove from allergens
            String addToAllergensUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/deleteAllergens";
            int statusCode = sendRequest.sendDeleteRequest(addToAllergensUrl, allergen);
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
            Toast toast = Toast.makeText(activity, "Unable to reach the server to modify allergens", Toast.LENGTH_LONG);
            toast.show();
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}