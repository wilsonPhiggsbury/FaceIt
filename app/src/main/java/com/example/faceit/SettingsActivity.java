package com.example.faceit;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private JSONObject questionBankJSON;
    private static final int RESULT_LOAD_IMAGE = 1; //id for the image selected to upload
    private Button browseImageBtn;
    private Button uploadPhotoBtn;
    private ImageView image;
    private EditText editPhotoName;
    private static Bitmap imageToUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        image = (ImageView)findViewById(R.id.imageToUpload);
        browseImageBtn = (Button)findViewById(R.id.browseImageBtn);
        uploadPhotoBtn = (Button)findViewById(R.id.uploadImageBtn);
        editPhotoName = (EditText)findViewById(R.id.editUploadName);

        image.setOnClickListener((View.OnClickListener) this);
        uploadPhotoBtn.setOnClickListener((View.OnClickListener) this);
        browseImageBtn.setOnClickListener((View.OnClickListener)this);

        requestPermission();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.browseImageBtn:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.uploadImageBtn:
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
                String imgFileName = dateFormat.format(new Date());

                try {
                    if(imageToUpload == null)
                        Log.e("FaceIt Guardian Mode","Attempting to store NULL image!");
                    else if(editPhotoName.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please input name of person in photo!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Persistent.saveImageToStorage(imgFileName, imageToUpload);
                        Persistent.saveEntryToStorage(imgFileName, editPhotoName.getText().toString(), null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    /*
     * Callback when user select image from gallery for upload
     * get a callback
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data (not null) we know that an image was picked.
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){
            //display the picture selected
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);

            //set the photo to a bitmap
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            imageToUpload = BitmapFactory.decodeFile(imagePath, options);
            if(imageToUpload == null)
                Log.e("FaceIt Guardian Mode","Image just returned null from path "+imagePath);
            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

        }
    }

    private void requestPermission(){
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
        if(!granted)
        {
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //                    grantResult[0] means it will check for the first postion permission which is READ_EXTERNAL_STORAGE
                    //                    grantResult[1] means it will check for the Second postion permission which is CAMERA
                    Log.d("FaceIt Guardian Mode","Storage permissions Granted.");
                } else
                {
                    Log.d("FaceIt Guardian Mode","Storage permissions Rejected!");
                    Toast.makeText(this, "Please allow access to the gallery.", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                return;
            }

        }

    }
}
