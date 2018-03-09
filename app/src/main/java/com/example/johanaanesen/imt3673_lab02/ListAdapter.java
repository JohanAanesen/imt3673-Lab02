package com.example.johanaanesen.imt3673_lab02;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<RssFeedModel> {


    public ListAdapter(Context context, int resource, List<RssFeedModel> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.rss_item_layout, null);
        }

        RssFeedModel p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.postTitle);
            TextView tt2 = (TextView) v.findViewById(R.id.postLink);

            if (tt1 != null) {
                tt1.setText(p.title.toString());
            }

            if (tt2 != null) {
                tt2.setText(p.link.toString());
            }
        }

        return v;
    }

}