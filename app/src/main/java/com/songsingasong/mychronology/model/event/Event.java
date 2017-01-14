package com.songsingasong.mychronology.model.event;

import android.net.Uri;

import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.SDate;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 11. 23..
 */
public class Event {
    private static final String CLASS_NAME = "Event";
    public static final int NO_ID = -1;

    private long mId = NO_ID;
    private String mTitle;
    private SDate mDate;
    private float mImportance;
    private ArrayList<Tag> mTags;
    private ArrayList<Uri> mPictures;
    private String mDescription;

//    public Event() {
//        this("", new Date(), new ArrayList<Tag>(), new ArrayList<Uri>(), "", new ArrayList<String>());
//    }
//
//    public Event(String title) {
//        this(title, new Date(), new ArrayList<Tag>(), new ArrayList<Uri>(),"", new ArrayList<String>());
//    }

    public Event(long id, SDate date, String title, ArrayList<Tag> tags, String description, float importance, ArrayList<Uri> pictures) {
        this(date, title, tags, description, importance, pictures);
        mId = id;
    }

    public Event(int id, SDate date, String title, String tags, String description, float importance, String pictures) {
        this(date, title, tags, description, importance, pictures);
        mId = id;
    }

    public Event(SDate date, String title, ArrayList<Tag> tags, String description, float importance, ArrayList<Uri> pictures) {
        setDateImpl(date);
        setTitleImpl(title);
        setImportanceImpl(importance);
        setTagsImpl(tags);
        setPicturesImpl(pictures);
        setDescriptionImpl(description);
    }

    public Event(SDate date, String title, String tags, String description, float importance, String pictures) {
        this(date, title, Tag.stringToList(tags), description, importance, parsePictureString(pictures));
    }

    public void setImportanceImpl(float importance) {
        mImportance = importance;
    }

    public void setTitleImpl(String title) {
        mTitle = title;
    }

    public void setDateImpl(SDate date) {
        mDate = date;
    }

    public void setTagsImpl(ArrayList<Tag> tags) {
        mTags = tags;
    }

    public void setPicturesImpl(ArrayList<Uri> pictures) {
        mPictures = pictures;
    }

    public void setDescriptionImpl(String description) {
        mDescription = description;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public SDate getDate() {
        return mDate;
    }

    public float getImportance() {
        return mImportance;
    }

    public ArrayList<Tag> getTagList() {
        return mTags;
    }

    public static ArrayList<Uri> parsePictureString(String pictures) {
        return new ArrayList<>();
    }

    public ArrayList<Uri> getPicturesList() {
        return mPictures;
    }

    public String getPicturesAsString() {
        if (mPictures == null || mPictures.size() == 0) {
            return "";
        }

        String pictures = "";
//        for (Uri picture : mPictures) {
//            pictures += picture.toString() +
//        }
        return "";
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return "Event[title : " + getTitle() + ", date : " + getDate() + ", importance : " + getImportance() + ", tags : " + getTagList() + ", pictures : " + getPicturesList() + ", description : " + getDescription() + "]";
    }
}
