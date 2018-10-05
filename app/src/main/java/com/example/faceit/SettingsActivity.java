package com.example.faceit;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private JSONObject questionBankJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            questionBankJSON = Persistent.readJSONFromStorage(getApplicationContext());
            //questionBankJSON.put(Long.toHexString(new Date().getTime()), "Hi");
            //Persistent.writeJSONToStorage(getApplicationContext(), questionBankJSON);
        } catch (JSONException e) {
            Log.e("JSON Exception!", "JSON conversion from string fail");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("IO Exception!", "IO for read fail");
            e.printStackTrace();
        }
    }
}
