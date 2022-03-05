package com.dzurikm.quizapp.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dzurikm.quizapp.R;
import com.dzurikm.quizapp.activities.MainActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class WriteQuizFile extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Uri fileToTakeContentFrom;

    public WriteQuizFile(Context context,Uri fileToTakeContentFrom) {
        this.context = context;
        this.fileToTakeContentFrom = fileToTakeContentFrom;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            String dataToWrite = getJsonText();

            try {

                OutputStream outputStream = context.openFileOutput("quiz.json", Context.MODE_PRIVATE);

                outputStream.write(dataToWrite.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        MainActivity.onFileImported();
    }

    private String getJsonText() throws IOException {
        String data = "";

        if (!fileExist()){
            OutputStream outputStream = context.openFileOutput("quiz.json", Context.MODE_PRIVATE);
            outputStream.write(getExampleJSONContent().getBytes());
            outputStream.close();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(fileToTakeContentFrom)));

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

            Log.i("UPLOADED DATA",data);

        return data;
    }

    private boolean fileExist(){
        try {
            FileInputStream fileInputStream = context.openFileInput("quiz.json");
            fileInputStream.close();
            return true;
        } catch (IOException e) {
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
            data = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }
}
