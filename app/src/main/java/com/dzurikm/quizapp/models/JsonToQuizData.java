package com.dzurikm.quizapp.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dzurikm.quizapp.R;
import com.dzurikm.quizapp.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Locale;

public class JsonToQuizData {
    private String url;
    private Context context;
    public int result = 0;

    public JsonToQuizData(Context context,String url){
        this.url = url;
        this.context = context;
    }

    public LinkedList<Quiz.Question> getData(){
        LinkedList<Quiz.Question> questions = new LinkedList<Quiz.Question>();

        try{
            String text = getJsonText();
            Log.i("JSON DATA RAW",text);

            JSONObject obj = new JSONObject(text);

            JSONArray arr = obj.getJSONArray("questions");

            for (int i = 0; i < arr.length(); i++)
            {
                JSONArray smallArr = arr.getJSONObject(i).getJSONArray("answers");

                Quiz.Answer answer = new Quiz.Answer(arr.getJSONObject(i).getString("rightAnswer"),getLinkedList(smallArr));

                Quiz.Question question = new Quiz.Question(answer,formatQuestion(arr.getJSONObject(i).getString("question")));

                questions.add(question);

            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            result = 1;
            Log.i("JSON DATA RAW", "Error");
        }

        Log.i("JSON FULL DATA" , questions.toString());

        return questions;
    }

    private String formatQuestion(String q){
        q = q.trim();

        if (q.charAt(q.length() - 1) != '?') q += "?";

        return q;
    }

    private LinkedList<String> getLinkedList(JSONArray arr) throws JSONException {
        LinkedList<String> listdata = new LinkedList<String>();
        JSONArray jArray = (JSONArray) arr;
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getString(i));
            }
        }

        return listdata;
    }

    private String getJsonText() throws IOException {
        String data = "";
        boolean fileExist = fileExist();

        if (!fileExist){
            OutputStream outputStream = context.openFileOutput("quiz.json", Context.MODE_PRIVATE);
            outputStream.write(getExampleJSONContent().getBytes());
            outputStream.close();
        }

        try {
            FileInputStream fileInputStream = context.openFileInput("quiz.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            Log.d("CONTENT FROM NEW FILE",sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("CONTENT FROM NEW FILE",data);

        return data;
    }

    private boolean fileExist(){
        try {
            FileInputStream fileInputStream = context.openFileInput("quiz.json");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String getExampleJSONContent(){
        InputStream fileInputStream = context.getResources().openRawResource(R.raw.quiz_example);
        String data = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            data =  sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DATA LOADED FROM EX",data);

        }

        Log.i("DATA LOADED FROM EX",data);

        return data;

    }

}
