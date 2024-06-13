package com.example.kosovo.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kosovo.repository.PostRepository;
import com.example.kosovo.repository.UserRepository;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.post.PostResponse;
import com.example.kosovo.entity.profile.ProfileResponse;
import com.example.kosovo.entity.reaction.ReactResponse;
import com.example.kosovo.utils.Util;

import java.util.Map;

import okhttp3.MultipartBody;

public class ProfileViewModel extends ViewModel {
    private UserRepository userRepository;
    private PostRepository postRepository;

    public ProfileViewModel(UserRepository userRepository,PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    public LiveData<ProfileResponse> fetchProfileInfo(Map<String,String> params){
        return userRepository.fetchProfileInfo(params);
    }
    public LiveData<PostResponse> getProfilePosts(Map<String,String> params){
        return postRepository.getProfilePosts(params);
    }
    public LiveData<GeneralResponse> uploadPost(MultipartBody multipartBody, Boolean isCoverOrProfileImage){
        return postRepository.uploadPost(multipartBody, isCoverOrProfileImage);
    }
    public LiveData<GeneralResponse> editPost(MultipartBody multipartBody) {
        return postRepository.editPost(multipartBody);
    }

    public LiveData<GeneralResponse> performAction(ProfileActivity.PerformAction performAction){
        return userRepository.performOperation(performAction);
    }
    public LiveData<ReactResponse> performReaction(Util.PerformReaction performReaction){
        return postRepository.performReaction(performReaction);
    }
    public LiveData<GeneralResponse> deletePost(Map<String, String> params){
        return postRepository.deletePost(params);
    }
}
