package com.example.kosovo.entity.reaction;

import com.google.gson.annotations.SerializedName;

public class ReactResponse{

	@SerializedName("reaction")
	private Reaction reaction;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setReaction(Reaction reaction){
		this.reaction = reaction;
	}

	public Reaction getReaction(){
		return reaction;
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

	public ReactResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}
}