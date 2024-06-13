package com.example.kosovo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.FbReactions;
import com.amrdeveloper.reactbutton.ReactButton;
import com.bumptech.glide.Glide;
import com.example.kosovo.R;
import com.example.kosovo.data.remote.ApiClient;
import com.example.kosovo.ui.postupload.EditPostActivity;
import com.example.kosovo.helper.DataFormatter;
import com.example.kosovo.entity.post.PostsItem;
import com.example.kosovo.entity.reaction.Reaction;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<PostsItem> postsItems;

    public interface IUpdateUserReaction {
        void updateUserReaction(String uid, int postId, String postOwnerId, String previousReactionType,
                                String newReactionType,
                                int adapterPosition);
    }

    public interface IDeletePost {
        void deletePost(String postId, String postUserId, int adapterPosition);
    }

    IUpdateUserReaction iUpdateUserReaction;

    IDeletePost iDeletePost;

    public PostAdapter(Context context, List<PostsItem> postsItems) {
        this.context = context;
        this.postsItems = postsItems;
        if (context instanceof IUpdateUserReaction) {
            iUpdateUserReaction = (IUpdateUserReaction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IUpdateUserReaction");
        }
        if (context instanceof IDeletePost) {
            iDeletePost = (IDeletePost) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IDeletePost");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostsItem postsItem = postsItems.get(position);
        holder.peopleName.setText(postsItem.getName());

        String formattedDate = DataFormatter.getFormattedDate(postsItem.getStatusTime());
        holder.date.setText(formattedDate);
//////////////////////////
        String uid = FirebaseAuth.getInstance().getUid();

        if (uid.equals(postsItem.getPostUserId())) {
//            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.moreOptions.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, holder.moreOptions);
                popupMenu.inflate(R.menu.post_options_menu);
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.action_edit) {
                        Intent intent = new Intent(context, EditPostActivity.class);
                        intent.putExtra("postId", postsItem.getPostId());
                        intent.putExtra("post",postsItem.getPost());
                        intent.putExtra("postUserId", postsItem.getPostUserId());
                        intent.putExtra("statusImage", postsItem.getStatusImage());
                        intent.putExtra("privacy", postsItem.getPrivacy());
                        context.startActivity(intent);
                        return true;
                    } else {
                        holder.deletePost(postsItem);
                        return true;
                    }
                });
                popupMenu.show();
            });
        } else {
            holder.moreOptions.setVisibility(View.GONE);
        }

///////////////////////
        if (postsItem.getPrivacy() == 0) {
            holder.privacyIcon.setImageResource(R.drawable.icon_friends);
        } else if (postsItem.getPrivacy() == 1) {
            holder.privacyIcon.setImageResource(R.drawable.icon_only_me);
        } else {
            holder.privacyIcon.setImageResource(R.drawable.icon_public);
        }
        int reactionCountValuer = 0;

        reactionCountValuer = postsItem.getLikeCount() + postsItem.getLoveCount() +
                postsItem.getCareCount() + postsItem.getHahaCount() +
                postsItem.getWowCount() + postsItem.getSadCount() +
                postsItem.getAngryCount();

        if (reactionCountValuer == 0 || reactionCountValuer == 1) {
            holder.reactionCounter.setText(reactionCountValuer + " Reaction");
        } else {
            holder.reactionCounter.setText(reactionCountValuer + " Reactions");
        }
        holder.reactButton.setCurrentReaction(FbReactions.getReaction(postsItem.getReactionType()));
        String profileImage = postsItem.getProfileUrl();
        if (!postsItem.getProfileUrl().isEmpty()) {
            if (Uri.parse(postsItem.getProfileUrl()).getAuthority() == null) {
                profileImage = ApiClient.BASE_URL + postsItem.getProfileUrl();
            }
            Glide.with(context).load(profileImage).placeholder(R.drawable.default_profile_placeholder).into(holder.peopleImage);

        }
        if (!postsItem.getStatusImage().isEmpty()) {
            holder.statusImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(ApiClient.BASE_URL + postsItem.getStatusImage()).placeholder(R.drawable.default_profile_placeholder).into(holder.statusImage);
        } else {
            holder.statusImage.setVisibility(View.GONE);
        }
        if (postsItem.getPost().isEmpty()) {
            holder.post.setVisibility(View.GONE);
        } else {
            holder.post.setVisibility(View.VISIBLE);
            holder.post.setText(postsItem.getPost());
        }

        holder.sharePost.setOnClickListener(v -> {
            String postContent = postsItem.getPost();
            String postTitle = "Check out this post";
            String postCreatorName = postsItem.getName();

            String shareBody = "Check this post from " + postCreatorName + " on Kosovita social app:\n\n" + postContent;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, postTitle);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            context.startActivity(Intent.createChooser(shareIntent, "Share post using..."));
        });
    }

    @Override
    public int getItemCount() {
        return postsItems.size();
    }

    public void updatePostAfterReaction(int adapterPosition, Reaction reaction) {
        PostsItem postsItem = postsItems.get(adapterPosition);
        postsItem.setLikeCount(reaction.getLikeCount());
        postsItem.setLoveCount(reaction.getLoveCount());
        postsItem.setCareCount(reaction.getCareCount());
        postsItem.setHahaCount(reaction.getHahaCount());
        postsItem.setWowCount(reaction.getWowCount());
        postsItem.setSadCount(reaction.getSadCount());
        postsItem.setAngryCount(reaction.getAngryCount());
        postsItem.setReactionType(reaction.getReactionType());

        postsItems.set(adapterPosition, postsItem);
        notifyItemChanged(adapterPosition, postsItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ShapeableImageView peopleImage;
        TextView peopleName;
        TextView date;
        ImageView privacyIcon;
        TextView post;
        ImageView statusImage;
        ReactButton reactButton;
        TextView reactionCounter;
        ImageView moreOptions;
        LinearLayout sharePost;
//        Button deleteButton;
//        Button editButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            peopleImage = itemView.findViewById(R.id.people_image);
            peopleName = itemView.findViewById(R.id.people_name);
            date = itemView.findViewById(R.id.date);
            privacyIcon = itemView.findViewById(R.id.privacy_icon);
            post = itemView.findViewById(R.id.post);
            statusImage = itemView.findViewById(R.id.status_image);
            reactButton = itemView.findViewById(R.id.reaction);
            reactionCounter = itemView.findViewById(R.id.reactionCounter);
            moreOptions = itemView.findViewById(R.id.more_options);
            sharePost = itemView.findViewById(R.id.share);
//            deleteButton = itemView.findViewById(R.id.deleteButton);
//            editButton = itemView.findViewById(R.id.editButton);
//
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    PostsItem post = postsItems.get(getAdapterPosition());
//                    iDeletePost.deletePost(String.valueOf(post.getPostId()), post.getPostUserId(), getAdapterPosition());
//                }
//            });

            reactButton.setReactClickListener(this);
            reactButton.setReactDismissListener(this);
        }
        public void deletePost(PostsItem post) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                PostsItem postsItem = postsItems.get(adapterPosition);
                iDeletePost.deletePost(String.valueOf(postsItem.getPostId()), postsItem.getPostUserId(), adapterPosition);
            }
        }

        public void bind(PostsItem posts) {
            post.setText(posts.getPost());
            if (posts.getStatusImage() != null && !posts.getStatusImage().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(posts.getStatusImage())
                        .into(statusImage);
                statusImage.setVisibility(View.VISIBLE);
            } else {
                statusImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            onReactionChanged(v);
        }

        @Override
        public boolean onLongClick(View v) {
            onReactionChanged(v);
            return false;
        }

        private void onReactionChanged(View v) {
            String previousReactionType = postsItems.get(getAdapterPosition()).getReactionType();
            String newReactionType = ((ReactButton) v).getCurrentReaction().getReactType();
            if (!previousReactionType.contentEquals(newReactionType)) {
                iUpdateUserReaction.updateUserReaction(
                        FirebaseAuth.getInstance().getUid(),
                        postsItems.get(getAdapterPosition()).getPostId(),
                        postsItems.get(getAdapterPosition()).getPostUserId(),
                        previousReactionType,
                        newReactionType,
                        getAdapterPosition()
                );

            }
        }

    }
}
