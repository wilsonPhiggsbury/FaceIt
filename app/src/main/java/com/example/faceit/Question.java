package com.example.faceit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public final class Question {
    private String name;
    private Bitmap image;
    public Question(String name, Bitmap face)
    {
        // bitmaps are saved as their ID, which is the formatted date object specified in Persistent.java
        // to avoid collisions
        this.name = name;
        image = face;
    }
    @Override
    public String toString()
    {
        return "["+name+":"+image.toString()+"]\n";
    }
    public String getName()
    {
        return name;
    }
    public void putImageTo(ImageView faceImg)
    {
        faceImg.setImageBitmap(image);
    }
}
