package com.songsingasong.mychronology.model.question;

import com.songsingasong.mychronology.model.tag.Tag;
import com.songsingasong.mychronology.utils.LocaleString;
import com.songsingasong.mychronology.utils.SDate;
import com.songsingasong.mychronology.utils.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by jaewoosong on 15. 12. 17..
 */
class QuestionReceiveAdapter {
    private static final String CLASS_NAME = "QuestionReceiveAdapter";

//    private static final String IP = "182.216.229.225";
//    private static final String PORT = "9090";
    private static final String IP = "192.168.219.6";
    private static final String PORT = "80";
    private static final String Q_QUERY = "http://" + IP + ":" + PORT + "/question.php?index=";

    public ArrayList<Question> getQuestions(long index){
        /** HTTP GET */
        String url = Q_QUERY + index;
        URL request_url = null;
        try {
            request_url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (request_url == null) {
            return null;
        }

        SLog.d(CLASS_NAME, "getQuestions", "url size : " + url.getBytes().length + " Bytes");

        HttpURLConnection http_conn = null;
        JSONObject json = null;
        try {
            http_conn = (HttpURLConnection) request_url.openConnection();
            http_conn.setConnectTimeout(100000);
            http_conn.setReadTimeout(100000);
            http_conn.setRequestMethod("GET");
            //        http_conn.setDoOutput(true);
            //        String ret = http_conn.getResponseMessage();
            json = streamToJson(http_conn.getInputStream());
            SLog.d(CLASS_NAME, "getQuestions", "json : " + json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        HttpURLConnection.setFollowRedirects(true);

        if (http_conn != null) {
            http_conn.disconnect();
        }

        ArrayList<Question> questions = null;

        if (json != null) {
            try {
                JSONArray questionArray = json.getJSONArray("questions");
                if (questionArray != null) {
                    questions = new ArrayList<>();
                    int length = questionArray.length();
                    for (int i = 0; i < length; i++) {
                        questions.add(jsonToQuestion(questionArray.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return questions;
    }

    private JSONObject streamToJson(InputStream is) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = rd.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        String response = responseStrBuilder.toString();
        SLog.d(CLASS_NAME, "streamToJson", "response size : " + response.getBytes().length + " Bytes");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private Question jsonToQuestion(JSONObject json) {
        if (json == null) {
            return null;
        }

        LocaleString qStr = null;
        String requestSection = null;
        ArrayList<Tag> tags = null;
        LocaleString title = null;
        LocaleString description = null;
        SDate date = null;
        float importance = 0f;

        try {
             qStr = new LocaleString(json.getString("question_en"), json.getString("question_ko"), json.getString("question_cn"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            requestSection = json.getString("request_section");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray tagArray = json.getJSONArray("given_tags");
            if (tagArray != null) {
                tags = new ArrayList<>();
                int length = tagArray.length();
                for (int i = 0; i < length; i++) {
                    tags.add(new Tag(tagArray.getString(i)));
                }
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            SLog.d(CLASS_NAME, "jsonToQuestion", "No tags");
        }

        try {
            title = new LocaleString(json.getString("given_title_en"), json.getString("given_title_ko"), json.getString("given_title_cn"));
        } catch (JSONException e) {
//            e.printStackTrace();
            SLog.d(CLASS_NAME, "jsonToQuestion", "No given title");
        }

        try {
            description = new LocaleString(json.getString("given_description_en"), json.getString("given_description_ko"), json.getString("given_description_cn"));
        } catch (JSONException e) {
//            e.printStackTrace();
            SLog.d(CLASS_NAME, "jsonToQuestion", "No given description");
        }

        try {
            String d = json.getString("given_date");
            if (d != null && d.length() == 8) {
                int year = Integer.valueOf(d.substring(0, 4));
                int month = Integer.valueOf(d.substring(4, 6));
                int day = Integer.valueOf(d.substring(6));

                date = new SDate(year, month, day);
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            SLog.d(CLASS_NAME, "jsonToQuestion", "No given date");
        }

        try {
            importance = (float) json.getDouble("given_importance");
        } catch (JSONException e) {
//            e.printStackTrace();
            SLog.d(CLASS_NAME, "jsonToQuestion", "No given importance");
        }

        Question question = new Question.Builder(qStr, requestSection).tags(tags).title(title).description(description).date(date).importance(importance).build();
        SLog.d(CLASS_NAME, "jsonToQuestion", "" + question);

        return question;
    }
}
