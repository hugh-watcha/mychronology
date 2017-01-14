package com.songsingasong.mychronology;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.songsingasong.mychronology.event_card.AddEventCard;
import com.songsingasong.mychronology.event_card.EditEventCard;
import com.songsingasong.mychronology.event_card.QCard;
import com.songsingasong.mychronology.login.LogInPage;
import com.songsingasong.mychronology.login.SessionManager;
import com.songsingasong.mychronology.main_list.MainListController;
import com.songsingasong.mychronology.model.event.EventManager;
import com.songsingasong.mychronology.model.question.QuestionQueue;
import com.songsingasong.mychronology.model.question.QuestionReceiver;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CLASS_NAME = "MainActivity";

    private AddEventCard mAddEventCard;
    private EditEventCard mEditEventCard;
    private QCard mQCard;

    private AddEventCard.OnButtonClickListener mOnAddEventCardButtonClickListener = new AddEventCard.OnButtonClickListener() {
        @Override
        public void onSubmit(SDate date, String title, ArrayList<Tag> tags, String description) {
            SLog.d(CLASS_NAME, "AddEventCard.OnButtonClickListener$onSubmit", "");
            EventManager.getInstance().addEvent(SessionManager.getInstance().getUserId(), date,
                                                title, tags, description, 0.0f, null);
            mAddEventCard.hideCard();
        }

        @Override
        public void onCancel() {
            SLog.d(CLASS_NAME, "AddEventCard.OnButtonClickListener$onCancel", "");
            mAddEventCard.hideCard();
        }
    };

    private EditEventCard.OnButtonClickListener mOnEditEventCardButtonClickListener = new EditEventCard.OnButtonClickListener() {
        @Override
        public void onSubmit(long id, SDate date, String title, ArrayList<Tag> tags, String description) {
            SLog.d(CLASS_NAME, "EditEventCard.OnButtonClickListener$onSubmit", "");
            EventManager.getInstance().updateEvent(SessionManager.getInstance().getUserId(), id,
                                                   date, title, tags, description, 0.0f, null);
            mEditEventCard.hideCard();
        }

        @Override
        public void onCancel() {
            SLog.d(CLASS_NAME, "EditEventCard.OnButtonClickListener$onCancel", "");
            mEditEventCard.hideCard();
        }

        @Override
        public void onRemove(long id) {
            SLog.d(CLASS_NAME, "EditEventCard.OnButtonClickListener$onRemove", "");
            EventManager.getInstance().removeEvent(id);
            mEditEventCard.hideCard();
        }
    };

    private QCard.OnButtonClickListener mOnQCardButtonClickListener = new QCard.OnButtonClickListener() {
        @Override
        public void onSubmit(SDate date, String title, ArrayList<Tag> tags, String description) {
            SLog.d(CLASS_NAME, "QCard.OnButtonClickListener$onSubmit", "");
            EventManager.getInstance().addEvent(SessionManager.getInstance().getUserId(), date,
                                                title, tags, description, 0.0f, null);
            mQCard.hideCard();
            QuestionQueue.getInstance().remove();
        }

        @Override
        public void onCancel() {
            SLog.d(CLASS_NAME, "QCard.OnButtonClickListener$onCancel", "");
            mQCard.hideCard();
        }

        @Override
        public void onAlreadyHave() {
            SLog.d(CLASS_NAME, "QCard.OnButtonClickListener$onAlreadyHave", "");
            mQCard.hideCard();
            QuestionQueue.getInstance().remove();
        }
    };

    private MainListController.OnItemClickListener mOnListItemClickListener = new MainListController.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SLog.d(CLASS_NAME, "onItemClick", "");
            mEditEventCard.showCard(EventManager.getInstance().getEventList().get(position));
        }
    };

    private SessionManager.OnSessionListener mSessionListener = new SessionManager.OnSessionListener() {
        @Override
        public void onCurrentUserChanged(String userId) {
            EventManager.getInstance().changeUserId(userId);
        }
    };

    private LogInPage mLogInPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.d(CLASS_NAME, "onCreate", "");
        SessionManager.getInstance().init(SApp.getContext());
        SessionManager.getInstance().registerSessionChangedListener(mSessionListener);
        EventManager.getInstance();
        setContentView(R.layout.activity_main);

        setActionBarCustom();

        mLogInPage = new LogInPage();
        mLogInPage.init((ViewGroup) findViewById(R.id.login_card));
        mLogInPage.closeLogInCard();

        MainListController mMainListController = new MainListController(getApplicationContext(),
                                                                        (RecyclerView) findViewById(
                                                                                R.id.main_list));
        mMainListController.setOnItemClickListener(mOnListItemClickListener);

        ViewGroup mRootLayout = (ViewGroup) findViewById(R.id.layout_for_card);
        mAddEventCard = new AddEventCard(getApplicationContext(),
                                         mRootLayout);
        mAddEventCard.setOnButtonClickListener(mOnAddEventCardButtonClickListener);

        mEditEventCard = new EditEventCard(getApplicationContext(), mRootLayout);
        mEditEventCard.setOnButtonClickListener(mOnEditEventCardButtonClickListener);

        mQCard = new QCard(getApplicationContext(), mRootLayout);
        mQCard.setOnButtonClickListener(mOnQCardButtonClickListener);

        EventManager.getInstance().changeUserId("0");
    }

    private void setActionBarCustom() {
        SLog.d(CLASS_NAME, "setActionBarCustom", "");
        TextView addButton = (TextView) findViewById(R.id.btn_actionbar_add);
        addButton.setOnClickListener(this);

        TextView questionButton = (TextView) findViewById(R.id.btn_actionbar_question);
        questionButton.setOnClickListener(this);

        mQuestionCount = (TextView) findViewById(R.id.tv_actionbar_question_count);

        QuestionQueue.getInstance().registereOnQueueChangeListener(mOnQueueChangeListener);
    }

    private TextView mQuestionCount;
    private QuestionQueue.OnQueueChangeListener mOnQueueChangeListener = new QuestionQueue.OnQueueChangeListener() {
        @Override
        public void onQuestionQueueChanged(final int count) {
            SLog.d(CLASS_NAME, "onQuestionQueueChanged", "count : " + count);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mQuestionCount == null) {
                        return;
                    }
                    mQuestionCount.setText(String.format("%d", count));
                    if (count == 0) {
                        mQuestionCount.setVisibility(View.INVISIBLE);
                    } else {
                        mQuestionCount.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SessionManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SLog.d(CLASS_NAME, "onResume", "");
        AppEventsLogger.activateApp(this);
        QuestionQueue.getInstance().onResume();
        QuestionReceiver.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SLog.d(CLASS_NAME, "onPause", "");
        AppEventsLogger.deactivateApp(this);
        QuestionReceiver.stop();
        QuestionQueue.getInstance().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SLog.d(CLASS_NAME, "onDestroy", "");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_actionbar_add: {
                SLog.d(CLASS_NAME, "onClick", "id : btn_actionbar_add");
                showAddEventCard();
                break;
            }

            case R.id.btn_actionbar_question: {
                SLog.d(CLASS_NAME, "onClick", "id : btn_actionbar_question");
                showQuestionCard();
                break;
            }

            default:
                break;
        }
    }

    private void showAddEventCard() {
        if (mAddEventCard != null) {
            mAddEventCard.showCard();
        }
    }

    private void showQuestionCard() {
        if (mQCard != null) {
            if (!QuestionQueue.getInstance().isEmpty()) {
                mQCard.showCard(QuestionQueue.getInstance().peek());
            }
        }
    }
}
