package com.example.kosovo.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kosovo.data.remote.ApiError;
import com.example.kosovo.data.remote.ApiService;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.post.PostResponse;
import com.example.kosovo.entity.reaction.ReactResponse;
import com.example.kosovo.utils.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private static PostRepository instance = null;
    private final ApiService apiService;

    private PostRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static PostRepository getRepository(ApiService apiService) {
        if (instance == null) {
            instance = new PostRepository(apiService);
        }
        return instance;
    }
    public LiveData<PostResponse> getNewsFeed(Map<String,String> params){
        MutableLiveData<PostResponse> posts = new MutableLiveData<>();
        Call<PostResponse> call = apiService.getNewsFeed(params);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()){
                    posts.postValue(response.body());
                }else {
                    Gson gson = new Gson();
                    PostResponse postResponse = null;
                    try {
                        postResponse = gson.fromJson(response.errorBody().string(),PostResponse.class);
                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        postResponse = new PostResponse(errorMessage.message,errorMessage.status);
                    }
                    posts.postValue(postResponse);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                PostResponse postResponse = new PostResponse(errorMessage.message, errorMessage.status);
                posts.postValue(postResponse);
            }
        });
        return posts;
    }
    public LiveData<PostResponse> getProfilePosts(Map<String,String> params){
        MutableLiveData<PostResponse> posts = new MutableLiveData<>();
        Call<PostResponse> call = apiService.loadProfilePosts(params);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()){
                    posts.postValue(response.body());
                }else {
                    Gson gson = new Gson();
                    PostResponse postResponse = null;
                    try {
                        postResponse = gson.fromJson(response.errorBody().string(),PostResponse.class);
                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        postResponse = new PostResponse(errorMessage.message,errorMessage.status);
                    }
                    posts.postValue(postResponse);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                PostResponse postResponse = new PostResponse(errorMessage.message, errorMessage.status);
                posts.postValue(postResponse);
            }
        });
        return posts;
    }
    public LiveData<GeneralResponse> uploadPost(MultipartBody multipartBody, Boolean isCoverOrProfileImage){
        MutableLiveData<GeneralResponse> postUpload = new MutableLiveData<>();
        Call<GeneralResponse> call = null;
        if (isCoverOrProfileImage) {
            call = apiService.uploadImage(multipartBody);
        }else {
            call = apiService.uploadPost(multipartBody);
        }
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()){
                    postUpload.postValue(response.body());
                }else {
                    Gson gson = new Gson();
                    GeneralResponse generalResponse = null;
                    try {
                        generalResponse = gson.fromJson(response.errorBody().string(),GeneralResponse.class);
                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        generalResponse = new GeneralResponse(errorMessage.message,errorMessage.status);
                    }
                    postUpload.postValue(generalResponse);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                GeneralResponse generalResponse = new GeneralResponse(errorMessage.message, errorMessage.status);
                postUpload.postValue(generalResponse);
            }
        });

        return postUpload;
    }
        public LiveData<ReactResponse> performReaction(Util.PerformReaction performReaction){
        MutableLiveData<ReactResponse> posts = new MutableLiveData<>();
        Call<ReactResponse> call = apiService.performReaction(performReaction);
        call.enqueue(new Callback<ReactResponse>() {
            @Override
            public void onResponse(Call<ReactResponse> call, Response<ReactResponse> response) {
                if (response.isSuccessful()){
                    posts.postValue(response.body());
                }else {
                    Gson gson = new Gson();
                    ReactResponse reactResponse = null;
                    try {
                        reactResponse = gson.fromJson(response.errorBody().string(),ReactResponse.class);
                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        reactResponse = new ReactResponse(errorMessage.message,errorMessage.status);
                    }
                    posts.postValue(reactResponse);
                }
            }

            @Override
            public void onFailure(Call<ReactResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                ReactResponse reactResponse = new ReactResponse(errorMessage.message, errorMessage.status);
                posts.postValue(reactResponse);
            }
        });
        return posts;
    }
    ///odovde nadlje ja som pisav trazi greske ovde
    public LiveData<GeneralResponse> deletePost(Map<String, String> params){
        MutableLiveData<GeneralResponse> postDelete = new MutableLiveData<>();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(params));
        Call<GeneralResponse> call = apiService.deletePost(body);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()){
                    postDelete.postValue(response.body());
                }else {
                    Gson gson = new Gson();
                    GeneralResponse generalResponse = null;
                    try {
                        generalResponse = gson.fromJson(response.errorBody().string(),GeneralResponse.class);
                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        generalResponse = new GeneralResponse(errorMessage.message,errorMessage.status);
                    }
                    postDelete.postValue(generalResponse);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                GeneralResponse generalResponse = new GeneralResponse(errorMessage.message, errorMessage.status);
                postDelete.postValue(generalResponse);
            }
        });
        return postDelete;
    }
    ////Edittt
    public LiveData<GeneralResponse> editPost(MultipartBody multipartBody) {
        MutableLiveData<GeneralResponse> postUpload = new MutableLiveData<>();
        Call<GeneralResponse> call = apiService.editPost(multipartBody);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    postUpload.postValue(response.body());
                } else {
                    Gson gson = new Gson();
                    GeneralResponse generalResponse = null;
                    try {
                        generalResponse = gson.fromJson(response.errorBody().string(), GeneralResponse.class);
                    } catch (IOException e) {
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        generalResponse = new GeneralResponse(errorMessage.message, errorMessage.status);
                    }
                    postUpload.postValue(generalResponse);
                }
            }
            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                GeneralResponse generalResponse = new GeneralResponse(errorMessage.message, errorMessage.status);
                postUpload.postValue(generalResponse);
            }
        });

        return postUpload;
    }
}
