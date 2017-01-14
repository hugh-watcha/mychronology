package com.songsingasong.mychronology.model.tag;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by jaewoosong on 2015. 11. 23..
 */
public class Tag {
    private static final String CLASS_NAME = "Tag";
    public static final char DELIMITER = ',';

    private String mName;
    private int mColor;

    public Tag(String name) {
        mName = name;
        mColor = generateColor(name);
    }

    private int generateColor(String seed) {
        if (seed == null) {
            return 0x000000;
        }
        return seed.hashCode() | 0xFF000000;
    }

    public String getName() {
        return mName;
    }

    public int getColor() {
        return mColor;
    }

    public boolean equalTo(Tag tag) {
        return (tag != null) && (getName().equals(tag.getName()));
    }

    public boolean equalTo(String name) {
        return getName().equals(name);
    }

    @Override
    public String toString() {
        return mName;
    }

    public static String convertListToString(ArrayList<Tag> tags) {
        if (tags == null || tags.size() == 0) {
            return "";
        }

        String str = "";
        for (Tag tag : tags) {
            str += tag.getName() + Tag.DELIMITER;
        }
        return str.substring(0, str.lastIndexOf(Tag.DELIMITER));
    }

    public static ArrayList<Tag> stringToList(String tags) {
        if (tags == null) {
            return null;
        }
        ArrayList<Tag> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(tags, Tag.DELIMITER + "");
        while (st.hasMoreTokens()) {
            list.add(new Tag(st.nextToken()));
        }

        return list;
    }
}
