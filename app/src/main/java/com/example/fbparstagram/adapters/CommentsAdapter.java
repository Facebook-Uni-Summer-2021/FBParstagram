package com.example.fbparstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbparstagram.R;
import com.example.fbparstagram.models.Comment;
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
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    Context context;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCommentUserAvatar;
        TextView tvCommentUserName;
        TextView tvCommentText;
        TextView tvCommentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentUserAvatar = itemView.findViewById(R.id.ivCommentUserAvatar);
            tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
        }

        public void bind(Comment comment) {

            tvCommentUserName.setText(comment.getUser().getUsername());
            tvCommentText.setText(comment.getText());

            //Set post avatar
            ParseFile avatar = comment.getUser().getParseFile("avatar");
            if (avatar != null) {
                Glide.with(context)
                        .load(avatar.getUrl())
                        //.centerCrop()
                        .transform(new CircleCrop())
                        .into(ivCommentUserAvatar);
            } else {
                Glide.with(context)
                        .load(context.getDrawable(R.drawable.instagram_user_filled_24))
                        .transform(new CircleCrop())
                        .into(ivCommentUserAvatar);
            }
        }
    }
}
