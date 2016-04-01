package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by s130604 on 22-3-2016.
 */
public class RandomRecipeThread extends Thread {
    Activity activity;
    public RandomRecipeThread(Activity activity){
        this.activity=activity;
    }
    @Override
    public void run(){
        User user = User.getInstance();
        String authTokenUrl = "http://appdev-gr1.win.tue.nl:8008/api/authenticate/" + user.getUsername() + "/" + user.getPassword();
        JSONObject authTokenJson=null;
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
        String recipeUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/"+user.getUsername()+"/" + authToken + "/random";
        String recipeResult = sendRequest.sendGetRequest(recipeUrl);
        Intent i = new Intent(activity, RecipeInfoActivity.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        Recipe recipe =new Recipe(recipeResult);
        i.putExtra("Recipe", recipe);
        activity.startActivity(i);
    }
}
