package com.songsingasong.mychronology.event_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.songsingasong.mychronology.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaewoosong on 2015. 11. 28..
 */
public class DateSpinnerAdapter extends ArrayAdapter<String> {
    private static final String CLASS_NAME = "DateSpinnerAdapter";
    private int mResourceId;
    private List<String> mData;
    private Context mContext;

    public DateSpinnerAdapter(Context context, int resource) {
        this(context, resource, new ArrayList<String>());
    }

    public DateSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourceId = resource;
        mData = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parents) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResourceId, parents, false);
        }

        TextView tv = (TextView) view.findViewById(R.id.date_spinner_text);
        tv.setText(mData.get(position));

        return view;
    }
}
