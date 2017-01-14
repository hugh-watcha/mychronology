package com.songsingasong.mychronology.login;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginBehavior;
import com.facebook.login.widget.LoginButton;
import com.songsingasong.mychronology.R;

/**
 * Created by jaewoosong on 2015. 12. 7..
 */
public class LogInPage {
    private static final String CLASS_NAME = "LogInPage";

    private ViewGroup mRoot;

    public LogInPage() {
    }

    public void init(ViewGroup logInCard) {
        mRoot = logInCard;
        makeLogInButton(logInCard);
    }

    private void makeLogInButton(ViewGroup parent) {
        LoginButton button = (LoginButton) parent.findViewById(R.id.login_button);
        button.setReadPermissions(SessionManager.READ_PERMISSIONS);
        button.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        Button close = (Button) parent.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLogInCard();
            }
        });

        Button getposts = (Button) parent.findViewById(R.id.test_get_facebook);
        getposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jjajan();
            }
        });

////         Callback registration
//        button.registerCallback(FacebookManager.getInstance().getCallbackManager(), new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                if (loginResult == null) {
//                    SLog.d(CLASS_NAME, "onSuccess", "result : null");
//                } else {
//                    SLog.d(CLASS_NAME, "onSuccess", "result : " + loginResult.getAccessToken());
//                    setAccessToken(loginResult.getAccessToken());
//                }
//            }
//
//            @Override
//            public void onCancel() {
//                SLog.d(CLASS_NAME, "onCancel", "");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                SLog.e(CLASS_NAME, "onError", "", exception);
//            }
//        });
    }

    private void jjajan() {
        SessionManager.getInstance().getFbPosts();
    }

    public void closeLogInCard() {
        if (mRoot != null) {
            mRoot.setVisibility(View.GONE);
        }
    }
}
