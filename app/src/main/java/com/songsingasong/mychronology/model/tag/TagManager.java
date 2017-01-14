package com.songsingasong.mychronology.model.tag;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 11. 23..
 */
public class TagManager {
    private static final String CLASS_NAME = "TagManager";
    private ArrayList<Tag> mTagList = new ArrayList<>();

    private static TagManager mInstance;

    private TagManager() {}

    public static TagManager getInstance() {
        if (mInstance == null) {
            mInstance = new TagManager();
        }

        return mInstance;
    }

    public Tag addTag(String tagName) {
        for (Tag tag : mTagList) {
            if (tag.equalTo(tagName)) {
                return tag;
            }
        }

        Tag newTag = new Tag(tagName);
        mTagList.add(newTag);
        return newTag;
    }
}
