package com.example.fbparstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.fbparstagram.MainActivity;
import com.example.fbparstagram.R;
import com.example.fbparstagram.fragments.ComposeFragment;
import com.example.fbparstagram.fragments.PostsViewFragment;
import com.example.fbparstagram.fragments.ProfileFragment;
import com.example.fbparstagram.fragments.UserFragment;
import com.example.fbparstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
        ImageView ivPostUserAvatar;
        ImageView ivPostLike;
        CardView cvPost;
        BottomNavigationView navigationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPostLikeCount = itemView.findViewById(R.id.tvPostLikeCount);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivPostUserAvatar = itemView.findViewById(R.id.ivPostUserAvatar);
            ivPostLike = itemView.findViewById(R.id.ivPostLike);
            navigationView = itemView.findViewById(R.id.bottom_navigation);
            cvPost = itemView.findViewById(R.id.cvPost);
        }

        public void bind(Post post) {
            tvUserName.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            tvPostLikeCount.setText(String.valueOf(post.getLikeCount()));

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

            ivPostLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Like the post!!!");
                    //Will I need to do Parse here for liking?
                }
            });

            ivPostUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Avatar clicked");
                    //FragmentManager fragmentManager = getSupportFragmentManager();

                    Fragment fragment = new UserFragment(post.getUser());
                    //Android suggests to avoid switch on menu
//                            if (item.getItemId() == R.id.action_home) {
//                                Log.i(TAG, "To home");
//                                fragment = new PostsViewFragment();
//                            } else if (item.getItemId() == R.id.action_compose) {
//                                Log.i(TAG, "To compose");
//                                fragment = new ComposeFragment();
//                            } else if (item.getItemId() == R.id.action_profile) {
//                                Log.i(TAG, "To profile");
//                                fragment = new ProfileFragment();
//                            } else
//                                //Default fragment
//                                fragment = new PostsViewFragment();
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

//                    navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//                        @Override
//                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                            return true;
//                        }
//                    });
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
        }
    }

}
