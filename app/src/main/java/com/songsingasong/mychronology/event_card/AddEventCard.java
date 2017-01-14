package com.songsingasong.mychronology.event_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songsingasong.mychronology.R;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 12. 2..
 */
public class AddEventCard {
    private static final String CLASS_NAME = "AddEventCard";
    private BaseEventCard mEventCard;

    private ViewGroup mButtonLayout;

    public interface OnButtonClickListener {
        void onSubmit(SDate date, String title, ArrayList<Tag> tags, String description);
        void onCancel();
    }

    public AddEventCard(Context context, ViewGroup rootLayout) {
        mEventCard = new BaseEventCard(context, rootLayout);
        initReferences(context);
        mEventCard.setButtons(mButtonLayout);
    }

    public void showCard() {
        mEventCard.initViews();
        mEventCard.showCard();
        SLog.d(CLASS_NAME, "showCard", "");
    }

    public void hideCard() {
        mEventCard.hideCard();
        SLog.d(CLASS_NAME, "hideCard", "");
    }

    private void initReferences(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mButtonLayout = (ViewGroup) inflater.inflate(R.layout.add_event_card_buttons, null, false);
        View mSubmit = mButtonLayout.findViewById(R.id.btn_add_event_submit);
        View mCancel = mButtonLayout.findViewById(R.id.btn_add_event_cancel);

        mSubmit.setOnClickListener(mSubmitClickListener);
        mCancel.setOnClickListener(mSubmitClickListener);
    }

    private OnButtonClickListener mButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mButtonClickListener = listener;
    }

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_add_event_submit) {
                // Submit
                if (mButtonClickListener != null) {
                    SDate date = new SDate(mEventCard.getYear(), mEventCard.getMonth(), mEventCard.getDay());
                    mButtonClickListener.onSubmit(date, mEventCard.getTitle(),
                                                  Tag.stringToList(mEventCard.getTags()), mEventCard.getDescription());
                }
            } else if (id == R.id.btn_add_event_cancel) {
                if (mButtonClickListener != null) {
                    mButtonClickListener.onCancel();
                }
            }
        }
    };

}
