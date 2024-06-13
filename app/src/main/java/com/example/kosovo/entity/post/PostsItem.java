package com.example.kosovo.entity.post;

import com.google.gson.annotations.SerializedName;

public class PostsItem{

	@SerializedName("profileUrl")
	private String profileUrl;

	@SerializedName("coverUrl")
	private String coverUrl;

	@SerializedName("postUserId")
	private String postUserId;

	@SerializedName("statusImage")
	private String statusImage;

	@SerializedName("uid")
	private String uid;

	@SerializedName("userToken")
	private String userToken;

	@SerializedName("post")
	private String post;

	@SerializedName("statusTime")
	private String statusTime;

	@SerializedName("name")
	private String name;

	@SerializedName("privacy")
	private int privacy;

	@SerializedName("postId")
	private int postId;

	@SerializedName("email")
	private String email;

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

	public void setPostUserId(String postUserId){
		this.postUserId = postUserId;
	}

	public String getPostUserId(){
		return postUserId;
	}

	public void setStatusImage(String statusImage){
		this.statusImage = statusImage;
	}

	public String getStatusImage(){
		return statusImage;
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

	public void setPost(String post){
		this.post = post;
	}

	public String getPost(){
		return post;
	}

	public void setStatusTime(String statusTime){
		this.statusTime = statusTime;
	}

	public String getStatusTime(){
		return statusTime;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPrivacy(int privacy){
		this.privacy = privacy;
	}

	public int getPrivacy(){
		return privacy;
	}

	public void setPostId(int postId){
		this.postId = postId;
	}

	public int getPostId(){
		return postId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

}