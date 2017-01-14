package com.songsingasong.mychronology.utils;

import com.songsingasong.mychronology.SApp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jaewoosong on 2015. 11. 28..
 */
public class SDate {
    private static final String CLASS_NAME = "SDate";

    public static final String NONE = "None";
    private static final int DONT_KNOW = 987654321;
//    public static final String RANGE = "range";

    private int mYear = DONT_KNOW;
    private int mMonth = DONT_KNOW;
    private int mDay = DONT_KNOW;

    public SDate(int year, int month, int day) {
        mYear = year != 0 ? year : DONT_KNOW;
        mMonth = month != 0 ? month : DONT_KNOW;
        mDay = day != 0 ? day : DONT_KNOW;
    }

    public void setYear(int year) {
        mYear = year != 0 ? year : DONT_KNOW;
    }

    public void setMonth(int month) {
        mMonth = month != 0 ? month : DONT_KNOW;
    }

    public void setDay(int day) {
        mDay = day != 0 ? day : DONT_KNOW;
    }

    public int getYear() {
        return mYear == DONT_KNOW ? 0 : mYear;
    }

    public int getMonth() {
        return mMonth == DONT_KNOW ? 0 : mMonth;
    }

    public int getDay() {
        return mDay == DONT_KNOW ? 0 : mDay;
    }

    private static final String DATE_FORMAT_KO_DAY = "yyyy. MM. dd.";
    private static final String DATE_FORMAT_KO_MONTH = "yyyy. MM.";
    private static final String DATE_FORMAT_KO_YEAR = "yyyy.";
    private static final String DATE_FORMAT_US_DAY = "MMM dd. yyyy";
    private static final String DATE_FORMAT_US_MONTH = "MMM. yyyy";
    private static final String DATE_FORMAT_US_YEAR = "yyyy.";

    @Override
    public String toString() {
        if (mYear == DONT_KNOW) {
            return "";
        }

        Locale locale = SApp.getContext().getResources().getConfiguration().locale;
        boolean isKorean = locale.equals(Locale.KOREA);

        DateFormat format;
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, mYear);

        if (mMonth != DONT_KNOW) {
            c.set(Calendar.MONTH, mMonth - 1);

            if (mDay != DONT_KNOW) {
                c.set(Calendar.DAY_OF_MONTH, mDay);
                format = new SimpleDateFormat(isKorean ? DATE_FORMAT_KO_DAY : DATE_FORMAT_US_DAY, locale);
            } else {
                format = new SimpleDateFormat(isKorean ? DATE_FORMAT_KO_MONTH : DATE_FORMAT_US_MONTH, locale);
            }
        } else {
            format = new SimpleDateFormat(isKorean ? DATE_FORMAT_KO_YEAR : DATE_FORMAT_US_YEAR, locale);
        }

        return format.format(c.getTime());
    }

    public int compareTo(SDate dest) {
        // return src - dest

        if (dest == null) {
            return 1;
        }

        int[] srcDate = { getYear(), getMonth(), getDay() };
        int[] dstDate = { dest.getYear(), dest.getMonth(), dest.getDay() };

        int cmp;
        int i = 0;
        while ((cmp = compare(srcDate[i], dstDate[i])) == KNOW_BOTH) {
            int srcNum = srcDate[i];
            int dstNum = dstDate[i];

            if (srcNum != dstNum || i == 2) {
                return srcNum - dstNum;
            }

            i++;
        }

        return cmp;
    }

    private static final int KNOW_BOTH = 2;
    private static final int KNOW_SRC = -1;
    private static final int KNOW_DST = 1;
    private static final int DONT_KNOW_BOTH = 0;

    private static int compare(int src, int dst) {
        if (src == DONT_KNOW && dst == DONT_KNOW) {
            return DONT_KNOW_BOTH;
        }

        if (src == DONT_KNOW) {
            return KNOW_DST;
        }

        if (dst == DONT_KNOW) {
            return KNOW_SRC;
        }

        return KNOW_BOTH;
    }

    private final static String DATE_FORMAT = "dd-MM-yyyy";

    public static boolean isDateValid(int year, int month, int day) {
        String date = day + "-" + month + "-" + year;
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
