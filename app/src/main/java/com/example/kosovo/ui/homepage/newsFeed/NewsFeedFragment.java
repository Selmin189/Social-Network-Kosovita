package com.example.kosovo.ui.homepage.newsFeed;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kosovo.R;
import com.example.kosovo.ui.homepage.MainActivity;
import com.example.kosovo.ui.homepage.MainViewModel;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.post.PostResponse;
import com.example.kosovo.entity.post.PostsItem;
import com.example.kosovo.entity.reaction.ReactResponse;
import com.example.kosovo.utils.Util;
import com.example.kosovo.utils.ViewModelFactory;
import com.example.kosovo.adapter.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,PostAdapter.IDeletePost {

    private RecyclerView recyclerView;
    private MainViewModel mainViewModel;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostAdapter postAdapter;
    private List<PostsItem> postsItemList;
    private Boolean isFirstLoading = true;
    private int limit = 5;
    private int offset = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        postsItemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyv_newsfeed);
        mainViewModel = new ViewModelProvider((FragmentActivity) context, new ViewModelFactory()).get(MainViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isLastItemReached()){
                    offset += limit;
                    fetchNews();
                }
            }
        });
    }

    private boolean isLastItemReached(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findLastCompletelyVisibleItemPosition();
        int numberOfItems = postAdapter.getItemCount();
        return (position >= numberOfItems -1);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchNews();
    }

    private void fetchNews() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getUid());
        params.put("limit", limit + "");
        params.put("offset", offset + "");
        ((MainActivity) getActivity()).showProgressBar();

        mainViewModel.getNewsFeed(params).observe(getViewLifecycleOwner(), new Observer<PostResponse>() {
            @Override
            public void onChanged(PostResponse postResponse) {
                ((MainActivity) getActivity()).hideProgressBar();

                if (postResponse.getStatus() == 200) {
                    if (swipeRefreshLayout.isRefreshing()){
                        postsItemList.clear();
                        postAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    postsItemList.addAll(postResponse.getPosts());
                    if (isFirstLoading) {
                        postAdapter = new PostAdapter(context, postsItemList);
                        recyclerView.setAdapter(postAdapter);
                    } else {
                        postAdapter.notifyItemRangeChanged(postsItemList.size(), postResponse.getPosts().size());
                    }
                    if (postResponse.getPosts().size() == 0){
                        offset -= limit;
                    }
                    isFirstLoading = false;
                } else {
                    if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, postResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        offset = 0;
        postsItemList.clear();
        isFirstLoading = true;
    }

    @Override
    public void onRefresh() {
        offset = 0;
        isFirstLoading = true;
        fetchNews();
    }

    public void updateUserReaction(String uid, int postId, String postOwnerId, String previousReactionType, String newReactionType, int adapterPosition) {
        mainViewModel.performReaction(new Util.PerformReaction(
                uid,
                postId + "",
                postOwnerId,
                previousReactionType,
                newReactionType
        )).observe(this, new Observer<ReactResponse>() {
            @Override
            public void onChanged(ReactResponse reactResponse) {
                if (reactResponse.getStatus() == 200){
                    postAdapter.updatePostAfterReaction(adapterPosition,reactResponse.getReaction());
                }else {
                    Toast.makeText(context, reactResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void deletePost(String postId, String postUserId, int adapterPosition) {
        Map<String, String> params = new HashMap<>();
        params.put("postId", postId);
        params.put("postUserId", postUserId);

        mainViewModel.deletePost(params).observe(this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                if (generalResponse.getStatus() == 200){
                    // Ako je post uspešno obrisan, ažurirajte listu postova i obavestite adapter
                    postsItemList.remove(adapterPosition);
                    postAdapter.notifyItemRemoved(adapterPosition);
                    Toast.makeText(context, "Post deleted successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}