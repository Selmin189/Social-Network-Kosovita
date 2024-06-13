package com.example.kosovo.ui.homepage.friends;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosovo.R;
import com.example.kosovo.adapter.FriendAdapter;
import com.example.kosovo.ui.homepage.MainActivity;
import com.example.kosovo.ui.homepage.MainViewModel;
import com.example.kosovo.entity.friend.Friend;
import com.example.kosovo.entity.friend.FriendResponse;
import com.example.kosovo.entity.friend.Request;
import com.example.kosovo.utils.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {

    private MainViewModel mainViewModel;
    private Context context;
    private RecyclerView friendRequesRV, friendsRV;
    private TextView friendTitle, requestTitle, defaultTitle;
    private FriendAdapter friendAdapter;
    private FriendRequestAdapter friendRequestAdapter;
    private List<Friend> friends = new ArrayList<>();
    private List<Request> requestList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider((FragmentActivity) context, new ViewModelFactory()).get(MainViewModel.class);

        friendsRV = view.findViewById(R.id.friends_recyv);
        friendRequesRV = view.findViewById(R.id.friend_request_recyv);
        requestTitle = view.findViewById(R.id.request_title);
        friendTitle = view.findViewById(R.id.friend_title);
        defaultTitle = view.findViewById(R.id.default_text);

        friendAdapter = new FriendAdapter(context, friends);
        friendRequestAdapter = new FriendRequestAdapter(context,requestList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        friendsRV.setAdapter(friendAdapter);
        friendsRV.setLayoutManager(linearLayoutManager);
        friendRequesRV.setAdapter(friendRequestAdapter);
        friendRequesRV.setLayoutManager(linearLayoutManager2);
        loadFriends();
    }


    private void loadFriends() {
        ((MainActivity) getActivity()).showProgressBar();
        mainViewModel.loadFriends(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<FriendResponse>() {
            @Override
            public void onChanged(FriendResponse friendResponse) {
                ((MainActivity) getActivity()).hideProgressBar();
                loadData(friendResponse);
            }
        });
    }


    private void loadData(FriendResponse friendResponse) {
        if(friendResponse.getStatus() == 200){
            friends.clear();
            friends.addAll(friendResponse.getResult().getFriends());
            friendAdapter.notifyDataSetChanged();

            requestList.clear();
            requestList.addAll(friendResponse.getResult().getRequests());
            friendRequestAdapter.notifyDataSetChanged();

            if (friendResponse.getResult().getFriends().size() > 0){
                friendTitle.setVisibility(View.VISIBLE);
            }else {
                friendTitle.setVisibility(View.GONE);
            }
            if (friendResponse.getResult().getRequests().size() > 0){
                requestTitle.setVisibility(View.VISIBLE);
            }else {
                requestTitle.setVisibility(View.GONE);
            }
            if (friendResponse.getResult().getFriends().size() == 0 && friendResponse.getResult().getRequests().size() == 0){
                defaultTitle.setVisibility(View.VISIBLE);
            }
        }else {
            Toast.makeText(context, friendResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}