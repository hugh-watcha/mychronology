package com.songsingasong.mychronology;

import android.app.Application;
import android.content.Context;

/**
 * Created by jaewoosong on 2015. 11. 28..
 */
public class SApp extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
