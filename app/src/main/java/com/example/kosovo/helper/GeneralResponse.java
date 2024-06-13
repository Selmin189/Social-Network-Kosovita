package com.example.kosovo.helper;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse{

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	@SerializedName("extra")
	private String extra;

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GeneralResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}
}