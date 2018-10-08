package com.example.faceit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.String;

public final class Persistent {
    private static JSONObject questionBankJSON;
    public static JSONObject readJSONFromStorage(Context context) throws JSONException, IOException{
        // Store JSONObject as string, retrieve string and cast back into JSONObject
        File theJSONFile = new File(context.getFilesDir(), context.getString(R.string.question_bank));
        try {
            FileInputStream questionBankReader = new FileInputStream(theJSONFile);
            byte[] rawBytes = new byte[questionBankReader.available()];
            questionBankReader.read(rawBytes);
            questionBankReader.close();
            String JSONString = new String(rawBytes, "UTF-8");
            questionBankJSON = new JSONObject(JSONString);
            Log.d("JSON loaded:", questionBankJSON.toString());
        } catch (FileNotFoundException e) {
            // attempt to create new file.
            questionBankJSON = new JSONObject("{}");
            writeJSONToStorage(context, questionBankJSON);
            Log.d("JSON created:", questionBankJSON.toString());
        }
        return questionBankJSON;
    }
    public static void writeJSONToStorage(Context context, JSONObject json) throws IOException
    {
        File theJSONFile = new File(context.getFilesDir(), context.getString(R.string.question_bank));
        FileOutputStream questionBankWriter = new FileOutputStream(theJSONFile);
        byte[] rawBytes = json.toString().getBytes("UTF8");
        questionBankWriter.write(rawBytes);
        questionBankWriter.close();
        Log.d("Written JSON :",json.toString());
    }
    public static void saveImageToStorage(File path, String fileName, Bitmap imageToSave) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(path, fileName));
        imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
    }
}
