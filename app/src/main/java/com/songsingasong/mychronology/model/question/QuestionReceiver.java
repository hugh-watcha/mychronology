package com.songsingasong.mychronology.model.question;

import android.content.Context;
import android.content.SharedPreferences;

import com.songsingasong.mychronology.SApp;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;

/**
 * Created by jaewoosong on 15. 12. 17..
 */
public class QuestionReceiver {

    private static class Receiver extends Thread {
        private static final String CLASS_NAME = "QuestionReceiverThread";
        private static final long WAITING_TIME = 60 * 1000;

        private boolean mRunning;
        private QuestionIndexManager mIndexManager;
        private QuestionReceiveAdapter mAdapter;

        public Receiver() {
            setName(CLASS_NAME);
            setPriority(Thread.NORM_PRIORITY);
            mIndexManager = new QuestionIndexManager();
            mIndexManager.load();

            mAdapter = new QuestionReceiveAdapter();
        }

        @Override
        public void run() {
            super.run();

            while (mRunning) {
                ArrayList<Question> questions = getQuestions(mIndexManager.getIndex());

                if (questions != null) {
                    // handle question
                    QuestionQueue.getInstance().addAll(questions);
                    mIndexManager.increaseIndex();
                }

                if (isInterrupted()) {
                    SLog.d(CLASS_NAME, "run", "Thread interrupted 1");
                    mRunning = false;
                    continue;
                }

                try {
                    sleep(WAITING_TIME);
                } catch (InterruptedException e) {
                    SLog.d(CLASS_NAME, "run", "Thread interrupted 2");
                    mRunning = false;
                }
            }

            onStop();
        }

        public void prepare() {
            mRunning = true;
        }

        @Override
        public void interrupt() {
            super.interrupt();
        }

        private ArrayList<Question> getQuestions(long index) {
            return mAdapter.getQuestions(index);
        }

        private void onStop() {
            mIndexManager.save();
        }

        private static class QuestionIndexManager {
            private static final String CLASS_NAME = "QuestionIndexManager";
            private static final String PREF_NAME = "QuestionIndex";
            private static final String KEY = "QuestionIndex";

            private int mIndex;

            public void load() {
                SharedPreferences pref = SApp.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                mIndex = pref.getInt(KEY, 0);
                SLog.d(CLASS_NAME, "load", "index : " + mIndex);
            }

            public int getIndex() {
                return mIndex;
            }

            public void increaseIndex() {
                mIndex++;
                SLog.d(CLASS_NAME, "increaseIndex", "index : " + mIndex);
            }

            public void save() {
                SLog.d(CLASS_NAME, "save", "index : " + mIndex);
                SharedPreferences pref = SApp.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                pref.edit().putInt(KEY, mIndex).apply();
            }
        }
    }

    private static Receiver mReceiver;

    public static void start() {
        Receiver receiver = getReceiver();
        receiver.prepare();
        receiver.start();
    }

    public static void stop() {
        Receiver receiver = getReceiver();
        receiver.interrupt();
        resetReceiver();
    }

    private static void resetReceiver() {
        mReceiver = null;
    }

    private static Receiver getReceiver() {
       if (mReceiver == null) {
           mReceiver = new Receiver();
       }

        return mReceiver;
    }

}
