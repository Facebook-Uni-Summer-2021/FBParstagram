package com.example.fbparstagram.fragments;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfilePostsViewFragment extends PostsViewFragment {
    private static final String TAG = "ProfilePostsViewFragment";

    ParseUser user;
    int position;

    public ProfilePostsViewFragment (ParseUser user, int position) {
        this.user = user;
        this.position = position;
    }

    protected void queryPosts() {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);//ParseUser.getCurrentUser()
        //I don't know why, but we need to limit pull
        query.setLimit(10);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @SuppressLint("LongLogTag")
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
                //scrollListener.resetState();
                //rvPosts.scrollToPosition(position);
                rvPosts.smoothScrollToPosition(position);
            }
        });
    }

    protected void queryPostsSkip(int skip) {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);//ParseUser.getCurrentUser()
        //I don't know why, but we need to limit pull
        query.setLimit(10);
        query.setSkip(skip);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @SuppressLint("LongLogTag")
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
                //adapter.clear();
                //posts.clear();
                posts.addAll(results);
                adapter.notifyDataSetChanged();
                srPosts.setRefreshing(false);
                //Bugged out my RecView when in range of ScrollView's visibleThreshold
                //scrollListener.resetState();
                //rvPosts.scrollToPosition(position);
                rvPosts.smoothScrollToPosition(position);
            }
        });
    }
}
