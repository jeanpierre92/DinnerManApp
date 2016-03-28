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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by s136693 on 5-3-2016.
 */

public class Tab_Search extends android.support.v4.app.Fragment implements View.OnClickListener {
    ArrayList<Recipe> recipes = new ArrayList<>();
    ListView list;
    CompactBaseAdapter adapter;

    User user = User.getInstance();

    //EditText views needed to hide them if an advanced search is necessary
    EditText editTextSearch;
    EditText editTextIncludeIngredients;
    EditText editTextExcludeIngredients;
    EditText editTextAllergens;
    //TextView views needed to hide them if an advanced search is necessary
    TextView textViewIncludeIngredients;
    TextView textViewExcludeIngredients;
    TextView textViewAllergens;
    TextView textViewCal;
    TextView textViewCarbs;
    TextView textViewProtein;
    TextView textViewFat;
    RangeSeekBar calSeekBar;
    RangeSeekBar proteinSeekBar;
    RangeSeekBar fatSeekBar;
    RangeSeekBar carbsSeekBar;
    RelativeLayout advancedOptionsLayout;

    // Buttons needed to set onClickListener()
    Button buttonSearch;
    Button buttonAdvancedOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_search, container, false);

        list = (ListView) v.findViewById(R.id.listView_reccomendations);
        View header = inflater.inflate(R.layout.search_header, null);
        list.addHeaderView(header, null, false);list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                 if (viewId == R.id.favoritesImageViewRec) {
                    System.out.println("started favoriting");
                    if (isNetworkAvailable()) {
                        int recipeId = recipes.get(position).getId();
                        boolean addTofavorite = !user.getFavorites().contains(recipeId);
                        new FavoritesTask(position, getActivity(), recipeId, adapter).execute(addTofavorite);
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Unable to reach the server to modify favorites", Toast.LENGTH_LONG);
                        toast.show();
                    }

                } else {
                    Intent i = new Intent(getContext(), RecipeInfoActivity.class);
                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.putExtra("Recipe", recipes.get(position));
                    getContext().startActivity(i);
                }
            }
        });
        adapter = new CompactBaseAdapter(getActivity(), recipes, false);
        list.setAdapter(adapter);

        // EditText views found and hidden as default
        editTextSearch = (EditText) v.findViewById(R.id.editTextSearch);
        editTextIncludeIngredients = (EditText) v.findViewById(R.id.editTextIncludeIngredients);
        editTextExcludeIngredients = (EditText) v.findViewById(R.id.editTextExcludeIngredients);
        editTextAllergens = (EditText) v.findViewById(R.id.editTextAllergens);

        // TextView views found and hidden as default
        textViewIncludeIngredients = (TextView) v.findViewById(R.id.textViewIncludeIngredients);
        textViewExcludeIngredients = (TextView) v.findViewById(R.id.textViewExcludeIngredients);
        textViewCal = (TextView) v.findViewById(R.id.textViewCal);
        textViewCarbs = (TextView) v.findViewById(R.id.textViewCarbs);
        textViewFat = (TextView) v.findViewById(R.id.textViewFat);
        textViewProtein = (TextView) v.findViewById(R.id.textViewProtein);
        textViewAllergens = (TextView) v.findViewById(R.id.textViewAllergens);


        buttonSearch = (Button) v.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        buttonAdvancedOptions = (Button) v.findViewById(R.id.buttonToggleAdvancedOptions);
        buttonAdvancedOptions.setOnClickListener(this);

        calSeekBar = (RangeSeekBar) v.findViewById(R.id.cal_seek_bar);
        fatSeekBar = (RangeSeekBar) v.findViewById(R.id.fat_seek_bar);
        carbsSeekBar = (RangeSeekBar) v.findViewById(R.id.carbs_seek_bar);
        proteinSeekBar = (RangeSeekBar) v.findViewById(R.id.protein_seek_bar);

        advancedOptionsLayout = (RelativeLayout) v.findViewById(R.id.advanced_options_layout);
        advancedOptionsLayout.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSearch:
                if (isNetworkAvailable()) {
                    if (recipes != null) {
                        recipes.clear();
                    }
                    new SearchTask(editTextSearch.getText().toString(),
                            editTextIncludeIngredients.getText().toString(),
                            editTextExcludeIngredients.getText().toString(),
                            editTextAllergens.getText().toString(),
                            (int) calSeekBar.getSelectedMinValue(),
                            (int) calSeekBar.getSelectedMaxValue(),
                            (int) carbsSeekBar.getSelectedMinValue(),
                            (int) carbsSeekBar.getSelectedMaxValue(),
                            (int) fatSeekBar.getSelectedMinValue(),
                            (int) fatSeekBar.getSelectedMaxValue(),
                            (int) proteinSeekBar.getSelectedMinValue(),
                            (int) proteinSeekBar.getSelectedMaxValue()).execute((Void) null);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "No network available to search for recipes", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case R.id.buttonToggleAdvancedOptions:
                toggleAdvancedOptions();
                break;
            default:
                break;
        }

    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    // Toggles the advanced options views (
    public void toggleAdvancedOptions() {
        if (advancedOptionsLayout.getVisibility() == View.VISIBLE) {
            advancedOptionsLayout.setVisibility(View.GONE);

        } else {
            advancedOptionsLayout.setVisibility(View.VISIBLE);
        }
    }

    public class SearchTask extends AsyncTask<Void, Void, String> {
        String query;
        String includeIngredients;
        String excludeIngredients;
        String allergens;
        int minCal, maxCal, minCarbs, maxCarbs, minFat, maxFat, minProtein, maxProtein;

        public SearchTask(String query, String includeIngredients, String excludeIngredients, String allergens, int minCal,
                          int maxCal, int minCarbs, int maxCarbs, int minFat, int maxFat, int minProtein, int maxProtein) {
            this.query = query;
            this.includeIngredients = includeIngredients;
            this.excludeIngredients = excludeIngredients;
            this.allergens = allergens;
            this.minCal = minCal;
            this.maxCal = maxCal;
            this.minCarbs = minCarbs;
            this.maxCarbs = maxCarbs;
            this.minFat = minFat;
            this.maxFat = maxFat;
            this.minProtein = minProtein;
            this.maxProtein = maxProtein;
        }

        @Override
        protected String doInBackground(Void... params) {
            includeIngredients+=",salt";
            System.out.println("mincal: "+minCal);
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
                return "";
            }
            String authToken = null;
            try {
                authToken = authTokenJson.getString("authToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String searchUrl = "http://appdev-gr1.win.tue.nl:8008/api/recipe/" + user.getUsername() + "/" + authToken;
            if(includeIngredients.equals("")){
                searchUrl+="/nutrition/";
            }
            else{
                searchUrl+="/combo/" + includeIngredients+"/";
            }
            searchUrl += minCal+"/"+maxCal+"/"+minFat+"/"+maxFat+"/"+minProtein+"/"+maxProtein+"/"+minCarbs+"/"+maxCarbs;
            String result = sendRequest.sendGetRequest(searchUrl);
            System.out.println(result);
            JSONObject resultJson = null;
            try {
                resultJson = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray recipeArray = null;
            try {
                recipeArray = resultJson.getJSONArray("recipes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < recipeArray.length(); i++) {
                try {
                    Recipe recipe = new Recipe(recipeArray.get(i).toString());
                    recipes.add(recipe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("")) {
                Toast toast = Toast.makeText(getActivity(), "Unable to reach the server to retrieve search results", Toast.LENGTH_LONG);
                toast.show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}