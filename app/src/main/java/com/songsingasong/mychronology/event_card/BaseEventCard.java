package com.songsingasong.mychronology.event_card;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.songsingasong.mychronology.R;
import com.songsingasong.mychronology.SApp;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by jaewoosong on 2015. 12. 2..
 */
final class BaseEventCard implements AdapterView.OnItemSelectedListener {
    private static final String CLASS_NAME = "BaseEventCard";
    private static final int SELECTABLE_YEARS = 100;

    private ViewGroup mRootView;
    private ViewGroup mEventCardView;

    private ViewGroup mDateLayout;
    private Spinner mYearSpinner;
    private Spinner mMonthSpinner;
    private Spinner mDaySpinner;

    private ViewGroup mTitleLayout;
    private EditText mTitle;

    private ViewGroup mTagLayout;
    private EditText mTags;

    private ViewGroup mDescriptionLayout;
    private EditText mDescription;

    private DateSpinnerAdapter mYearAdapter;
    private DateSpinnerAdapter mMonthAdapter;
    private DateSpinnerAdapter mDayAdapter;

    private static final ArrayList<String> DAYS_28 = new ArrayList<>();
    private static final ArrayList<String> DAYS_29 = new ArrayList<>();
    private static final ArrayList<String> DAYS_30 = new ArrayList<>();
    private static final ArrayList<String> DAYS_31 = new ArrayList<>();

    static {
        DAYS_28.add(SDate.NONE);
        DAYS_29.add(SDate.NONE);
        DAYS_30.add(SDate.NONE);
        DAYS_31.add(SDate.NONE);
        for (int i = 1; i <= 28; i++) {
            DAYS_28.add("" + i);
            DAYS_29.add("" + i);
            DAYS_30.add("" + i);
            DAYS_31.add("" + i);
        }
        DAYS_29.add("29");
        DAYS_30.add("29");
        DAYS_31.add("29");
        DAYS_30.add("30");
        DAYS_31.add("30");
        DAYS_31.add("31");
    }

    public BaseEventCard(Context context, ViewGroup rootLayout) {
        initReferences(context, rootLayout);
        initSpinners(context);
    }

    private static final long ANIM_DURATION = 200;
    private static final float FROM_ALPHA = 0.0f;
    private static final float FROM_Y = 200f;

    public boolean showCard() {
        if (mRootView.indexOfChild(mEventCardView) != -1) {
            SLog.d(CLASS_NAME, "showCard", "it's on already");
            return false;
        }
        mRootView.addView(mEventCardView);
        mEventCardView.setAlpha(FROM_ALPHA);
        mEventCardView.setTranslationY(FROM_Y);
        mEventCardView.animate().alpha(1.0f).translationY(0f).setDuration(ANIM_DURATION)
                      .setInterpolator(new DecelerateInterpolator()).setListener(null);
        SLog.d(CLASS_NAME, "showCard", "");
        return true;
    }

    public void hideCard() {
        hideKeyboard();
        mEventCardView.animate().alpha(FROM_ALPHA).translationY(FROM_Y).setDuration(
                ANIM_DURATION).setInterpolator(new AccelerateInterpolator())
                      .setListener(new Animator.AnimatorListener() {
                          @Override
                          public void onAnimationStart(Animator animation) {

                          }

                          @Override
                          public void onAnimationEnd(Animator animation) {
                              mRootView.removeView(mEventCardView);
                          }

                          @Override
                          public void onAnimationCancel(Animator animation) {

                          }

                          @Override
                          public void onAnimationRepeat(Animator animation) {

                          }
                      });
        SLog.d(CLASS_NAME, "hideCard", "");
    }

    private void initReferences(Context context, ViewGroup rootLayout) {
        mRootView = rootLayout;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mEventCardView = (ViewGroup) inflater.inflate(R.layout.event_card, rootLayout, false);
        mEventCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mDateLayout = (ViewGroup) mEventCardView.findViewById(R.id.event_card_date);
        mYearSpinner = (Spinner) mEventCardView.findViewById(R.id.spinner_add_event_year);
        mMonthSpinner = (Spinner) mEventCardView.findViewById(R.id.spinner_add_event_month);
        mDaySpinner = (Spinner) mEventCardView.findViewById(R.id.spinner_add_event_day);

        mTitleLayout = (ViewGroup) mEventCardView.findViewById(R.id.event_card_title);
        mTitle = (EditText) mEventCardView.findViewById(R.id.et_add_event_title);

        mTagLayout = (ViewGroup) mEventCardView.findViewById(R.id.event_card_tag);
        mTags = (EditText) mEventCardView.findViewById(R.id.et_add_event_tags);

        mDescriptionLayout = (ViewGroup) mEventCardView.findViewById(R.id.event_card_description);
        mDescription = (EditText) mEventCardView.findViewById(R.id.et_add_event_description);
    }

    private void initSpinners(Context context) {
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int currYear = c.get(Calendar.YEAR);
        for (int i = 0; i < SELECTABLE_YEARS; i++) {
            years.add("" + (currYear - i));
        }

        String[] monthNames = DateFormatSymbols.getInstance(
                SApp.getContext().getResources().getConfiguration().locale).getShortMonths();

        months.add(SDate.NONE);
        Collections.addAll(months, monthNames);

        days.addAll(DAYS_31);

        mYearAdapter = new DateSpinnerAdapter(context, R.layout.date_spinner_item, years);
        mMonthAdapter = new DateSpinnerAdapter(context, R.layout.date_spinner_item, months);
        mDayAdapter = new DateSpinnerAdapter(context, R.layout.date_spinner_item, days);

        mYearSpinner.setAdapter(mYearAdapter);
        mMonthSpinner.setAdapter(mMonthAdapter);
        mDaySpinner.setAdapter(mDayAdapter);

        mYearSpinner.setOnItemSelectedListener(this);
        mMonthSpinner.setOnItemSelectedListener(this);
        mDaySpinner.setOnItemSelectedListener(this);
    }

    public void initViews() {
        //initialize views
        mYearSpinner.setSelection(0);
        mMonthSpinner.setSelection(0);
        mDaySpinner.setSelection(0);
        mDaySpinner.setEnabled(false);

        mTitle.setText("");
        mTags.setText("");
        mDescription.setText("");
    }

    public void initViews(int year, int month, int day, String title, String tags, String description) {
        int gap = Integer.parseInt(mYearAdapter.getItem(0)) - year;

        mYearSpinner.setSelection(gap);
        mMonthSpinner.setSelection(month);
        mDaySpinner.setSelection(day);
        mDaySpinner.setEnabled(day != 0);

        mTitle.setText(title);
        mTags.setText(tags);
        mDescription.setText(description);
    }

    public void addHeader(ViewGroup header) {
        getRootView().addView(header, 0);
    }

    public void applyRequestSection(String sections) {
        mDateLayout.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mTagLayout.setVisibility(View.GONE);
        mDescriptionLayout.setVisibility(View.GONE);

        StringTokenizer st = new StringTokenizer(sections, "|");
        String section;
        while (st.hasMoreTokens()) {
            section = st.nextToken();
            switch (section) {
                case "date": {
                    mDateLayout.setVisibility(View.VISIBLE);
                    break;
                }

                case "title": {
                    mTitleLayout.setVisibility(View.VISIBLE);
                    break;
                }

                case "tags": {
                    mTagLayout.setVisibility(View.VISIBLE);
                    break;
                }

                case "description": {
                    mDescriptionLayout.setVisibility(View.VISIBLE);
                    break;
                }

                default:
                    break;
            }
        }
    }

    public void setButtons(ViewGroup buttonLayout) {
        getRootView().addView(buttonLayout);
    }

    private ViewGroup getRootView() {
        return (ViewGroup) mEventCardView.findViewById(R.id.event_card_root);
    }

    public int getYear() {
        return Integer.parseInt(mYearAdapter.getItem(mYearSpinner.getSelectedItemPosition()));
    }

    public int getMonth() {
        return parseFromSpinner(mMonthSpinner);
    }

    public int getDay() {
        return parseFromSpinner(mDaySpinner);
    }

    private static int parseFromSpinner(Spinner spinner) {
        return spinner.getSelectedItemPosition();
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getTags() {
        return mTags.getText().toString();
    }

    public String getDescription() {
        return mDescription.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mYearSpinner) {
            int month = mMonthSpinner.getSelectedItemPosition();
            int day = mDaySpinner.getSelectedItemPosition();
            if (month != 0 && day != 0) {
                int year = Integer.valueOf(mYearAdapter.getItem(position));

                setDaysToSpinner(getMaximumDayOfTheMonth(position, month));

                if (!SDate.isDateValid(year, month, day)) {
                    mDaySpinner.setSelection(0);
                }
            }
        } else if (parent == mMonthSpinner) {
            if (position == 0) {
                mDaySpinner.setSelection(0);
                mDaySpinner.setEnabled(false);
            } else {
                mDaySpinner.setEnabled(true);

                setDaysToSpinner(getMaximumDayOfTheMonth(Integer.valueOf(mYearAdapter.getItem(mYearSpinner.getSelectedItemPosition())), position));

                if (mDaySpinner.getSelectedItemPosition() != 0) {
                    int year = Integer.valueOf(mYearAdapter.getItem(mYearSpinner.getSelectedItemPosition()));
                    int month = position;
                    int day = mDaySpinner.getSelectedItemPosition();
                    if (!SDate.isDateValid(year, month, day)) {
                        mDaySpinner.setSelection(0);
                    }
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getMaximumDayOfTheMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);

        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void setDaysToSpinner(int maximumDay) {
        switch (maximumDay) {
            case 28:
                mDayAdapter.clear();
                mDayAdapter.addAll(DAYS_28);
                break;
            case 29:
                mDayAdapter.clear();
                mDayAdapter.addAll(DAYS_29);
                break;
            case 30:
                mDayAdapter.clear();
                mDayAdapter.addAll(DAYS_30);
                break;
            case 31:
                mDayAdapter.clear();
                mDayAdapter.addAll(DAYS_31);
                break;
            default:
                break;
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) SApp.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (mTitle != null) {
            inputManager.hideSoftInputFromWindow(mTitle.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
