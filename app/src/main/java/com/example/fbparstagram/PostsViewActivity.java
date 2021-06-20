package com.example.fbparstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fbparstagram.adapters.PostsAdapter;
import com.example.fbparstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Instagram timeline, where you can view
 * all posts made and saved in Parse/Back4App
 */
public class PostsViewActivity extends AppCompatActivity {
    private static final String TAG = "PostsViewActivity";

    BottomNavigationView navigationView;
    SwipeRefreshLayout srPosts;
    PostsAdapter adapter;
    RecyclerView rvPosts;
    List<Post> posts;

    /**
     * Defines RecyclerView information.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_view);
        Log.i(TAG, "in PostsViewActivity");

        //Define bottomnavigation
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                //Android suggests to avoid switch on menu
                if (item.getItemId() == R.id.action_home) {
                    Log.i(TAG, "To home");
                } else if (item.getItemId() == R.id.action_compose) {
                    Log.i(TAG, "To compose");
                } else if (item.getItemId() == R.id.action_profile) {
                    Log.i(TAG, "To profile");
                }
                return true;
            }
        });

        //Create SwipeRefreshListener
        srPosts = findViewById(R.id.srPosts);
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
        rvPosts = findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(this, posts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        //Get posts
        queryPosts();
    }

    /**
     * Create menu and menu items.
     * @param menu The menu.
     * @return Modified menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Define meu items
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle clicking of items in menu.
     * @param item The items in the menu.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_compose) {
            //Open new compose activity
            Intent intent = new Intent(PostsViewActivity.this, ComposeActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mSignOut) {
            //Sign out of Parse/Back4App
            ParseUser.logOut();
            //ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent intent = new Intent(this, LoginActivity.class);
            //New things to close current activity to return to login
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reads all of the posts in Parse/Back4App/DB
     */
    private void queryPosts() {
        //Get the actual data from parse using the object/model
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //Get extra, specified info
        query.include(Post.KEY_USER);
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