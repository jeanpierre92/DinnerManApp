package com.example.s135123.kitchener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.util.Arrays;
import java.util.List;

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

    //TextView views needed to hide them if an advanced search is necessary
    RangeSeekBar calSeekBar;
    RangeSeekBar proteinSeekBar;
    RangeSeekBar fatSeekBar;
    RangeSeekBar carbsSeekBar;
    RelativeLayout advancedOptionsLayout;

    // Buttons needed to set onClickListener()
    Button buttonSearch;
    Button buttonAdvancedOptions;

    boolean searchButtonEnabled = true;

    ImageView questionMark;

    TextView textViewIncludeIngredients;
    PopupWindow popupWindow;
    ListView popupList;
    Button closePopupWindow;
    ArrayList<String> ingredients = new ArrayList<>();
    AllergenAdapter ingredientAdapter;
    private static final ArrayList<String> allAllergens = new ArrayList<>(Arrays.asList("potatoes", "pepper", "vanilla", "coconut", "cream", "cheese", "leeks", "ginger", "eggs", "salt", "paprika", "fish", "beef", "tomatoes", "cabbage", "spinach", "sugar", "shrimp", "milk", "rice", "peanut", "onions", "mushrooms", "soy sauce", "chocolate", "mutton", "apples", "honey", "lemons", "broccoli", "carrots", "chicken", "garlic", "pasta", "mustard", "cucumber", "pork", "limes", "noodles"));

    TextView textViewExcludeIngredients;
    PopupWindow popupWindowExclude;
    ListView popupListExclude;
    Button closePopupWindowExclude;
    ArrayList<String> ingredientsExclude = new ArrayList<>();
    AllergenAdapter ingredientAdapterExclude;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_search, container, false);

        list = (ListView) v.findViewById(R.id.listView_reccomendations);
        View header = inflater.inflate(R.layout.search_header, null);
        list.addHeaderView(header, null, false);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        adapter = new CompactBaseAdapter(getActivity(), recipes, false);
        list.setAdapter(adapter);

        LayoutInflater layoutInflaterExclude = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutExclude = layoutInflaterExclude.inflate(R.layout.popup_search_exclude, popupListExclude);
        popupWindowExclude = new PopupWindow(layoutExclude, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindowExclude.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindowExclude.setOutsideTouchable(false);

        ingredientAdapterExclude = new AllergenAdapter(getActivity(), ingredientsExclude);
        popupListExclude = (ListView) layoutExclude.findViewById(R.id.popup_list_exclude);
        popupListExclude.setAdapter(ingredientAdapterExclude);
        popupListExclude.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.allergen_checkbox) {
                    if (((CheckBox) view.findViewById(R.id.allergen_checkbox)).isChecked()) {
                        ingredientsExclude.add(allAllergens.get(position));
                    } else {
                        ingredientsExclude.remove(allAllergens.get(position));
                    }
                    //adapter.notifyDataSetChanged();
                }
            }
        });
        closePopupWindowExclude = (Button) layoutExclude.findViewById(R.id.buttonSearchPopupExclude);
        closePopupWindowExclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowExclude.dismiss();
            }
        });
        textViewExcludeIngredients = (TextView) v.findViewById(R.id.selectExcludeIngredients);
        textViewExcludeIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPhone = getResources().getBoolean(R.bool.isPhone);
                if (isPhone) {
                    popupWindowExclude.showAtLocation(layoutExclude, Gravity.CENTER_VERTICAL, 0, 0);
                } else {
                    popupWindowExclude.showAtLocation(layoutExclude, Gravity.NO_GRAVITY, 0, 0);
                }
            }
        });

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.popup_search, popupList);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(false);

        ingredientAdapter = new AllergenAdapter(getActivity(), ingredients);
        popupList = (ListView) layout.findViewById(R.id.popup_list);
        popupList.setAdapter(ingredientAdapter);
        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();
                if (viewId == R.id.allergen_checkbox) {
                    if (((CheckBox) view.findViewById(R.id.allergen_checkbox)).isChecked()) {
                        ingredients.add(allAllergens.get(position));
                    } else {
                        ingredients.remove(allAllergens.get(position));
                    }
                    //adapter.notifyDataSetChanged();
                }
            }
        });
        closePopupWindow = (Button) layout.findViewById(R.id.buttonSearchPopup);
        closePopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // TextView views found and hidden as default
        textViewIncludeIngredients = (TextView) v.findViewById(R.id.selectIncludeIngredients);
        textViewIncludeIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPhone = getResources().getBoolean(R.bool.isPhone);
                if (isPhone) {
                    popupWindow.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);
                } else {
                    popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
                }
            }
        });

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
        questionMark = (ImageView) v.findViewById(R.id.question_mark_search);
        questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TutorialSearch.class);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSearch:
                if (isNetworkAvailable()) {
                    if (searchButtonEnabled) {
                        searchButtonEnabled = false;
                        if (recipes != null) {
                            recipes.clear();
                            adapter.notifyDataSetChanged();
                        }
                        for(String allergen:ingredientsExclude){
                            new AllergensTask(allergen, ingredientsExclude, ingredientAdapterExclude, getActivity()).execute(true);
                        }
                        String ingredientsString = android.text.TextUtils.join(",", ingredients.toArray());
                        ingredientsString = ingredientsString.replaceAll(" ", "%20");
                        new SearchTask(
                                ingredientsString,
                                (int) calSeekBar.getSelectedMinValue(),
                                (int) calSeekBar.getSelectedMaxValue(),
                                (int) carbsSeekBar.getSelectedMinValue(),
                                (int) carbsSeekBar.getSelectedMaxValue(),
                                (int) fatSeekBar.getSelectedMinValue(),
                                (int) fatSeekBar.getSelectedMaxValue(),
                                (int) proteinSeekBar.getSelectedMinValue(),
                                (int) proteinSeekBar.getSelectedMaxValue()).execute((Void) null);
                        for(String allergen:ingredientsExclude){
                            new AllergensTask(allergen, ingredientsExclude, ingredientAdapterExclude, getActivity()).execute(false);
                        }
                    }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!user.getDidSearchTutorial()) {
                user.setDidSearchTutorial(true);
                Intent i = new Intent(getActivity(), TutorialSearch.class);
                startActivity(i);
            }
        }
    }

    public class SearchTask extends AsyncTask<Void, Void, String> {
        String includeIngredients;
        int minCal, maxCal, minCarbs, maxCarbs, minFat, maxFat, minProtein, maxProtein;

        public SearchTask(String includeIngredients, int minCal,
                          int maxCal, int minCarbs, int maxCarbs, int minFat, int maxFat, int minProtein, int maxProtein) {
            this.includeIngredients = includeIngredients;
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
            System.out.println("mincal: " + minCal);
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
            if (includeIngredients.equals("")) {
                searchUrl += "/nutrition/";
            } else {
                searchUrl += "/combo/" + includeIngredients + "/";
            }
            searchUrl += minCal + "/" + maxCal + "/" + minFat + "/" + maxFat + "/" + minProtein + "/" + maxProtein + "/" + minCarbs + "/" + maxCarbs;
            String result = sendRequest.sendGetRequest(searchUrl);
            System.out.println("searchUrl: " + searchUrl);
            System.out.println("searchResut: " + result);
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
            System.out.println("searchresult: " + result);
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            if (recipes.size() == 0) {
                Toast toast = Toast.makeText(getActivity(), "No recipes found", Toast.LENGTH_LONG);
                toast.show();
            }
            if (result.equals("")) {
                Toast toast = Toast.makeText(getActivity(), "Unable to reach the server to retrieve search results", Toast.LENGTH_LONG);
                toast.show();
            } else {
                adapter.notifyDataSetChanged();
            }
            searchButtonEnabled = true;
        }
    }
}