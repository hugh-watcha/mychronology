package com.songsingasong.mychronology.event_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songsingasong.mychronology.R;
import com.songsingasong.mychronology.model.event.Event;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 12. 2..
 */
public class EditEventCard {
    private static final String CLASS_NAME = "EditEventCard";
    private BaseEventCard mEventCard;

    private ViewGroup mButtonLayout;

    private long mEventId;

    public interface OnButtonClickListener {
        void onSubmit(long id, SDate date, String title, ArrayList<Tag> tags, String description);
        void onCancel();
        void onRemove(long id);
    }

    public EditEventCard(Context context, ViewGroup rootLayout) {
        mEventCard = new BaseEventCard(context, rootLayout);
        initReferences(context);
        mEventCard.setButtons(mButtonLayout);
    }

    public void showCard(Event event) {
        mEventId = event.getId();
        mEventCard.initViews(event.getDate().getYear(), event.getDate().getMonth(), event.getDate().getDay(), event.getTitle(), Tag.convertListToString(event.getTagList()), event.getDescription());
        mEventCard.showCard();
        SLog.d(CLASS_NAME, "showCard", "");
    }

    public void hideCard() {
        mEventCard.hideCard();
        mEventId = -1;
        SLog.d(CLASS_NAME, "hideCard", "");
    }

    private void initReferences(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mButtonLayout = (ViewGroup) inflater.inflate(R.layout.edit_event_card_button, null, false);
        View submit = mButtonLayout.findViewById(R.id.btn_edit_event_submit);
        View cancel = mButtonLayout.findViewById(R.id.btn_edit_event_cancel);
        View remove = mButtonLayout.findViewById(R.id.btn_edit_event_remove);

        submit.setOnClickListener(mSubmitClickListener);
        cancel.setOnClickListener(mSubmitClickListener);
        remove.setOnClickListener(mSubmitClickListener);
    }

    private OnButtonClickListener mButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mButtonClickListener = listener;
    }

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_edit_event_submit) {
                // Submit
                if (mButtonClickListener != null) {
                    SDate date = new SDate(mEventCard.getYear(), mEventCard.getMonth(), mEventCard.getDay());
                    mButtonClickListener.onSubmit(mEventId, date, mEventCard.getTitle(),
                                                  Tag.stringToList(mEventCard.getTags()), mEventCard.getDescription());
                }
            } else if (id == R.id.btn_edit_event_cancel) {
                if (mButtonClickListener != null) {
                    mButtonClickListener.onCancel();
                }
            } else if (id == R.id.btn_edit_event_remove) {
                if (mButtonClickListener != null) {
                    mButtonClickListener.onRemove(mEventId);
                }
            }
        }
    };
}
