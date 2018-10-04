package com.example.faceit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.faceit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private EditText passcode;
    private String codeStr = "0000";

    /**
     * Called when the user taps the Send button
     */

    public void startQuiz(View view) {
        passcode = (EditText) findViewById(R.id.passcode);

        //get inserted text from EditText widget
        String passcodeStr = passcode.getText().toString();


        if (!(passcodeStr.equals(codeStr))) {
            //enter game mode
            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
            startActivity(intent);
        } else {
            //enter guardian mode...
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

    }
}
