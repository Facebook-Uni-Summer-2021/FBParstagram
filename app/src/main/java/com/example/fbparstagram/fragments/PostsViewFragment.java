package com.example.fbparstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fbparstagram.EndlessRecyclerViewScrollListener;
import com.example.fbparstagram.R;
import com.example.fbparstagram.adapters.PostsAdapter;
import com.example.fbparstagram.models.Comment;
import com.example.fbparstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class PostsViewFragment extends Fragment {
    private static final String TAG = "PostsViewFragment";

    EndlessRecyclerViewScrollListener scrollListener;
    SwipeRefreshLayout srPosts;
    PostsAdapter adapter;
    RecyclerView rvPosts;
    List<Post> posts;
    //int skip;

    public PostsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        posts = new ArrayList<>();
        rvPosts = view.findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(getContext(), posts, getParentFragmentManager());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(manager);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore - page: " + page + ", total: " + totalItemsCount);
                queryPostsSkip(totalItemsCount);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);

        //Get posts
        queryPosts();
    }

//    protected void setScroll () {
//
//    }

    /**
     * Reads all of the posts in Parse/Back4App/DB; due to
     * the similarity of PostsViewFragment and ProfileFragment,
     * PostsViewFragment will be the superclass of ProfileFragment
     * to avoid repetitive code.
     */
    protected void queryPosts() {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);
        //I don't know why, but we need to limit pull
        query.setLimit(10);
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
                //scrollListener.resetState();
            }
        });
    }

    protected void queryPostsSkip(int skip) {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);
        //I don't know why, but we need to limit pull
        query.setLimit(10);
        query.setSkip(skip);
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
                //adapter.clear();
                //posts.clear();
                posts.addAll(results);
                adapter.notifyDataSetChanged();
                srPosts.setRefreshing(false);
                //Bugged out my RecView when in range of ScrollView's visibleThreshold
                //scrollListener.resetState();
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        queryPosts();
//    }
}