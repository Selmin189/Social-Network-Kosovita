package com.example.kosovo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kosovo.R;
import com.example.kosovo.data.remote.ApiClient;
import com.example.kosovo.ui.profile.ProfileActivity;
import com.example.kosovo.entity.friend.Friend;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context context;
    private List<Friend> friendList;

    public FriendAdapter(Context context, List<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend item = friendList.get(position);
        holder.profileName.setText(item.getName());

        String image = "";

        if (Uri.parse(item.getProfileUrl()).getAuthority() == null){
            image = ApiClient.BASE_URL+item.getProfileUrl();
        }else {
            image = item.getProfileUrl();
        }
        Glide.with(context).load(image).placeholder(R.drawable.default_profile_placeholder).into(holder.profileImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("uid", item.getUid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView profileName;
        Button btnAcceptRequest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            profileName = itemView.findViewById(R.id.profile_name);
            btnAcceptRequest = itemView.findViewById(R.id.btn_accept);
            btnAcceptRequest.setVisibility(View.GONE);
        }
    }
}
