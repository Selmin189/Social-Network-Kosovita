package com.example.kosovo.data.remote;

import com.example.kosovo.ui.auth.LoginActivity;
import com.example.kosovo.ui.profile.ProfileActivity;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.auth.AuthResponse;
import com.example.kosovo.entity.friend.FriendResponse;
import com.example.kosovo.entity.post.PostResponse;
import com.example.kosovo.entity.profile.ProfileResponse;
import com.example.kosovo.entity.reaction.ReactResponse;
import com.example.kosovo.entity.search.SearchResponse;
import com.example.kosovo.utils.Util;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @POST("login")
    Call<AuthResponse> login(@Body LoginActivity.UserInfo userInfo);
    @POST("performReaction")
    Call<ReactResponse> performReaction(@Body Util.PerformReaction performReaction);
    @POST("uploadpost")
    Call<GeneralResponse> uploadPost(@Body MultipartBody body);
    @POST("editpost")
    Call<GeneralResponse> editPost(@Body MultipartBody body);

    @POST("uploadImage")
    Call<GeneralResponse> uploadImage(@Body MultipartBody body);

    @GET("loadprofileinfo")
    Call<ProfileResponse> fetchProfileInfo(@QueryMap Map<String,String> params);

    @GET("search")
    Call<SearchResponse> search(@QueryMap Map<String,String> params);

    @POST("performaction")
    Call<GeneralResponse> performAction(@Body ProfileActivity.PerformAction body);

    @GET("loadfriends")
    Call<FriendResponse> loadFriends(@Query("uid") String uid);
    @GET("getnewsfeed")
    Call<PostResponse> getNewsFeed(@QueryMap Map<String,String> params);
    @GET("loadProfilePosts")
    Call<PostResponse> loadProfilePosts(@QueryMap Map<String,String> params);
    @HTTP(method = "DELETE", path = "deletepost", hasBody = true)
    Call<GeneralResponse> deletePost(@Body RequestBody body);
}
