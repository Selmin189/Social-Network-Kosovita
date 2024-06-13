package com.example.kosovo.entity.reaction;

import com.google.gson.annotations.SerializedName;

public class Reaction{

	@SerializedName("loveCount")
	private int loveCount;

	@SerializedName("careCount")
	private int careCount;

	@SerializedName("wowCount")
	private int wowCount;

	@SerializedName("reactionType")
	private String reactionType;

	@SerializedName("sadCount")
	private int sadCount;

	@SerializedName("angryCount")
	private int angryCount;

	@SerializedName("likeCount")
	private int likeCount;

	@SerializedName("hahaCount")
	private int hahaCount;

	public void setLoveCount(int loveCount){
		this.loveCount = loveCount;
	}

	public int getLoveCount(){
		return loveCount;
	}

	public void setCareCount(int careCount){
		this.careCount = careCount;
	}

	public int getCareCount(){
		return careCount;
	}

	public void setWowCount(int wowCount){
		this.wowCount = wowCount;
	}

	public int getWowCount(){
		return wowCount;
	}

	public void setReactionType(String reactionType){
		this.reactionType = reactionType;
	}

	public String getReactionType(){
		return reactionType;
	}

	public void setSadCount(int sadCount){
		this.sadCount = sadCount;
	}

	public int getSadCount(){
		return sadCount;
	}

	public void setAngryCount(int angryCount){
		this.angryCount = angryCount;
	}

	public int getAngryCount(){
		return angryCount;
	}

	public void setLikeCount(int likeCount){
		this.likeCount = likeCount;
	}

	public int getLikeCount(){
		return likeCount;
	}

	public void setHahaCount(int hahaCount){
		this.hahaCount = hahaCount;
	}

	public int getHahaCount(){
		return hahaCount;
	}
}