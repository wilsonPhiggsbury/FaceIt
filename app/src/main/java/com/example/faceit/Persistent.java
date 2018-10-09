package com.example.faceit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;

public final class Persistent {
    private static JSONObject questionBankJSON = null;
    private static Context currentContext = null;
    public static void setContext(Context context)
    {
        currentContext = context;
    }
    private static JSONObject readJSONFromStorage() throws JSONException, IOException{
        // Store JSONObject as string, retrieve string and cast back into JSONObject
        JSONObject json = null;
        File theJSONFile = new File(currentContext.getFilesDir(), currentContext.getString(R.string.question_bank));
        try {
            FileInputStream questionBankReader = new FileInputStream(theJSONFile);
            byte[] rawBytes = new byte[questionBankReader.available()];
            questionBankReader.read(rawBytes);
            questionBankReader.close();
            String JSONString = new String(rawBytes, "UTF-8");
            json = new JSONObject(JSONString);
            Log.d("JSON loaded:", json.toString());
        } catch (FileNotFoundException e) {
            // attempt to create new file.
            json = new JSONObject("{}");
            writeJSONToStorage(json);
            Log.d("JSON created:", json.toString());
        }
        return json;
    }
    private static void writeJSONToStorage(JSONObject json) throws IOException
    {
        File theJSONFile = new File(currentContext.getFilesDir(), currentContext.getString(R.string.question_bank));
        FileOutputStream questionBankWriter = new FileOutputStream(theJSONFile);
        byte[] rawBytes = json.toString().getBytes("UTF8");
        questionBankWriter.write(rawBytes);
        questionBankWriter.close();
        Log.d("Persistent.java","Written JSON String - "+json.toString());
    }
    public static void saveImageToStorage(String fileName, Bitmap imageToSave) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(currentContext.getFilesDir(), fileName+".jpg"));
        imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);

        out.close();
    }
    private static Bitmap loadImageFromStorage(String fileName)
    {
        try {
            FileInputStream in = new FileInputStream(new File(currentContext.getFilesDir(), fileName+".jpg"));
            Bitmap face = BitmapFactory.decodeStream(in);
            in.close();
            return face;
        }
        catch (IOException e)
        {
            Log.e("Persistent.java","Bitmap for "+fileName+" could not be loaded!");
            return null;
        }
    }
    public static void saveEntryToStorage(String fileName, String personName, Relationship relation) throws IOException,JSONException
    {
        if(currentContext == null)
        {
            Log.e("FaceIt Guardian Mode","Accessing Persistent.java methods without first giving context.");
            return;
        }
        if(questionBankJSON == null)
        {
            questionBankJSON = readJSONFromStorage();
        }
        JSONObject innerObj = new JSONObject("{\"name\":"+personName+", \"relation\":"+"1"+"}");
        questionBankJSON.put(fileName, innerObj);
        writeJSONToStorage(questionBankJSON);
        Log.d("FaceIt Guardian Mode", "Successfully written entry "+fileName+".jpg as name \""+personName+"\"");
    }
    public static ArrayList<Question> loadEntriesFromStorage() throws IOException,JSONException
    {
        ArrayList<Question> questions = new ArrayList<>();
        if(questionBankJSON == null)
        {
            questionBankJSON = readJSONFromStorage();
        }
        Iterator<String> keys = questionBankJSON.keys();
        while(keys.hasNext())
        {
            String key = keys.next();
            Object attributes = questionBankJSON.get(key);
            Bitmap face = loadImageFromStorage(key);
            if(face != null && attributes instanceof JSONObject)
            {
                JSONObject attributesJSON = (JSONObject)attributes;
                questions.add(new Question(attributesJSON.get("name").toString(), face));
            }
        }
        return questions;
    }
    public static void wipeAppdata()
    {
        File[] files = currentContext.getFilesDir().listFiles();
        if(files != null)
            for(File file : files) {
                Log.d("Persistent.java","Deleting file "+file.getName());
                file.delete();
            }

    }
}
