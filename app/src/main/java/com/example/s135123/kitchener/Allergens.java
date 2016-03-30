package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Allergens extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    Button button_addAllergen;
    Button button_removeAllergen;
    EditText editText_allergens;

    ArrayAdapter<String> adapter;

    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergens);

        user = User.getInstance();

        // Initialising Views
        list = (ListView) findViewById(R.id.listView_allergens);
        button_addAllergen = (Button) findViewById(R.id.button_addAllergen);
        button_removeAllergen = (Button) findViewById(R.id.button_removeAllergen);
        editText_allergens = (EditText) findViewById(R.id.editText_allergens);
        System.out.println("numAllergies: "+user.getAllergies().size());
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getAllergens()
        );

        list.setAdapter(adapter);

    }


    // List allergens
    private String[] getAllergens() {
        return  Arrays.copyOf(user.getAllergies().toArray(),
                user.getAllergies().toArray().length,
                String[].class);
    }

    // Add allergens
    private void addAllergens(String allergen) {
        user.addAllergy(allergen);
        updateList();
    }

    // Remove allergens
    private void removeAllergens(String allergen) {
        user.removeAllergy(allergen);
        updateList();
    }

    private void updateList() {
        editText_allergens.setText("");
        adapter.notifyDataSetChanged();

        // TODO Make ListView list refresh more elegantly
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        String allergen = editText_allergens.getText().toString();
        System.out.println("ALLERGEN:"+allergen);
        switch (v.getId()) {
            case R.id.button_addAllergen:
                addAllergens(allergen);
                new AllergensTask(allergen).execute(true);
                break;
            case R.id.button_removeAllergen:
                removeAllergens(allergen);
                new AllergensTask(allergen).execute(false);
                break;
            default:
                updateList();
                break;
        }
    }
    private class AllergensTask extends AsyncTask<Boolean, Void, String> {
        String allergen;

        public AllergensTask(String allergen) {
            this.allergen = allergen;
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
                //add to allergens
                String addToAllergensUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/addAllergens";
                int statusCode = sendRequest.sendPostRequest(addToAllergensUrl, allergen);
                if (statusCode != 200) {
                    return "failed";
                } else {
                    if (!user.getAllergies().contains(allergen)) {
                        user.addAllergy(allergen);
                    }
                }
            } else {
                //remove from favorites
                String addToAllergensUrl = "http://appdev-gr1.win.tue.nl:8008/api/user/" + user.getUsername() + "/" + authToken + "/deleteAllergens";
                int statusCode = sendRequest.sendDeleteRequest(addToAllergensUrl, allergen);
                System.out.println("allergens delete url: "+addToAllergensUrl);
                System.out.println("allergens statuscode: "+statusCode);
                if (statusCode != 200) {
                    return "failed";
                } else {
                    user.removeAllergy(allergen);
                }
            }
            return "success";
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("failed")) {
                Toast toast = Toast.makeText(Allergens.this, "Unable to reach the server to modify allergens", Toast.LENGTH_LONG);
                toast.show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}