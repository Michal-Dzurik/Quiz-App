package com.dzurikm.quizapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dzurikm.quizapp.activities.MainActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadQuizFile extends AsyncTask<Void,Void,Integer> {
    private String url;
    private Context context;

    public DownloadQuizFile(Context context,String url) {
        this.url = url;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        try {
            URL u = new URL(url);
            String[] urlArr = url.split("\\.");

            Log.i("URl",url);
            if (!urlArr[urlArr.length - 1].toLowerCase().equals("json")) {
                return 3;
            }

            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            OutputStream fos = context.openFileOutput("quiz.json", Context.MODE_PRIVATE);
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (MalformedURLException e) {
            return 2;
        } catch (IOException e) {
            return 1;
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        if (result == 0){
            MainActivity.onFileDownloaded();
        }

        switch (result){
            case 1:
                MainActivity.showShortToast("Mali sme problémy so sťahovaním");
                break;
            case 2:
                MainActivity.showShortToast("Toja adressa nie je valídna");
                break;
            case 3:
                MainActivity.showShortToast("Na tejto adrese sa nenachádza súbor s príponou .json");
                break;
            default:
                MainActivity.showShortToast("Error");
                break;
        }

    }

}
