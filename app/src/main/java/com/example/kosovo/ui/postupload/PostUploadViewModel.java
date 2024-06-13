package com.example.kosovo.ui.postupload;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kosovo.repository.PostRepository;
import com.example.kosovo.helper.GeneralResponse;

import okhttp3.MultipartBody;

public class PostUploadViewModel extends ViewModel {
        private PostRepository postRepository;

    public PostUploadViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public LiveData<GeneralResponse> uploadPost(MultipartBody multipartBody,Boolean isCoverOrProfileImage){
        return postRepository.uploadPost(multipartBody, isCoverOrProfileImage);
    }

    public LiveData<GeneralResponse> editPost(MultipartBody multipartBody) {
        return postRepository.editPost(multipartBody);
    }
}
