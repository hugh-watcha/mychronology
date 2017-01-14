package com.songsingasong.mychronology.utils;

import com.songsingasong.mychronology.SApp;

import java.util.Locale;

/**
 * Created by jaewoosong on 15. 12. 17..
 */
public class LocaleString {
    private String mEnglish;
    private String mKorean;
    private String mChinese;

    public LocaleString(String english, String korean, String chinese) {
        mEnglish = english == null ? "" : english;
        mKorean = korean == null ? "" : korean;
        mChinese = chinese == null ? "" : chinese;
    }

    @Override
    public String toString() {
        Locale locale = SApp.getContext().getResources().getConfiguration().locale;
        return get(locale);
    }

    public String get(Locale locale) {
        if (locale.equals(Locale.KOREA)) {
            return mKorean;
        } else if (locale.equals(Locale.CHINA)) {
            return mChinese;
        } else {
            return mEnglish;
        }
    }
}
