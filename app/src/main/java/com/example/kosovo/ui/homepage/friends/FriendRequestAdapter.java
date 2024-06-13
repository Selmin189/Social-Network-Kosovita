package com.example.kosovo.ui.homepage.friends;

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
import com.example.kosovo.entity.friend.Request;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private Context context;
    private List<Request> requestList;

    private IPerformAction iPerformAction;

    public FriendRequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
        iPerformAction = (IPerformAction) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request item = requestList.get(position);
        holder.profileName.setText(item.getName());

        String image = "";

        if (Uri.parse(item.getProfileUrl()).getAuthority() == null) {
            image = ApiClient.BASE_URL + item.getProfileUrl();
        } else {
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
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profileImage;
        TextView profileName;
        Button btnAcceptRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            profileName = itemView.findViewById(R.id.profile_name);
            btnAcceptRequest = itemView.findViewById(R.id.btn_accept);

            btnAcceptRequest.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iPerformAction.performAction(requestList.indexOf(requestList.get(getAdapterPosition())),
                    requestList.get(getAdapterPosition()).getUid(), 3);
        }
    }

    public interface IPerformAction {
        void performAction(int position, String profileId, int operationType);
    }
}
