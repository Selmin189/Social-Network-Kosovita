package com.example.kosovo.entity.profile;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse{

	@SerializedName("profile")
	private Profile profile;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setProfile(Profile profile){
		this.profile = profile;
	}

	public Profile getProfile(){
		return profile;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public ProfileResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}
}