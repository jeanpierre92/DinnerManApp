package com.example.s135123.kitchener;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by s136693 on 5-3-2016.
 */

public class Tab_Recommendations extends android.support.v4.app.Fragment {

    ListView list;
    ArrayList<Recipe> recipes = new ArrayList<>();
    CompactBaseAdapter adapter;
    TextView noInternetText;
    RelativeLayout recommendationLayout;
    ImageView questionMark;
    final User user = User.getInstance();
    SendRequest sendRequest = new SendRequest();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_recommendations, container, false);
        noInternetText = (TextView) v.findViewById(R.id.text_rec_no_internet);
        recommendationLayout = (RelativeLayout) v.findViewById(R.id.recommendation_layout);
        recommendationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecipes();
            }
        });
        loadRecipes();
        list = (ListView) v.findViewById(R.id.listView_reccomendations);
        adapter = new CompactBaseAdapter(getActivity(), recipes, true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.favoritesImageViewRec) {
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
        list.setAdapter(adapter);
        questionMark = (ImageView) v.findViewById(R.id.question_mark_rec);
        questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TutorialRec.class);
                startActivity(i);
            }
        });
        return v;
        // return inflater.inflate(R.layout.tab_recommendations, container, false);
    }

    private void loadRecipes() {
        if (sendRequest.isNetworkAvailable(getActivity())) {
            RecommendRecipeTask task = new RecommendRecipeTask();
            noInternetText.setVisibility(View.GONE);
            task.execute((Void) null);

        } else {
            noInternetText.setText("No network available to retrieve recipes. Tap to retry");
            noInternetText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!user.getDidRecTutorial()) {
                user.setDidRecTutorial(true);
                Intent i = new Intent(getActivity(), TutorialRec.class);
                startActivity(i);
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

    public class RecommendRecipeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            User user = User.getInstance();
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
                return "failed";
            }
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String recommendUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken + "/recommendation";
            String result = sendRequest.sendGetRequest(recommendUrl);
            JSONObject resultJson = null;
            try {
                resultJson = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray recipeArray = null;
            ArrayList<Recipe> recipeResults = new ArrayList<>();
            try {
                recipeArray = resultJson.getJSONArray("recipes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < recipeArray.length(); i++) {
                try {
                    Recipe recipe = new Recipe(recipeArray.get(i).toString());
                    recipeResults.add(recipe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (Recipe recipe : recipeResults) {
                recipes.add(recipe);
            }
            //sort by cuisine
            ArrayList<Recipe> sortedRecipes = new ArrayList<>(recipes);
            String cuisine = "";
            recipes.clear();
            //Sort recipes by cuisines, starting with the cuisine of the first recommendation
            //then other recipes with the same cuisine
            //then the recipes with the cuisine of the second recommendation
            for (Recipe r : sortedRecipes) {
                if (!recipes.contains(r)) {
                    recipes.add(r);
                    cuisine = r.getCuisine();
                    for (Recipe r2 : sortedRecipes) {
                        if (!recipes.contains(r2) && r2.getCuisine().equals(cuisine)) {
                            recipes.add(r2);
                        }
                    }
                }
            }
            return "success";
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("failed")) {
                noInternetText.setText("Unable to reach the server. Tap to retry");
                noInternetText.setVisibility(View.VISIBLE);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}