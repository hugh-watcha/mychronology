package com.songsingasong.mychronology.main_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.songsingasong.mychronology.R;

/**
 * Created by jaewoosong on 2015. 11. 27..
 */
public class MainListViewHolder extends RecyclerView.ViewHolder {
    private static final String CLASS_NAME = "MainListViewHolder";
    private TextView mTitle;
    private TextView mDate;
    private TextView mDescription;

    public MainListViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.tv_card_view_title);
        mDate = (TextView) itemView.findViewById(R.id.tv_card_view_date);
        mDescription = (TextView) itemView.findViewById(R.id.tv_card_view_description);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setDate(String date) {
        mDate.setText(date);
    }

    public void setDescription(String description) {
        mDescription.setText(description);
    }
}
