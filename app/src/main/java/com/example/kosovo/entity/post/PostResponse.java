package com.example.kosovo.entity.post;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PostResponse {

	@SerializedName("message")
	private String message;

	@SerializedName("posts")
	private List<PostsItem> posts;

	@SerializedName("status")
	private int status;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setPosts(List<PostsItem> posts){
		this.posts = posts;
	}

	public List<PostsItem> getPosts(){
		return posts;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public PostResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}
}