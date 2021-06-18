package com.example.fbparstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostsViewActivity extends AppCompatActivity {
    private static final String TAG = "PostsViewActivity";

    SwipeRefreshLayout srPosts;
    PostsAdapter adapter;
    RecyclerView rvPosts;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_view);
        Log.i(TAG, "in PostsViewActivity");

        srPosts = findViewById(R.id.srPosts);
        srPosts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });

        srPosts.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        posts = new ArrayList<>();
        rvPosts = findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(this, posts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        queryPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Define meu items
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mCompose) {
            //Open new compose activity
            Intent intent = new Intent(PostsViewActivity.this, ComposeActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mSignOut) {
            //Sign out of Parse/Back4App
            //ParseUser.logOut();
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