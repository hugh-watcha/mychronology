package com.songsingasong.mychronology.model.question;

import com.songsingasong.mychronology.SApp;
import com.songsingasong.mychronology.login.SessionManager;
import com.songsingasong.mychronology.model.db.DBHandler;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jaewoosong on 15. 12. 21..
 */
public class QuestionQueue {
    private static final String CLASS_NAME = "QuestionQueue";

    private static QuestionQueue mInstance;

    private QuestionQueue() {}

    public static QuestionQueue getInstance() {
        if (mInstance == null) {
            mInstance = new QuestionQueue();
        }

        return mInstance;
    }

    public interface OnQueueChangeListener {
        void onQuestionQueueChanged(int count);
    }

    private ArrayList<OnQueueChangeListener> mListeners = new ArrayList<>();

    public void registereOnQueueChangeListener(OnQueueChangeListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void unregisterOnQueueChangeListener(OnQueueChangeListener listener) {
        mListeners.remove(listener);
    }

    private void callOnQueueChange() {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
                for (OnQueueChangeListener listener : mListeners) {
                    listener.onQuestionQueueChanged(mQueue.size());
                }
//            }
//        });
    }

    private LinkedList<Question> mQueue = new LinkedList<>();

    public boolean add(Question question) {
        if (question == null) {
            return false;
        }
        SLog.d(CLASS_NAME, "add", "question : " + question);
        boolean ret = mQueue.add(question);
        callOnQueueChange();
        return ret;
    }

    public boolean addAll(ArrayList<Question> questions) {
        boolean ret = mQueue.addAll(questions);
        callOnQueueChange();
        SLog.d(CLASS_NAME, "addAll", "size : " + questions.size());
        return ret;
    }

    public Question peek() {
        SLog.d(CLASS_NAME, "peek", "");
        return mQueue.peek();
    }

    public void remove() {
        mQueue.remove();
        callOnQueueChange();
        SLog.d(CLASS_NAME, "remove", "");
    }

    public Question poll() {
        Question q = mQueue.poll();
        callOnQueueChange();
        return q;
    }

    public boolean isEmpty() {
        return mQueue.isEmpty();
    }

    public void onPause() {
        ArrayList<Question> questions = new ArrayList<>();
        while (!mQueue.isEmpty()) {
            questions.add(mQueue.poll());
        }
        if (questions.isEmpty()) {
            return;
        }

        DBHandler dbHandler = DBHandler.getInstance(SApp.getContext());
        dbHandler.addAllQuestions(SessionManager.getInstance().getUserId(), questions);
    }

    public void onResume() {
        DBHandler dbHandler = DBHandler.getInstance(SApp.getContext());
        ArrayList<Question> questions = dbHandler.getAllQuestions(SessionManager.getInstance().getUserId());
        addAll(questions);
    }

}
