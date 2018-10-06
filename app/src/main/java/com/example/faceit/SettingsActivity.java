package com.example.faceit;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Timestamp;

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
        image = (ImageView)findViewById(R.id.imageToUpload);
        browseImageBtn = (Button)findViewById(R.id.browseImageBtn);
        uploadPhotoBtn = (Button)findViewById(R.id.uploadImageBtn);
        editPhotoName = (EditText)findViewById(R.id.editUploadName);

        image.setOnClickListener((View.OnClickListener) this);
        uploadPhotoBtn.setOnClickListener((View.OnClickListener) this);
        browseImageBtn.setOnClickListener((View.OnClickListener)this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.browseImageBtn:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.uploadImageBtn:
                uploadImageToInternalStorage(imageToUpload);
                Toast toast = Toast.makeText(getApplicationContext(), "Image Uploaded!", Toast.LENGTH_SHORT);
                toast.show();
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
            Bitmap imageToUpload = BitmapFactory.decodeFile(imagePath, options);

            //upload the bitmap
            uploadImageToInternalStorage(imageToUpload);

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
            Log.i("Image", "uploading image...");

        }
    }

    private void uploadImageToInternalStorage(Bitmap bitmapImage){
        //insert code here

    }
}
