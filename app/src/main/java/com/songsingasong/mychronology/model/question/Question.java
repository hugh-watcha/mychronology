package com.songsingasong.mychronology.model.question;

import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.LocaleString;
import com.songsingasong.mychronology.utils.SDate;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 15. 12. 17..
 */
public class Question {
    private static final String CLASS_NAME = "Question";
    private long mId;

    private LocaleString mQuestion;
    private String mRequestSections;
    private ArrayList<Tag> mGivenTags;
    private LocaleString mGivenTitle, mGivenDescription;
    private SDate mGivenDate;
    private float mGivenImportance;

    public static class Builder {
        private final LocaleString question;
        private final String requestSection;

        private ArrayList<Tag> mGivenTags = null;
        private LocaleString mGivenTitle = null, mGivenDescription = null;
        private SDate mGivenDate = null;
        private float mGivenImportance = 0f;

        public Builder(LocaleString question, String requestSection) {
            this.question = question;
            this.requestSection = requestSection;
        }

        public Builder tags(ArrayList<Tag> tags) {
            mGivenTags = tags;
            return this;
        }

        public Builder title(LocaleString title) {
            mGivenTitle = title;
            return this;
        }

        public Builder description(LocaleString description) {
            mGivenDescription = description;
            return this;
        }

        public Builder date(SDate date) {
            mGivenDate = date;
            return this;
        }

        public Builder importance(float importance) {
            mGivenImportance = importance;
            return this;
        }

        public Question build() {
            return new Question(this);
        }
    }

    private Question(Builder builder) {
        mQuestion = builder.question;
        mRequestSections = builder.requestSection;
        mGivenTags = builder.mGivenTags;
        mGivenTitle = builder.mGivenTitle;
        mGivenDescription = builder.mGivenDescription;
        mGivenDate = builder.mGivenDate;
        mGivenImportance = builder.mGivenImportance;
    }

    public LocaleString getQuestion() {
        return mQuestion;
    }

    public String getRequestSections() {
        return mRequestSections;
    }

    public ArrayList<Tag> getGivenTags() {
        return mGivenTags;
    }

    public LocaleString getGivenTitle() {
        return mGivenTitle;
    }

    public LocaleString getGivenDescription() {
        return mGivenDescription;
    }

    public SDate getGivenDate() {
        return mGivenDate;
    }

    public float getGivenImportance() {
        return mGivenImportance;
    }

    @Override
    public String toString() {
        return "Question(q:" + mQuestion + ", rqst:" + mRequestSections + ", title:" + mGivenTitle + ", des:" + mGivenDescription + ", date:" + mGivenDate + ", imp:" + mGivenImportance + ", tag:" + listToString(mGivenTags);
    }

    private String listToString(ArrayList<Tag> list) {
        if (list == null) {
            return "";
        }

        String ret = "";
        for (Tag tag : list) {
            ret += tag + " ";
        }

        return ret;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }
}
