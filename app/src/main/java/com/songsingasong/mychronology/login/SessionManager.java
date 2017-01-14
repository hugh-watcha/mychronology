package com.songsingasong.mychronology.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.GraphResponse;
import com.songsingasong.mychronology.model.event.EventManager;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by jaewoosong on 2015. 12. 8..
 */
public class SessionManager {
    private static final String CLASS_NAME = "SessionManager";
    private static final String NO_ID = "0";
    public static final String[] READ_PERMISSIONS = { "user_posts", "user_birthday" };

    public interface OnSessionListener {
        void onCurrentUserChanged(String userId);
    }

    private static SessionManager mInstance;

    private String mUserId = NO_ID;

    private ArrayList<OnSessionListener> mSessionChangedListeners = new ArrayList<>();

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (mInstance == null) {
            mInstance = new SessionManager();
        }

        return mInstance;
    }

    public void init(Context context) {
        FacebookManager.getInstance().init(context, new OnUserChangeListener() {
            @Override
            public void onUserChanged(String userId) {
                setCurrentUserId(userId);
            }
        }, new FacebookManager.FacebookListener() {
            @Override
            public void onResponse(GraphResponse response) {
                onFacebookResponse(response);
            }
        });

        SLog.d(CLASS_NAME, "init",
               "fb curr id : " + FacebookManager.getInstance().getCurrentUserId());
        setCurrentUserId(FacebookManager.getInstance().getCurrentUserId());
    }

    public String getUserId() {
        SLog.d(CLASS_NAME, "getUserId", "userId : " + mUserId);
        return mUserId;
    }

    public void setCurrentUserId(String userId) {
        SLog.d(CLASS_NAME, "setCurrentUserId", "userId : " + userId);
        mUserId = userId;

        onCurrentUserChanged(userId);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    public void registerSessionChangedListener(OnSessionListener listener) {
        if (!mSessionChangedListeners.contains(listener)) {
            mSessionChangedListeners.add(listener);
        }
    }

    public void deregisterSessionChangedListener(OnSessionListener listener) {
        mSessionChangedListeners.remove(listener);
    }

    private void onCurrentUserChanged(String userId) {
        for (OnSessionListener listener : mSessionChangedListeners) {
            listener.onCurrentUserChanged(userId);
        }
    }

    private void onFacebookResponse(GraphResponse response) {
        parseFacebookResponse(response);
    }

    public void getFbPosts() {
        FacebookManager.getInstance().getFbPosts("/me/feed", null);
    }

    public void getFbPosts(Bundle param) {
        FacebookManager.getInstance().getFbPosts("/me/feed", param);
    }

    private void parseFacebookResponse(GraphResponse response) {
        JSONObject json = response.getJSONObject();
        if (json == null) {
            return;
        }
        JSONArray jarray = null;

        try {
            jarray = json.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        parseNextPage(json);

        if (jarray == null) {
            return;
        }

        SLog.d(CLASS_NAME, "parseFacebookResponse",
               "length : " + jarray.length() + " array : " + jarray.toString());
        for(int i = 0; i < jarray.length(); i++){

            JSONObject event;
            try {
                event = jarray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            //get your values
            jsonToEvent(event);
        }


    }

    private String getStringFromJSON(JSONObject object, String key) {
        String ret;
        try {
            ret = object.getString(key);
        } catch (JSONException e) {
            ret = null;
//            e.printStackTrace();
        }

        return ret;
    }

    private void parseNextPage(JSONObject json) {
        try {
            JSONObject paging = json.getJSONObject("paging");
            String next = paging.getString("next");
            SLog.d(CLASS_NAME, "parseNextPage", "next : " + next);

            next = next.substring(next.indexOf("/feed"));
            StringTokenizer tk = new StringTokenizer(next, "&");

            String pagingToken = null;
            String until = null;

            while (tk.hasMoreTokens()) {
                String str = tk.nextToken();
                if (str.startsWith("__paging_token=")) {
                    pagingToken = str.replace("__paging_token=", "");
                } else if (str.startsWith("until=")) {
                    until = str.replace("until=", "");
                }
            }

            Bundle param = new Bundle();
            param.putString("__paging_token", pagingToken);
            param.putString("until", until);

            SLog.d(CLASS_NAME, "parseNextPage", "pt : " + pagingToken + ", until : " + until);

            getFbPosts(param);
        } catch (JSONException e) {
            SLog.d(CLASS_NAME, "parseNextPage", "no paging");
        }
    }

    private void jsonToEvent(JSONObject json) {
        String dateStr = getStringFromJSON(json, "created_time");
        String message = getStringFromJSON(json, "message");
        String story = getStringFromJSON(json, "story");
        String picture = getStringFromJSON(json, "picture");
        String link = getStringFromJSON(json, "link");

        SLog.d(CLASS_NAME, "jsonToEvent",
               "message : " + message + ", story : " + story + ", time : " + dateStr);

        if (message == null && story == null) {
            return;
        }

        Date d = new Date(Long.parseLong(dateStr) * 1000L);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        SDate date = new SDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));

        String title;
        if (story == null) {
            int brIndex = message.indexOf('\n');
            if (brIndex > 0) {
                title = message.substring(0, brIndex);
            } else {
                title = message;
            }
        } else {
            title = story;
        }

        String description;
        if (message != null) {
            description = message;
        } else {
            description = story;
        }

        ArrayList<Uri> pictures = new ArrayList<>();

        if (picture != null) {
            pictures.add(Uri.parse(picture));
        }

        EventManager.getInstance().addEvent(getUserId(), date, title, new ArrayList<Tag>(), description, 3.0f,
                                            pictures);
    }
}
