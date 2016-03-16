package com.example.s135123.kitchener;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by s136693 on 16-3-2016.
 */

class SingleRow {
    String title;
    String description;
    int image;

    SingleRow(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

}

public class CompactBaseAdapter extends BaseAdapter {

    ArrayList<SingleRow> list;
    Context context;

    CompactBaseAdapter(Context c) {
        context = c;
        list = new ArrayList<SingleRow>();

        Resources res = c.getResources();
        String[] titles = res.getStringArray(R.array.sample_recipe_titles);
        String[] descriptions = res.getStringArray(R.array.sample_recipe_descriptions);
        int[] images = {R.drawable.recipe_sample_chilli,R.drawable.recipe_sample_spiced_carrot_lentil_soup,R.drawable.recipe_sample_chicken_chorizo_jambalaya,R.drawable.recipe_sample_summer_in_winter_chicken,R.drawable.recipe_sample_spicy_root_lentil_casserole,R.drawable.recipe_sample_mustard_stuffed_chicken,R.drawable.recipe_sample_red_lentil_chickpea_chilli_soup,R.drawable.recipe_sample_falafel_burgers,R.drawable.recipe_sample_chicken_biryani};

        for(int i = 0;i < 9; i++) {
            list.add(new SingleRow(titles[i], descriptions[i],images[i]));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
        ImageView thumbnail = (ImageView) row.findViewById(R.id.imageView_recipe_thumbnail);


        SingleRow temp = list.get(position);
        title.setText(temp.title);
        description.setText(temp.description);
        thumbnail.setImageResource(temp.image);

        return row;
    }
}
