package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by s136693 on 16-3-2016.
 */


public class AllergenAdapter extends BaseAdapter {

    ArrayList<String> allergens;
    private static final ArrayList<String> allAllergens = new ArrayList<>(Arrays.asList("potatoes", "pepper", "vanilla", "coconut", "cream", "cheese", "leeks", "ginger", "eggs", "salt", "paprika", "fish", "beef", "tomatoes", "cabbage", "spinach", "sugar", "shrimp", "milk", "rice", "peanut", "onions", "mushrooms", "soy sauce", "chocolate", "mutton", "apples", "honey", "lemons", "broccoli", "carrots", "chicken", "garlic", "pasta", "mustard", "cucumber", "pork", "limes", "noodles"));
    Activity activity;

    AllergenAdapter(Activity a, ArrayList<String> allergens) {
        activity = a;
        this.allergens = allergens;
    }

    @Override
    public int getCount() {
        return allAllergens.size();
    }

    @Override
    public Object getItem(int position) {
        return allAllergens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.row_allergens, parent, false);
        }
        TextView allergen = (TextView) convertView.findViewById(R.id.allergen_text_view);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.allergen_checkbox);
        checkBox.setChecked(allergens.contains(allAllergens.get(position)));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });
        allergen.setText(allAllergens.get(position));
        return convertView;
    }
}
