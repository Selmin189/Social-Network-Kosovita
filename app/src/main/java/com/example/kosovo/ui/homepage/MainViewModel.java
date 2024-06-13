package com.example.kosovo.ui.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kosovo.repository.PostRepository;
import com.example.kosovo.repository.UserRepository;
import com.example.kosovo.ui.profile.ProfileActivity;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.friend.FriendResponse;
import com.example.kosovo.entity.post.PostResponse;
import com.example.kosovo.entity.reaction.ReactResponse;
import com.example.kosovo.utils.Util;

import java.util.Map;

public class MainViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private MutableLiveData<FriendResponse> friends = null;

    public MainViewModel(UserRepository userRepository,PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public MutableLiveData<FriendResponse> loadFriends(String uid) {
        if (friends == null){
            friends = userRepository.loadFriends(uid);
        }
        return friends;
    }
    public LiveData<GeneralResponse> performAction(ProfileActivity.PerformAction performAction){
        return userRepository.performOperation(performAction);
    }
    public LiveData<PostResponse> getNewsFeed(Map<String ,String> params){
        return postRepository.getNewsFeed(params);
    }
    public LiveData<ReactResponse> performReaction(Util.PerformReaction performReaction){
        return postRepository.performReaction(performReaction);
    }
    public LiveData<GeneralResponse> deletePost(Map<String, String> params){
        return postRepository.deletePost(params);
    }
}
