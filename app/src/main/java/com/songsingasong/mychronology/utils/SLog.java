package com.songsingasong.mychronology.utils;

import android.util.Log;

/**
 * Created by jaewoosong on 2015. 10. 28..
 */
public class SLog {
    private static final String TAG = "MyChronology";
    private static final boolean DEBUG = true;

    public static void v(String className, String methodName, String message) {
        if (DEBUG) {
            Log.v(TAG, format(className, methodName, message));
        }
    }

    public static void d(String className, String methodName, String message) {
        if (DEBUG) {
            Log.d(TAG, format(className, methodName, message));
        }
    }

    public static void i(String className, String methodName, String message) {
        if (DEBUG) {
            Log.i(TAG, format(className, methodName, message));
        }
    }

    public static void w(String className, String methodName, String message) {
        if (DEBUG) {
            Log.w(TAG, format(className, methodName, message));
        }
    }

    public static void e(String className, String methodName, String message) {
        if (DEBUG) {
            Log.e(TAG, format(className, methodName, message));
        }
    }

    public static void e(String className, String methodName, String message, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, format(className, methodName, message), e);
        }
    }

    private static String format(String className, String methodName, String message) {
        return "[" + className + "|" + methodName + "] " + message;
    }
}
