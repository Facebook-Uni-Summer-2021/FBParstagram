package com.example.fbparstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbparstagram.R;
import com.example.fbparstagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

/*
The following are steps to creating an adapter:
Step 1. Create a ViewHolder public class that extends
    RecyclerView.ViewHolder and define the widget variables
    using itemView
Step 2. Extend adapter class with
    RecyclerView.Adapter<[AdapterClass].ViewHolder> (implement methods)
Step 4. Define context and items list in adapter, then generate constructor
Step 5. Inflate view in onCreateViewHolder with
    "View view = LayoutInflater.from(context).inflate(R.layout.item_movie,
    parent, false);" and return as new ViewHolder
Step 6. In onBindViewHolder, create an item from the list of items
    (Item item = items.get(index)) and populate widget variables
    through holder.bind(item) (fix error by creating new method
    "bind()" in ViewHolder)
Step 7. Bind appropriate item model information to widget variables
    from ViewHolder
Step 8. Set getItemCount to size of items list

onCreateViewHolder - inflates an xml layout and return as ViewHolder
onBindViewHolder - populates data into view through ViewHolder
getItemCount - returns total items of items list
*/
public class PostsAdapter extends  RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private static final String TAG = "PostsAdapter";

    Context context;
    List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
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

    public void clear () {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll (List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvPostLikeCount;
        TextView tvPostDescription;
        ImageView ivImage;
        ImageView ivUserAvatar;
        ImageView ivPostLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPostLikeCount = itemView.findViewById(R.id.tvPostLikeCount);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            ivPostLike = itemView.findViewById(R.id.ivPostLike);
        }

        public void bind(Post post) {
            tvUserName.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());

            //Set post image
            //Glide.with(context).load(post.getImage()).into(ivImage);
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            //Set post avatar
            ivPostLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Like the post!!!");
                    //Will I need to do Parse here for liking?
                }
            });
        }
    }

}
