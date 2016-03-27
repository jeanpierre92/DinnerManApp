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
    int listSize = 9;

    CompactBaseAdapter(Activity a, ArrayList<Recipe> recipes) {
        activity = a;
        this.recipes = recipes;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.row_recommendations, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView_recipe_title);
        TextView description = (TextView) convertView.findViewById(R.id.textView_recipe_description);
        TextView time = (TextView) convertView.findViewById(R.id.textView_recipe_time);
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
        favoritesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    int recipeId = recipes.get(position).getId();
                    boolean addTofavorite = !user.getFavorites().contains(recipeId);
                    new FavoritesTask(position, activity, recipeId, CompactBaseAdapter.this).execute(addTofavorite);
                } else {
                    Toast toast = Toast.makeText(activity, "Unable to reach the server to modify favorites", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        title.setText(recipe.getTitle());
        time.setText(Integer.toString(recipe.getReadyInMinutes())+" min");
        description.setText(recipe.getSummary());
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        if (!imageLoader.isInited()) {
            System.out.println("inited image laoder");
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage(recipe.getImage(), thumbnail, options);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, RecipeInfoActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("Recipe", recipe);
                activity.startActivity(i);
            }
        });
        return convertView;
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
