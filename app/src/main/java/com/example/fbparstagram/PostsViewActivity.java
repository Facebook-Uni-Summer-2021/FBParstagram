package com.example.fbparstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class PostsViewActivity extends AppCompatActivity {
    private static final String TAG = "PostsViewActivity";

    PostsAdapter adapter;
    RecyclerView rvPosts;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_view);
        Log.i(TAG, "in PostsViewActivity");

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
                posts.addAll(results);
                adapter.notifyDataSetChanged();
            }
        });
    }
}