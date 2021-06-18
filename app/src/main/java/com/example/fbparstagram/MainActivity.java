package com.example.fbparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText etDesc;
    Button btnCaptureImage;
    Button btnSubmit;
    ImageView ivPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDesc = findViewById(R.id.etDesc);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivPostImage);

        queryPosts();
    }

    private void queryPosts() {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                //Get all Post objects and a ParseException
                if (e != null) {
                    //Handle error with retrieving posts
                    Log.e(TAG, "Issues with pulling: ", e);
                    return;
                }
                for (Post post: posts) {
                    Log.i(TAG, post.getUser().getUsername() + " says: " +
                            post.getDescription());
                }
            }
        });
    }
}