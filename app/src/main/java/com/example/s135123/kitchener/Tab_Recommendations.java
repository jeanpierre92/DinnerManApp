package com.example.s135123.kitchener;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by s136693 on 5-3-2016.
 */
public class Tab_Recommendations extends Fragment implements AdapterView.OnItemClickListener {

    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_recommendations,container,false);

        list = (ListView) v.findViewById(R.id.listView_reccomendations);
        list.setAdapter(new CompactBaseAdapter(getActivity()));
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Open new RecipeInfoActivity
    }
}