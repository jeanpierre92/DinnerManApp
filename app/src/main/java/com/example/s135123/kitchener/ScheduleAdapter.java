package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by s136693 on 16-3-2016.
 */


public class ScheduleAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes;
    Context context;

    ScheduleAdapter(Context c, ArrayList<Recipe> recipes) {
        context = c;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_schedule, parent, false);
        TextView title = (TextView) row.findViewById(R.id.textView_recipe_title);
        TextView description = (TextView) row.findViewById(R.id.textView_recipe_description);
        TextView time = (TextView) row.findViewById(R.id.textView_recipe_time);
        TextView dayNumber = (TextView) row.findViewById(R.id.dayNumber);
        ImageView thumbnail = (ImageView) row.findViewById(R.id.imageView_recipe_thumbnail);
        ImageView rerollImageView = (ImageView) row.findViewById(R.id.rerollImageView);

        final Recipe recipe = recipes.get(position);
        title.setText(recipe.getTitle());
        time.setText(Integer.toString(recipe.getReadyInMinutes()) + " min");
        description.setText(recipe.getSummary());
        dayNumber.setText("Day " + Integer.toString(position));
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage(recipe.getImage(), thumbnail, options);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RecipeInfoActivity.class);
                i.putExtra("Recipe", recipe);
                context.startActivity(i);
            }
        });
        rerollImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: reroll
            }
        });
        return row;
    }
}
