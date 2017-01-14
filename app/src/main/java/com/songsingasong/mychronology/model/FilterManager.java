package com.songsingasong.mychronology.model;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 11. 23..
 */
public class FilterManager {
    private static final String CLASS_NAME = "FilterManager";
    private float mImportance;

    private static FilterManager mInstance;

    private FilterManager(){}

    public static FilterManager getInstance() {
        if (mInstance == null) {
            mInstance = new FilterManager();
        }

        return mInstance;
    }

    public boolean isFiltered() {
        return mImportance > 0;
    }

    public void setImportance(float importance) {
        mImportance = importance;
        callListeners();
    }

    public float getImportance() {
        return mImportance;
    }

    private ArrayList<OnFilterChangeListener> mListeners = new ArrayList<>();

    private void callListeners() {
        for (OnFilterChangeListener listener : mListeners) {
            listener.onFilterChange(isFiltered());
        }
    }

    public void registerOnFilterChangeListener(OnFilterChangeListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void deregisterOnFilterChangeListener(OnFilterChangeListener listener) {
        mListeners.remove(listener);
    }

    public interface OnFilterChangeListener {
        void onFilterChange(boolean filterOn);
    }
}
