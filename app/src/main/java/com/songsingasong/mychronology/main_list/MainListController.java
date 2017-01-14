package com.songsingasong.mychronology.main_list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.songsingasong.mychronology.model.event.EventManager;
import com.songsingasong.mychronology.utils.SLog;

/**
 * Created by jaewoosong on 2015. 11. 27..
 */
public class MainListController implements View.OnClickListener {
    private static final String CLASS_NAME = "MainListController";

    private MainListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private EventManager.OnEventListChangeListener mEventListListChangeListener = new EventManager.OnEventListChangeListener() {
        @Override
        public void onEventListChange() {
            SLog.d(CLASS_NAME, "EventManager.OnEventListChangeListener$onEventListChange", "");
            notifyDataSetChanged();
        }
    };

    public MainListController(Context context, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);

        mRecyclerView = recyclerView;

        mAdapter = new MainListAdapter(EventManager.getInstance().getEventList(), this);
        recyclerView.setAdapter(mAdapter);

        EventManager.getInstance().registerOnEventListChangeListener(mEventListListChangeListener);
    }

    public void notifyDataSetChanged() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.setEvents(EventManager.getInstance().getEventList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerView == null || mOnItemClickListener == null) {
            return;
        }

        mOnItemClickListener.onItemClick(mRecyclerView.getChildLayoutPosition(v));
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
