package com.example.fbparstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;

    private File photoFile;
    public String photoFileName = "photo.jpg";

    EditText etDesc;
    Button btnCaptureImage;
    Button btnSubmit;
    ImageView ivPostImage;

    /**
     * Creates an activity to allow posts to be created
     * and uplaoded to Parse/Back4App.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etDesc = findViewById(R.id.etDesc);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivPostImage);

        //queryPosts();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDesc.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(ComposeActivity.this,
                            "Description cannot be empty!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(ComposeActivity.this,
                            "This is Instagram, you HAVE to have a photo!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //Get current user
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });

        ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }

    /**
     * Helper method to launch the device's camera.
     */
    private void launchCamera() {
        //Create Intent to take a picture and return control
        // to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        //wrap File object into a content provider
        //required for API >= 24
        //See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(ComposeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent
        // that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use
        // the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent,
                    CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * Was a bit finicky to get; deals with results from
     * returning from intent.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Gets a photo's location in a device.
     * @param fileName The photo's name.
     * @return The photo.
     */
    public File getPhotoFileUri(String fileName) {
        //Get safe storage directory for photos
        //Use `getExternalFilesDir` on Context to access
        // package-specific directories.
        //This way, we don't need to request external read/write
        // runtime permissions.
        File mediaStorageDir =
                new File(getExternalFilesDir(
                        Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        //File file = new File(mediaStorageDir.getPath() +
        //File.separator + fileName);

        return new File(mediaStorageDir.getPath() +
                File.separator + fileName);
    }

    /**
     * Saves a created post to Parse/Back4App
     * @param description Post decsription.
     * @param currentUser Current user.
     */
    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));

        //Always use ParseObject.method(CallBack), where
        // callback allows us to handle errors
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving: ", e);
                    Toast.makeText(ComposeActivity.this,
                            "Something went wrong while saving",
                            Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "Post saved");
                //Clear out text
                etDesc.setText("");
                ivPostImage.setImageDrawable(getDrawable(android.R.drawable.ic_menu_camera));
                finish();
            }
        });
    }
}
