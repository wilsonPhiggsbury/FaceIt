package com.example.faceit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public final class Question {
    private String name;
    private Bitmap image;
    public Question(Context context, String id, String name)
    {
        // bitmaps are saved as their ID, which is the ms since POSIX time to avoid collisions
        this.name = name;
        image = loadImageFromStorage(context.getFilesDir(), id);
    }
    private Bitmap loadImageFromStorage(File path, String fileName)
    {
        File f = new File(path, fileName);
        try {
            FileInputStream in = new FileInputStream(f);
            Bitmap face = BitmapFactory.decodeStream(in);
            in.close();
            return face;
        }
        catch (IOException e)
        {
            Log.e("Bitmap error","Bitmap for "+fileName+" could not be loaded!");
            return null;
        }
    }
}
