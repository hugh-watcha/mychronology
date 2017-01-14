package com.songsingasong.mychronology.model.event;

import android.net.Uri;

import com.songsingasong.mychronology.SApp;
import com.songsingasong.mychronology.model.FilterManager;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.model.db.DBHandler;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jaewoosong on 2015. 11. 23..
 */
public class EventManager {
    private static final String CLASS_NAME = "EventManager";
    private ArrayList<Event> mEventList = new ArrayList<>();
    private ArrayList<Event> mFilteredList = new ArrayList<>();

    private static EventManager mInstance;

    private DBHandler mDbHandler;

    private FilterManager.OnFilterChangeListener mOnFilterChangeListener = new FilterManager.OnFilterChangeListener() {
        @Override
        public void onFilterChange(boolean filterOn) {
            applyFilter();
        }
    };

    private EventManager() {
        mDbHandler = DBHandler.getInstance(SApp.getContext());
        FilterManager.getInstance().registerOnFilterChangeListener(mOnFilterChangeListener);
    }

    public static EventManager getInstance() {
        if (mInstance == null) {
            mInstance = new EventManager();
        }
        return mInstance;
    }

    public void changeUserId(String userId) {
        SLog.d(CLASS_NAME, "changeUserId", "userId : " + userId);
        setEventList(mDbHandler.getAllEvents(userId));
        sortList();

        callListeners();
    }

    private void setEventList(ArrayList<Event> list) {
        mEventList = list;
    }

    public ArrayList<Event> getEventList() {
        if (FilterManager.getInstance().isFiltered()) {
            return mFilteredList;
        }
        return mEventList;
    }

    public void applyFilter() {
        SLog.d(CLASS_NAME, "applyFilter", "");
        if (FilterManager.getInstance().isFiltered()) {
            mFilteredList.clear();
            for (Event event : mEventList) {
                if (isInFilter(event)) {
                    mFilteredList.add(event);
                }
            }

            callListeners();
        }
    }

    public void addEvent(String userId, SDate date, String title, ArrayList<Tag> tags, String description, float importance, ArrayList<Uri> pictures) {
        Event event = new Event(date, title, tags, description, importance, pictures);
        mEventList.add(event);

        SLog.d(CLASS_NAME, "addEvent", "event : " + event);

        if (FilterManager.getInstance().isFiltered()) {
            if (isInFilter(event)) {
                mFilteredList.add(event);
            }
        }
        if (mDbHandler != null) {
            mDbHandler.addEvent(userId, event);
        }
        sortList();

        callListeners();
    }

    public void removeEvent(long id) {
        Event event = getEvent(id);

        SLog.d(CLASS_NAME, "removeEvent", "event : " + event);

        mEventList.remove(event);
        mFilteredList.remove(event);

        if (mDbHandler != null) {
            mDbHandler.deleteEvent(event);
        }
        callListeners();
    }

    public void updateEvent(String userId, long id, SDate date, String title, ArrayList<Tag> tags, String description, float importance, ArrayList<Uri> pictures) {
        Event event = getEvent(id);

        SLog.d(CLASS_NAME, "updateEvent", "event : " + event);

        if (event == null) {
            return;
        }

        event.setDateImpl(date);
        event.setTitleImpl(title);
        event.setTagsImpl(tags);
        event.setDescriptionImpl(description);
        event.setImportanceImpl(importance);
        event.setPicturesImpl(pictures);

        if (mDbHandler != null) {
            mDbHandler.updateEvent(userId, event);
        }

        sortList();
        callListeners();
    }

    private Event getEvent(long id) {
        for (Event event : mEventList) {
            if (event.getId() == id) {
                return event;
            }
        }

        return null;
    }

    private void sortList() {
        Collections.sort(mEventList, DESC_DATE);

        if (FilterManager.getInstance().isFiltered()) {
            Collections.sort(mFilteredList, DESC_DATE);
        }
    }

    private boolean isInFilter(Event event) {
        return event.getImportance() >= FilterManager.getInstance().getImportance();
    }

    private static Comparator<Event> DESC_DATE = new Comparator<Event>() {
        @Override
        public int compare(Event lhs, Event rhs) {
            return rhs.getDate().compareTo(lhs.getDate());
        }
    };

    private ArrayList<OnEventListChangeListener> mListeners = new ArrayList<>();

    private void callListeners() {
        for (OnEventListChangeListener listener : mListeners) {
            listener.onEventListChange();
        }
    }

    public void registerOnEventListChangeListener(OnEventListChangeListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void deregisterOnEventListChangeListener(OnEventListChangeListener listener) {
        mListeners.remove(listener);
    }

    public interface OnEventListChangeListener {
        void onEventListChange();
    }
}
