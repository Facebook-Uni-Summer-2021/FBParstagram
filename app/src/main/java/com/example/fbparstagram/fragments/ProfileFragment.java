package com.example.fbparstagram.fragments;

import android.util.Log;

import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsViewFragment {
    private static final String TAG = "ProfileFragment";

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
}
