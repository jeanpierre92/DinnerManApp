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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by s136693 on 16-3-2016.
 */


public class CompactBaseAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes;
    Activity activity;
    boolean showCuisine;

    CompactBaseAdapter(Activity a, ArrayList<Recipe> recipes, boolean showCuisine) {
        activity = a;
        this.recipes = recipes;
        this.showCuisine = showCuisine;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.row_recommendations, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView_recipe_title);
        TextView description = (TextView) convertView.findViewById(R.id.textView_recipe_description);
        TextView time = (TextView) convertView.findViewById(R.id.textView_recipe_time);
        TextView cuisineTitle = (TextView) convertView.findViewById(R.id.cuisine_title);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.imageView_recipe_thumbnail);
        ImageView favoritesImage = (ImageView) convertView.findViewById(R.id.favoritesImageViewRec);
        final Recipe recipe = recipes.get(position);
        final User user = User.getInstance();

        if(user.getFavorites().contains(recipe.getId())){
            favoritesImage.setImageResource(R.drawable.favorites_full);
        }
        else{
            favoritesImage.setImageResource(R.drawable.favorites_empty);
        }
        //see onItemClick in activity
        favoritesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 1);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 2);
            }
        });
        cuisineTitle.setText(recipe.getCuisine()+" recipes");
        //the cuisine is only shown in the recommendation tab
        //it is only shown once: if there are multiple Dutch recipes , there is one header Dutch recipes
        if(showCuisine){
            cuisineTitle.setVisibility(View.VISIBLE);
            for(Recipe r:recipes){
                if(r==recipe){
                    break;
                }
                if(r.getCuisine().equals(recipe.getCuisine())){
                    cuisineTitle.setVisibility(View.GONE);
                    break;
                }
            }
        }else{
            cuisineTitle.setVisibility(View.GONE);
        }
        title.setText(recipe.getTitle());
        time.setText(Integer.toString(recipe.getReadyInMinutes())+" min");
        description.setText(recipe.getSummary());
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        }
        //cache image so that they don't reload any time you scroll up and down
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage(recipe.getImage(), thumbnail, options);
        return convertView;
    }
}
