package com.example.fbparstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.fbparstagram.fragments.UserFragment;
import com.example.fbparstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

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
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private static final String TAG = "PostsAdapter";

    FragmentManager fragmentManager;
    Context context;
    List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
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

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvPostLikeCount;
        TextView tvPostDescription;
        TextView tvPostDate;
        ImageView ivImage;
        ImageView ivPostUserAvatar;
        ImageView ivPostLike;
        ImageView ivComment;
        CardView cvPost;
        BottomNavigationView navigationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvCommentUserName);
            tvPostLikeCount = itemView.findViewById(R.id.tvPostLikeCount);
            tvPostDescription = itemView.findViewById(R.id.tvCommentText);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivPostUserAvatar = itemView.findViewById(R.id.ivCommentUserAvatar);
            ivPostLike = itemView.findViewById(R.id.ivPostLike);
            ivComment = itemView.findViewById(R.id.ivComment);
            navigationView = itemView.findViewById(R.id.bottom_navigation);
            cvPost = itemView.findViewById(R.id.cvPost);
            tvPostDate = itemView.findViewById(R.id.tvCommentDate);
        }

        public void bind(Post post) {
            tvUserName.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));
            //Assuming the data format remains the same, I split the String of Data by spaces
            String[] date = post.getCreatedAt().toString().split(" ");
            String dateContent = date[1] + " " + date[2] + " " + date[date.length - 1];
            Log.i(TAG, "date: " + dateContent);
            tvPostDate.setText(dateContent);

            //Set post image
            //Glide.with(context).load(post.getImage()).into(ivImage);
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            //Set post avatar
            ParseFile avatar = post.getUser().getParseFile("avatar");
            if (avatar != null) {
                Glide.with(context)
                        .load(avatar.getUrl())
                        //.centerCrop()
                        .transform(new CircleCrop())
                        .into(ivPostUserAvatar);
            } else {
                Glide.with(context)
                        .load(context.getDrawable(R.drawable.instagram_user_filled_24))
                        .transform(new CircleCrop())
                        .into(ivPostUserAvatar);
            }
            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30,30);
            //ivPostUserAvatar.setLayoutParams(new RelativeLayout.LayoutParams(100,100));

            if (post.getIsLiked()) {
                ivPostLike.setImageDrawable(context.getDrawable(R.drawable.ufi_heart_active));
            } else {
                ivPostLike.setImageDrawable(context.getDrawable(R.drawable.ufi_heart));
            }

            ivPostLike.setOnClickListener(new View.OnClickListener() {
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
                                tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));
                                //Set the heart to empty
                                ivPostLike.setImageDrawable(context.getDrawable(R.drawable.ufi_heart));
                            }
                        });
                    } else {
                        post.setIsLiked(true);
                        post.setLikeCount(post.getLikeCount() + 1);
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                //Set the count text to current +1
                                tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));
                                //Set the heart to full
                                ivPostLike.setImageDrawable(context.getDrawable(R.drawable.ufi_heart_active));
                            }
                        });
                    }
                }
            });

            ivPostUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Avatar clicked");
                    //FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new UserFragment(post.getUser());
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            });

            cvPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Clicked on post");
                    //Direct to detail activity for comments
                    Intent intent = new Intent(context, DetailPostActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });

            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Clicked on comment");
                    //Direct to detail activity for comments
                    Intent intent = new Intent(context, DetailPostActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });
        }
    }

}
