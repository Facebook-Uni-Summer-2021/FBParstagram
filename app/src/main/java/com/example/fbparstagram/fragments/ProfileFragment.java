package com.example.fbparstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.fbparstagram.R;
import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends PostsViewFragment {
    private static final String TAG = "ProfileFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 20;

    private File photoFile;
    public String photoFileName = "photo.jpg";

    ImageView ivUserAvatar;
    TextView tvAccountName;
    ParseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();
        ivUserAvatar = view.findViewById(R.id.ivUserAvatar);
        tvAccountName = view.findViewById(R.id.tvAccountName);

        tvAccountName.setText(user.getUsername());
        //Log.i(TAG, "Here: " + user.getParseFile("avatar"));

        //ParseFile temp = ParseUser.getCurrentUser().getParseFile("avatar");
        //Log.i(TAG, "Avatar urL: " + temp.getUrl());

        ParseFile avatar = user.getParseFile("avatar");
        //Log.i(TAG, "Avatar urL: " + avatar.getUrl());
        if (avatar != null) {
            Glide.with(this).load(avatar.getUrl()).into(ivUserAvatar);
        } else {
            Glide.with(this).load(android.R.drawable.ic_menu_report_image).into(ivUserAvatar);
        }

        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the camera
                Log.i(TAG, "Launching camera");
                /*
                Potentially create a modal similar to the Twitter compose
                to create a nicer profile image thingy.
                 */
                launchCamera();
            }
        });

        super.onViewCreated(view, savedInstanceState);

//        ParseFile file = posts.get(0).getUser().getParseFile("avatar");
//        Log.i(TAG, "Url: " + file.getUrl());
    }

    @Override
    protected void queryPosts() {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);

        //IMPORTANT! Distinction between PostsViewFragment
        // and ProfileFragment
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        //I don't know why, but we need to limit pull
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> results, ParseException e) {
                //Get all Post objects and a ParseException
                if (e != null) {
                    //Handle error with retrieving posts
                    Log.e(TAG, "Issues with pulling: ", e);
                    return;
                }
                for (Post post: results) {
                    Log.i(TAG, post.getUser().getUsername() +
                            " says: " + post.getDescription());
                }
                adapter.clear();
                posts.clear();
                posts.addAll(results);
                adapter.notifyDataSetChanged();
                srPosts.setRefreshing(false);
            }
        });
    }

    //I copied this to take a profile picture, but for less redundancy this could
    // be put into a separate class to be called.

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
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent
        // that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use
        // the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent,
                    CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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
                new File(getContext().getExternalFilesDir(
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
     * Was a bit finicky to get; deals with results from
     * returning from intent.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivUserAvatar.setImageBitmap(takenImage);
                user.put("avatar", new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        //Pic updated?
                        queryPosts();
                    }
                });
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
