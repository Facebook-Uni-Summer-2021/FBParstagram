package com.example.fbparstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbparstagram.adapters.CommentsAdapter;
import com.example.fbparstagram.databinding.ActivityDetailPostBinding;
import com.example.fbparstagram.models.Comment;
import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailPostActivity extends AppCompatActivity {
    private static final String TAG = "DetailPostActivity";

    TextView tvUserName;
    TextView tvPostLikeCount;
    TextView tvPostDescription;
    TextView tvPostDate;
    ImageView ivImage;
    ImageView ivUserAvatar;
    ImageView ivPostLike;
    CardView cvPost;

    Post post;
    List<Comment> comments;
    CommentsAdapter adapter;
    RecyclerView rvComments;

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

        comments = new ArrayList<>();
        rvComments = findViewById(R.id.rvComments);
        adapter = new CommentsAdapter(this, comments);

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        //Create refresh layout

        //TODO: set post avatar

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

        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnReply");
                String message = binding.etComment.getText().toString();
                if (message.isEmpty()) {
                    Log.i(TAG, "message required");
                    Toast.makeText(DetailPostActivity.this,
                            "Message must have content.",
                            Toast.LENGTH_SHORT).show();
                }
                //Test comment
                Comment comment = new Comment();
                comment.setText(message);
                comment.setUser(ParseUser.getCurrentUser());
                comment.setPostedId(post.getObjectId());
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error: " + e);
                            return;
                        }
                        Log.i(TAG, "Comment saved");
                        binding.etComment.setText("");

                        Log.i(TAG, "Username: " + comment.getUser().getUsername());
                        Log.i(TAG, "COmment: " + comment.getText());

                        //Refresh comments

                    }
                });

            }
        });

        //Set post image onClick to hide and unhide image

        queryComments();
    }

    private void queryComments() {
        //Consider changing design of comments; also think about updating recview when
        // new comment is made
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo("postedId", post.getObjectId());
        query.include(Comment.KEY_USER);
        //No limit...yet :(
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> results, ParseException e) {
                if (e != null) {
                    //Handle error with retrieving posts
                    Log.e(TAG, "Issues with pulling: ", e);
                    return;
                }
                for (Comment comment : results) {
                    Log.i(TAG, comment.getUser().getUsername() +
                            " says: " + comment.getText());
                }
                adapter.clear();
                comments.clear();
                comments.addAll(results);
                adapter.notifyDataSetChanged();
                //srPosts.setRefreshing(false);
                //scrollListener.resetState();
            }
        });
    }
}