package com.dzurikm.quizapp.activities;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzurikm.quizapp.R;
import com.dzurikm.quizapp.async.DownloadQuizFile;
import com.dzurikm.quizapp.async.WriteQuizFile;
import com.dzurikm.quizapp.views.ImageButton;
import com.dzurikm.quizapp.views.MenuButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private MenuButton startButton,exitButton;
    private ImageButton onlineUpload,offlineUpload;
    final int FILE_REQUEST_CODE = 2;
    private static TextView userInformationText;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Log.i("SRC", Environment.getExternalStorageDirectory().getAbsolutePath());

        startButton = (MenuButton) findViewById(R.id.startButton);
        exitButton = (MenuButton) findViewById(R.id.exitButton);

        onlineUpload = (ImageButton) findViewById(R.id.onlineUploadButton) ;
        offlineUpload = (ImageButton) findViewById(R.id.offlineUploadButton) ;
        userInformationText = (TextView) findViewById(R.id.userInformationTextView);


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,QuizActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        offlineUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLocalFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                openLocalFile.addCategory(Intent.CATEGORY_OPENABLE);
                openLocalFile.setType("application/json");
                MainActivity.this.startActivityForResult(openLocalFile,FILE_REQUEST_CODE);
            }
        });

        onlineUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.Theme_QuizApp_AlertDialogTheme);
                builder.setTitle("Prilož link");
                builder.setMessage("This is message");

                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = getTheme();
                theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
                @ColorInt int color = typedValue.data;

                final EditText input = new EditText(MainActivity.this);
                FrameLayout container = new FrameLayout(MainActivity.this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.alertInputMargin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.alertInputMargin);
                input.setLayoutParams(params);
                input.setBackgroundResource(R.drawable.menu_button_background);

                container.addView(input);

                builder.setView(container);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = input.getText().toString();
                        new DownloadQuizFile(getApplicationContext(),url).execute();
                    }
                });
                builder.setNegativeButton("Zrušiť", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                AlertDialog dialog = builder.create();
                builder.show();

            }
        });

       try {
           Log.i("JSON EXAMPLE",getJsonText());
       } catch (IOException e) {
           e.printStackTrace();
       }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
            }

            Log.d("uri",uri.toString());

            WriteQuizFile writeQuizFile = (WriteQuizFile) new WriteQuizFile(getApplicationContext(),uri).execute();
        }
    }

    public static void onFileImported(){
        Log.i("File import","Done");

        userInformationText.setText("Quiz sa importoval úspešne");
    }

    public static void onFileDownloaded(){
        Log.i("File import","Done");

        userInformationText.setText("Quiz sa stiahol a importoval úspešne");
    }


    private String getJsonText() throws IOException {
        String data = "";

        try {
            FileInputStream fileInputStream = openFileInput("quiz.json");
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

        Log.i("UPLOADED DATA",data);

        return data;
    }

    public static void showShortToast(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}