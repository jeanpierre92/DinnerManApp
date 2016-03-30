package com.example.s135123.kitchener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
        switch (v.getId()) {
            case R.id.button_addAllergen:
                addAllergens(editText_allergens.getText().toString());
                break;
            case R.id.button_removeAllergen:
                removeAllergens(editText_allergens.getText().toString());
                break;
            default:
                updateList();
                break;
        }
    }
}