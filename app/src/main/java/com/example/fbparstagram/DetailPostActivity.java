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
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.Date;

public class DetailPostActivity extends AppCompatActivity {
    private static final String TAG = "DetailPostActivity";

    Post post;

    TextView tvUserName;
    TextView tvPostLikeCount;
    TextView tvPostDescription;
    TextView tvPostDate;
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

        //Assuming the data format remains the same, I split the String of Data by spaces
        String[] date = post.getCreatedAt().toString().split(" ");
        String dateContent = date[1] + " " + date[2] + " " + date[date.length - 1];
        Log.i(TAG, "date: " + dateContent);
        binding.tvPostDate.setText(dateContent);
        //Log.i(TAG, "Date: "  + date);
        //Log.i(TAG, "Month: " + date.getMonth() + ", Year: " + date.getYear());

        //Set post avatar


        if (post.getIsLiked()) {
            binding.ivPostLike.setImageDrawable(getDrawable(R.drawable.ufi_heart_active));
        } else {
            binding.ivPostLike.setImageDrawable(getDrawable(R.drawable.ufi_heart));
        }

        binding.ivPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Like the post!!!");
                //Will I need to do Parse here for liking?
                if (post.getIsLiked()) {
                    post.setIsLiked(false);
                    if (post.getLikeCount() > 0) {
                        post.setLikeCount(post.getLikeCount() - 1);
                    }
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Set the count text to current -1
                            binding.tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));
                            //Set the heart to empty
                            binding.ivPostLike.setImageDrawable(getDrawable(R.drawable.ufi_heart));
                        }
                    });
                } else {
                    post.setIsLiked(true);
                    post.setLikeCount(post.getLikeCount() + 1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Set the count text to current +1
                            binding.tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));
                            //Set the heart to full
                            binding.ivPostLike.setImageDrawable(getDrawable(R.drawable.ufi_heart_active));
                        }
                    });
                }
            }
        });
    }
}