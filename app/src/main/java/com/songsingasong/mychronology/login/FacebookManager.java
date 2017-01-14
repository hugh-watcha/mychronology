package com.songsingasong.mychronology.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.songsingasong.mychronology.utils.SLog;

/**
 * Created by jaewoosong on 2015. 12. 8..
 */
class FacebookManager {
    private static final String CLASS_NAME = "FacebookManager";

    private static FacebookManager mInstance;

    private CallbackManager mCallbackManager;

    private OnUserChangeListener mOnUserChangeListener;

    private FacebookListener mFacebookListener;

    public interface FacebookListener {
        void onResponse(GraphResponse response);
    }


    private FacebookManager() {}
    //id : 952878081439442

    public static FacebookManager getInstance() {
        if (mInstance == null) {
            mInstance = new FacebookManager();
        }

        return mInstance;
    }

    public void init(Context context, OnUserChangeListener userChangeListener, FacebookListener listener) {
        mOnUserChangeListener = userChangeListener;

        FacebookSdk.sdkInitialize(context);
        mCallbackManager = CallbackManager.Factory.create();
        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                SLog.d(CLASS_NAME, "onCurrentAccessTokenChanged", "current : " + currentAccessToken);
                setAccessToken(currentAccessToken);
            }
        };
        mFacebookListener = listener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setAccessToken(AccessToken accessToken) {
        String userId = accessToken != null ? accessToken.getUserId() : "0";
        SLog.d(CLASS_NAME, "setAccessToken", "id : " + userId);
        mOnUserChangeListener.onUserChanged(userId);
        AccessToken.setCurrentAccessToken(accessToken);
    }

    public String getCurrentUserId() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null ? token.getUserId() : "0";
    }

    public void getFbPosts(String requestUrl, @Nullable Bundle param) {
        if (param == null) {
            param = new Bundle();
        }
        param.putString("fields", "id,message,created_time,story,picture,link");
        param.putString("date_format", "U");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                requestUrl,
                param,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        SLog.d(CLASS_NAME, "GraphRequest.Callback$onCompleted", "response : " + response);
                        if (mFacebookListener != null) {
                            mFacebookListener.onResponse(response);
                        }
                    }
                }
        ).executeAsync();
    }
}
