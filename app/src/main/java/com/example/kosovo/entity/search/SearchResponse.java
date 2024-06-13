package com.example.kosovo.entity.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResponse{

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private List<User> user;

	@SerializedName("status")
	private int status;

	public String getMessage(){
		return message;
	}

	public List<User> getUser(){
		return user;
	}

	public int getStatus(){
		return status;
	}

	public SearchResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}
}