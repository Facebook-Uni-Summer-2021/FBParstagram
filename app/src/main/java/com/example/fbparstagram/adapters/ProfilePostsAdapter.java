package com.example.fbparstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbparstagram.DetailPostActivity;
import com.example.fbparstagram.R;
import com.example.fbparstagram.fragments.PostsViewFragment;
import com.example.fbparstagram.fragments.UserFragment;
import com.example.fbparstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

//There must be a way to extend the PostsAdapter, but Im not so sure...
public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder> {
    private static final String TAG = "PostsAdapter";

    Context context;
    List<Post> posts;

    public ProfilePostsAdapter (Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        RelativeLayout rlPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            rlPost = itemView.findViewById(R.id.rlPost);
        }

        public void bind(Post post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            rlPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Clicked on post");
                    //Direct to detail activity for comments
                    Intent intent = new Intent(context, DetailPostActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });
        }
    }
}

/*
    Original contents, to override PostsAdapter
    public ProfilePostsAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        super(context, posts, fragmentManager);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_image, parent, false);
        return new ViewHolderA(view);
    }

    public class ViewHolderA extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public ViewHolderA(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(Post post) {

            //Set post image
            //Glide.with(context).load(post.getImage()).into(ivImage);
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30,30);
            //ivPostUserAvatar.setLayoutParams(new RelativeLayout.LayoutParams(100,100));
        }
    }
 */
