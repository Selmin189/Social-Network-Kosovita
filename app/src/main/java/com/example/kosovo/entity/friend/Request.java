package com.example.kosovo.entity.friend;

import com.google.gson.annotations.SerializedName;

public class Request {

	@SerializedName("profileUrl")
	private String profileUrl;

	@SerializedName("coverUrl")
	private String coverUrl;

	@SerializedName("uid")
	private String uid;

	@SerializedName("userToken")
	private String userToken;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	public void setProfileUrl(String profileUrl){
		this.profileUrl = profileUrl;
	}

	public String getProfileUrl(){
		return profileUrl;
	}

	public void setCoverUrl(String coverUrl){
		this.coverUrl = coverUrl;
	}

	public String getCoverUrl(){
		return coverUrl;
	}

	public void setUid(String uid){
		this.uid = uid;
	}

	public String getUid(){
		return uid;
	}

	public void setUserToken(String userToken){
		this.userToken = userToken;
	}

	public String getUserToken(){
		return userToken;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}