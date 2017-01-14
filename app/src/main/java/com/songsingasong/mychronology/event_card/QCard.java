package com.songsingasong.mychronology.event_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.songsingasong.mychronology.R;
import com.songsingasong.mychronology.model.question.Question;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 2015. 12. 16..
 */
public class QCard {
    private static final String CLASS_NAME = "QCard";
    private BaseEventCard mEventCard;

    private ViewGroup mQuestionLayout;
    private ViewGroup mButtonLayout;

    private Question mQuestion;

    public interface OnButtonClickListener {
        void onSubmit(SDate date, String title, ArrayList<Tag> tags, String description);
        void onCancel();
        void onAlreadyHave();
    }

    public QCard(Context context, ViewGroup rootLayout) {
        mEventCard = new BaseEventCard(context, rootLayout);
        initReferences(context);
        mEventCard.setButtons(mButtonLayout);
        mEventCard.addHeader(mQuestionLayout);
    }

    public void showCard(Question question) {
        mEventCard.initViews();
        setQuestion(question);

        mEventCard.showCard();
        SLog.d(CLASS_NAME, "showCard", "");
    }

    public void hideCard() {
        mEventCard.hideCard();
        SLog.d(CLASS_NAME, "hideCard", "");
    }

    private void setQuestion(Question question) {
        mQuestion = question;

        TextView q = (TextView) mQuestionLayout.findViewById(R.id.tv_q_card_question);
        q.setText(question.getQuestion().toString());

        String reqestSections = question.getRequestSections();
        applyRequestSections(reqestSections);
    }

    private void applyRequestSections(String requestSections) {
        mEventCard.applyRequestSection(requestSections);
    }

    private void initReferences(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mButtonLayout = (ViewGroup) inflater.inflate(R.layout.q_card_buttons, null, false);
        View mSubmit = mButtonLayout.findViewById(R.id.btn_q_card_submit);
        View mCancel = mButtonLayout.findViewById(R.id.btn_q_card_cancel);
        View mAlready = mButtonLayout.findViewById(R.id.btn_q_card_already);

        mSubmit.setOnClickListener(mSubmitClickListener);
        mCancel.setOnClickListener(mSubmitClickListener);
        mAlready.setOnClickListener(mSubmitClickListener);

        mQuestionLayout = (ViewGroup) inflater.inflate(R.layout.q_card_question, null, false);
    }

    private OnButtonClickListener mButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mButtonClickListener = listener;
    }

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_q_card_submit) {
                // Submit
                if (mButtonClickListener != null) {
                    SDate givenDate = mQuestion.getGivenDate();
                    SDate date = givenDate != null ? givenDate : new SDate(mEventCard.getYear(), mEventCard.getMonth(), mEventCard.getDay());

                    String givenTItle = mQuestion.getGivenTitle().toString();
                    String title = givenTItle.compareTo("null") != 0 ? givenTItle : mEventCard.getTitle();

                    ArrayList<Tag> givenTags = mQuestion.getGivenTags();
                    ArrayList<Tag> tags = givenTags != null ? givenTags : Tag.stringToList(mEventCard.getTags());

                    String givenDescription = mQuestion.getGivenDescription().toString();
                    String description = givenDescription.compareTo("null") != 0 ? givenDescription : mEventCard.getDescription();

                    mButtonClickListener.onSubmit(date, title,
                                                  tags, description);
                }
            } else if (id == R.id.btn_q_card_cancel) {
                if (mButtonClickListener != null) {
                    mButtonClickListener.onCancel();
                }
            } else if (id == R.id.btn_q_card_already) {
                if (mButtonClickListener != null) {
                    mButtonClickListener.onAlreadyHave();
                }
            }
        }
    };

}
