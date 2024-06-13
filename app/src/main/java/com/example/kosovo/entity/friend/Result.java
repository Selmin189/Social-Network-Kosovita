package com.example.kosovo.entity.friend;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("requests")
	private List<Request> requests;

	@SerializedName("friends")
	private List<Friend> friends;

	public void setRequests(List<Request> requests){
		this.requests = requests;
	}

	public List<Request> getRequests(){
		return requests;
	}

	public void setFriends(List<Friend> friends){
		this.friends = friends;
	}

	public List<Friend> getFriends(){
		return friends;
	}
}