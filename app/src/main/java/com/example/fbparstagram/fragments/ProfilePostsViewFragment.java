package com.example.fbparstagram.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbparstagram.EndlessRecyclerViewScrollListener;
import com.example.fbparstagram.R;
import com.example.fbparstagram.adapters.PostsAdapter;
import com.example.fbparstagram.adapters.ProfilePostsAdapter;
import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostsViewFragment extends PostsViewFragment {
    private static final String TAG = "ProfilePostsViewFragment";

    ParseUser user;
    List<Post> posts;
    int position;

    //Use posts to generate recview to avoid having to query with skip
    public ProfilePostsViewFragment (int position, ParseUser user, List<Post> posts) {
        this.position = position;
        this.user = user;
        this.posts = posts;
    }
    @SuppressLint("LongLogTag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "OnCreate");

        //Create SwipeRefreshListener
        srPosts = view.findViewById(R.id.srPosts);
        srPosts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });

        //Set color of refresh symbol
        srPosts.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Set all RecView information
        //posts = new ArrayList<>();
        rvPosts = view.findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(getContext(), posts, getParentFragmentManager());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(manager);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @SuppressLint("LongLogTag")
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore - page: " + page + ", total: " + totalItemsCount);
                queryPostsSkip(totalItemsCount);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        adapter.notifyDataSetChanged();
        rvPosts.scrollToPosition(position);
        //Get posts
        //queryPosts();
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
                //Scrolling up causes lag to occur; using querySkip also caused issues
                //rvPosts.scrollToPosition(position);

                //rvPosts.smoothScrollToPosition(position);
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
                //rvPosts.smoothScrollToPosition(position);
            }
        });
    }
}
