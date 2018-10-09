package com.example.faceit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.faceit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Persistent.setContext(getApplicationContext());
    }

    private EditText passcode;
    private String codeStr = "0000";
    private String wipeStr = "wipe";

    /**
     * Called when the user taps the Send button
     */

    public void startQuiz(View view) {
        passcode = (EditText) findViewById(R.id.passcode);

        //get inserted text from EditText widget
        String passcodeStr = passcode.getText().toString();

        if (passcodeStr.equals(codeStr)) {
            //enter guardian mode...
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if(passcodeStr.equals(wipeStr)){
            //enter game mode
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.confirm_wipe);
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    Persistent.wipeAppdata();
                    Toast.makeText(MainActivity.this,"Question bank successfully wiped.",Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            //enter game mode
            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
            startActivity(intent);
        }

    }
}
