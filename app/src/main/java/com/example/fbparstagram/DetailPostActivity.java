package com.example.fbparstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbparstagram.databinding.ActivityDetailPostBinding;
import com.example.fbparstagram.models.Post;

import org.parceler.Parcels;

public class DetailPostActivity extends AppCompatActivity {
    private static final String TAG = "DetailPostActivity";

    Post post;

    TextView tvUserName;
    TextView tvPostLikeCount;
    TextView tvPostDescription;
    ImageView ivImage;
    ImageView ivUserAvatar;
    ImageView ivPostLike;
    CardView cvPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailPostBinding binding = ActivityDetailPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        //I did not include the image as a took too much space;
        // to mimic Instagram, the detail view only has the description and comments
        binding.tvUserName.setText(post.getUser().getUsername());
        binding.tvPostDescription.setText(post.getDescription());
        binding.tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));

        //Set post avatar
        binding.ivPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Like the post!!!");
                //Will I need to do Parse here for liking?
            }
        });
    }
}