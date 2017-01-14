package com.songsingasong.mychronology.main_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songsingasong.mychronology.R;
import com.songsingasong.mychronology.model.event.Event;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 11. 27..
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListViewHolder> {
    private static final String CLASS_NAME = "MainListAdapter";
    private ArrayList<Event> mEvents;
    private View.OnClickListener mOnClickListener;

    public MainListAdapter(ArrayList<Event> events, View.OnClickListener onClickListener) {
        mEvents = events;
        mOnClickListener = onClickListener;
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        v.setOnClickListener(mOnClickListener);
        MainListViewHolder vh = new MainListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainListViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.setTitle(event.getTitle());
        holder.setDate(event.getDate().toString());
        holder.setDescription(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void setEvents(ArrayList<Event> events) {
        mEvents = events;
    }
}
