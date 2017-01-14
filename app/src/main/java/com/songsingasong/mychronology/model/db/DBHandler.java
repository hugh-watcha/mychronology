package com.songsingasong.mychronology.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.songsingasong.mychronology.model.event.Event;
import com.songsingasong.mychronology.model.question.Question;
import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.LocaleString;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jaewoosong on 2015. 11. 30..
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final String CLASS_NAME = "DBHandler";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "myChronology";

    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_QUESTIONS = "questions";

    private enum DbType {
        INTEGER     ("INTEGER"),
        TEXT        ("TEXT"),
        PRIMARY_KEY ("INTEGER PRIMARY KEY"),
        FLOAT       ("FLOAT");

        private final String name;

        DbType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum EventDbKeys {
        ID              ("id", DbType.PRIMARY_KEY),
        USER_ID         ("user_id", DbType.TEXT),
        CHRONOLOGY_NAME ("chronology_name", DbType.TEXT),
        YEAR            ("year", DbType.INTEGER),
        MONTH           ("month", DbType.INTEGER),
        DAY             ("day", DbType.INTEGER),
        TITLE           ("title", DbType.TEXT),
        TAGS            ("tags", DbType.TEXT),
        DESCRIPTION     ("description", DbType.TEXT),
        IMPORTANCE      ("importance", DbType.TEXT),
        PICTURES        ("pictures", DbType.TEXT);

        public static String[] getKeys() {
            EventDbKeys[] keys = values();
            String[] strs = new String[keys.length];
            int index = 0;
            for (EventDbKeys key : keys) {
                strs[index++] = key.toString();
            }

            return strs;
        }

        private final String keyString;
        private final DbType type;
        private int index;

        EventDbKeys(String keyString, DbType type) {
            this.keyString = keyString;
            this.type = type;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

        public DbType type() {
            return type;
        }

        @Override
        public String toString() {
            return keyString;
        }
    }

    private enum QuestionDbKeys {
        ID                      ("id", DbType.PRIMARY_KEY),
        USER_ID                 ("user_id", DbType.TEXT),
        CHRONOLOGY_NAME         ("chronology_name", DbType.TEXT),
        QUESTION_EN             ("question_en", DbType.TEXT),
        QUESTION_KO             ("question_ko", DbType.TEXT),
        QUESTION_CN             ("question_cn", DbType.TEXT),
        REQUEST_SECTIONS        ("request_sections", DbType.TEXT),
        GIVEN_TAGS_EN           ("given_tags_en", DbType.TEXT),
        GIVEN_TAGS_KO           ("given_tags_ko", DbType.TEXT),
        GIVEN_TAGS_CN           ("given_tags_cn", DbType.TEXT),
        GIVEN_TITLE_EN          ("given_title_en", DbType.TEXT),
        GIVEN_TITLE_KO          ("given_title_ko", DbType.TEXT),
        GIVEN_TITLE_CN          ("given_title_cn", DbType.TEXT),
        GIVEN_DESCRIPTION_EN    ("given_description_en", DbType.TEXT),
        GIVEN_DESCRIPTION_KO    ("given_description_ko", DbType.TEXT),
        GIVEN_DESCRIPTION_CN    ("given_description_cn", DbType.TEXT),
        GIVEN_DATE              ("given_date", DbType.TEXT),
        GIVEN_IMPORTANCE        ("given_importance", DbType.FLOAT);

        public static String[] getKeys() {
            QuestionDbKeys[] keys = values();
            String[] strs = new String[keys.length];
            int index = 0;
            for (QuestionDbKeys key : keys) {
                strs[index++] = key.toString();
            }

            return strs;
        }

        private final String keyString;
        private final DbType type;
        private int index;

        QuestionDbKeys(String keyString, DbType type) {
            this.keyString = keyString;
            this.type = type;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

        public DbType type() {
            return type;
        }

        @Override
        public String toString() {
            return keyString;
        }
    }

    static {
        int index = 0;
        for (EventDbKeys key : EventDbKeys.values()) {
            key.setIndex(index++);
        }
        index = 0;
        for (QuestionDbKeys key : QuestionDbKeys.values()) {
            key.setIndex(index++);
        }
    }

    private static DBHandler mInstance;

    public static DBHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHandler(context);
        }

        return mInstance;
    }

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SLog.d(CLASS_NAME, "onCreate", "");
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENTS +
                " (" + getEventColumnListWithType() + ")";
        db.execSQL(CREATE_EVENT_TABLE);

        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + " (" + getQuestionColumnListWithType() + ")";
        db.execSQL(CREATE_QUESTION_TABLE);
    }

    private String getEventColumnListWithType() {
        String str = "";
        for (EventDbKeys key : EventDbKeys.values()) {
            str += (key + " " + key.type() + ",");
        }

        return str.substring(0, str.length() - 1);
    }

    private String getQuestionColumnListWithType() {
        String str = "";
        for (QuestionDbKeys key : QuestionDbKeys.values()) {
            str += (key + " " + key.type() + ",");
        }

        return str.substring(0, str.length() - 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SLog.d(CLASS_NAME, "onUpgrade", "oldVer : " + oldVersion + ", newVer : " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);

        onCreate(db);
    }

    public void addEvent(String userId, Event event) {
        SLog.d(CLASS_NAME, "addEvent", "userId : " + userId + ", event : " + event.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = makeValuesWithEvent(userId, event);

        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();

        event.setId(id);
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, EventDbKeys.getKeys(), EventDbKeys.ID + "=?",
                                 new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor == null) {
            return null;
        }

        Event event = null;
        if (cursor.moveToFirst()) {
            event = getEventFromRow(cursor);
        }

        db.close();

        return event;
    }

    public ArrayList<Event> getAllEvents(String userId) {
        ArrayList<Event> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + EventDbKeys.USER_ID + "=" + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return events;
        }

        if (cursor.moveToFirst()) {
            do {
                events.add(getEventFromRow(cursor));
            } while (cursor.moveToNext());
        }

        db.close();

        SLog.d(CLASS_NAME, "getAllEvents", "userId : " + userId + ", size : " + events.size());
        return events;
    }

    public int getEventsCount(String userId) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS + " WHERE" + EventDbKeys.USER_ID + "=" + userId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateEvent(String userId, Event event) {
        SLog.d(CLASS_NAME, "updateEvent", "userId : " + userId + ", event : " + event);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = makeValuesWithEvent(userId, event);
        // updating row
        int ret = db.update(TABLE_EVENTS, values, EventDbKeys.ID + " = ?",
                         new String[]{String.valueOf(event.getId())});

        db.close();
        return ret;
    }

    public void deleteEvent(Event event) {
        SLog.d(CLASS_NAME, "deleteEvent", "event : " + event);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, EventDbKeys.ID + " = ?",
                  new String[] { String.valueOf(event.getId()) });
        db.close();
    }

    private Event getEventFromRow(Cursor cursor) {
        return new Event(Integer.parseInt(cursor.getString(EventDbKeys.ID.index())),
                         new SDate(Integer.parseInt(cursor.getString(EventDbKeys.YEAR.index())), Integer.parseInt(cursor.getString(
                                 EventDbKeys.MONTH.index())), Integer.parseInt(
                                 cursor.getString(EventDbKeys.DAY.index()))),
                         cursor.getString(EventDbKeys.TITLE.index()), cursor.getString(EventDbKeys.TAGS.index()), cursor.getString(
                EventDbKeys.DESCRIPTION.index()),
                         Float.parseFloat(cursor.getString(EventDbKeys.IMPORTANCE.index())), cursor.getString(
                EventDbKeys.PICTURES.index()));
    }

    private ContentValues makeValuesWithEvent(String userId, Event event) {
        ContentValues values = new ContentValues();
        values.put(EventDbKeys.USER_ID.toString(), userId);
        values.put(EventDbKeys.YEAR.toString(), event.getDate().getYear());
        values.put(EventDbKeys.MONTH.toString(), event.getDate().getMonth());
        values.put(EventDbKeys.DAY.toString(), event.getDate().getDay());
        values.put(EventDbKeys.TITLE.toString(), event.getTitle());
        values.put(EventDbKeys.TAGS.toString(), Tag.convertListToString(event.getTagList()));
        values.put(EventDbKeys.DESCRIPTION.toString(), event.getDescription());
        values.put(EventDbKeys.IMPORTANCE.toString(), event.getImportance());
        values.put(EventDbKeys.PICTURES.toString(), event.getPicturesAsString());

        return values;
    }

    private ContentValues makeValuesWithQuestion(String userId, Question question) {
        ContentValues values = new ContentValues();
        values.put(QuestionDbKeys.USER_ID.toString(), userId);
        values.put(QuestionDbKeys.QUESTION_EN.toString(), question.getQuestion().get(Locale.ENGLISH));
        values.put(QuestionDbKeys.QUESTION_KO.toString(), question.getQuestion().get(Locale.KOREA));
        values.put(QuestionDbKeys.QUESTION_CN.toString(), question.getQuestion().get(Locale.CHINA));
        values.put(QuestionDbKeys.REQUEST_SECTIONS.toString(), question.getRequestSections());
        values.put(QuestionDbKeys.GIVEN_TAGS_EN.toString(), Tag.convertListToString(
                question.getGivenTags()));
        LocaleString givenTitle = question.getGivenTitle();
        if (givenTitle != null) {
            values.put(QuestionDbKeys.GIVEN_TITLE_EN.toString(), givenTitle.get(Locale.ENGLISH));
            values.put(QuestionDbKeys.GIVEN_TITLE_KO.toString(),
                       givenTitle.get(Locale.KOREA));
            values.put(QuestionDbKeys.GIVEN_TITLE_CN.toString(),
                       givenTitle.get(Locale.CHINA));
        }
        LocaleString givenDescription = question.getGivenDescription();
        if (givenDescription != null) {
            values.put(QuestionDbKeys.GIVEN_DESCRIPTION_EN.toString(), givenDescription.get(
                    Locale.ENGLISH));
            values.put(QuestionDbKeys.GIVEN_DESCRIPTION_KO.toString(),
                       givenDescription.get(Locale.KOREA));
            values.put(QuestionDbKeys.GIVEN_DESCRIPTION_CN.toString(),
                       givenDescription.get(Locale.CHINA));
        }
        SDate givenDate = question.getGivenDate();
        if (givenDate != null) {
            values.put(QuestionDbKeys.GIVEN_DATE.toString(),
                       String.format("%.4d%.2d%.2d", givenDate.getYear(),
                                     givenDate.getMonth(),
                                     givenDate.getDay()));
        }
        values.put(QuestionDbKeys.GIVEN_IMPORTANCE.toString(), question.getGivenImportance());

        return values;
    }

    private Question getQuestionFromRow(Cursor cursor) {
        LocaleString question = new LocaleString(cursor.getString(QuestionDbKeys.QUESTION_EN.index()), cursor.getString(QuestionDbKeys.QUESTION_KO.index()), cursor.getString(QuestionDbKeys.QUESTION_CN.index()));
        String requestSections = cursor.getString(QuestionDbKeys.REQUEST_SECTIONS.index());

        ArrayList<Tag> tags = Tag.stringToList(
                cursor.getString(QuestionDbKeys.GIVEN_TAGS_EN.index()));
        String titleEn = cursor.getString(QuestionDbKeys.GIVEN_TITLE_EN.index());
        String titleKo = cursor.getString(QuestionDbKeys.GIVEN_TITLE_KO.index());
        String titleCn = cursor.getString(QuestionDbKeys.GIVEN_TITLE_CN.index());
        LocaleString title = null;
        if (titleEn != null) {
            title = new LocaleString(titleEn, titleKo, titleCn);
        }

        String descriptionEn = cursor.getString(QuestionDbKeys.GIVEN_DESCRIPTION_EN.index());
        String descriptionKo = cursor.getString(QuestionDbKeys.GIVEN_DESCRIPTION_KO.index());
        String descriptionCn = cursor.getString(QuestionDbKeys.GIVEN_DESCRIPTION_CN.index());
        LocaleString description = null;
        if (descriptionEn != null) {
            description = new LocaleString(descriptionEn, descriptionKo, descriptionCn);
        }

        float importance = cursor.getLong(QuestionDbKeys.GIVEN_IMPORTANCE.index());

        String strDate = cursor.getString(QuestionDbKeys.GIVEN_DATE.index());
        SDate date = null;

        if (strDate != null) {
            date = new SDate(Integer.parseInt(strDate.substring(0, 4)), Integer.parseInt(strDate.substring(4,6)), Integer.parseInt(strDate.substring(6, 8)));
        }

        return new Question.Builder(question, requestSections).tags(tags).title(title).description(description).importance(importance).date(date).build();
    }

    public void addAllQuestions(String userId, ArrayList<Question> questions) {
        SLog.d(CLASS_NAME, "addAllQuestions", "userId : " + userId + ", size : " + questions.size());
        for (Question question : questions) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = makeValuesWithQuestion(userId, question);

            // Inserting Row
            long id = db.insert(TABLE_QUESTIONS, null, values);
            db.close(); // Closing database connection

            question.setId(id);
        }
    }

    public ArrayList<Question> getAllQuestions(String userId) {
        ArrayList<Question> questions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + QuestionDbKeys.USER_ID + "=" + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return questions;
        }

        if (cursor.moveToFirst()) {
            do {
                questions.add(getQuestionFromRow(cursor));
                db.delete(TABLE_QUESTIONS, QuestionDbKeys.ID + "=?", new String[] { "" + cursor.getLong(QuestionDbKeys.ID.index) });
            } while (cursor.moveToNext());
        }

        db.close();
        SLog.d(CLASS_NAME, "getAllQuestions", "userId : " + userId + ", size : " + questions.size());
        return questions;
    }
}
