package com.example.s135123.kitchener;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by s136693 on 16-3-2016.
 */


public class CompactBaseAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes;
    Context context;
    int listSize = 9;

    CompactBaseAdapter(Context c, ArrayList<Recipe> recipes) {
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
        View row = inflater.inflate(R.layout.row_recommendations, parent, false);
        TextView title = (TextView) row.findViewById(R.id.textView_recipe_title);
        TextView description = (TextView) row.findViewById(R.id.textView_recipe_description);
        TextView time = (TextView) row.findViewById(R.id.textView_recipe_time);
        ImageView thumbnail = (ImageView) row.findViewById(R.id.imageView_recipe_thumbnail);

        final Recipe recipe = recipes.get(position);
        title.setText(recipe.getTitle());
        time.setText(Integer.toString(recipe.getReadyInMinutes()));
        description.setText("DESCRIPTION");
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
        imageLoader.displayImage(recipe.getImage(), thumbnail);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RecipeInfoActivity.class);
                i.putExtra("Recipe", recipe);
                context.startActivity(i);
            }
        });
        return row;
    }
}
