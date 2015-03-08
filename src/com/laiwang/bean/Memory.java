package com.laiwang.bean;

import java.util.Date;

public class Memory {

	private String title;
	private String introduction;
	private String dataUri;
	private double latitude;
	private double longitude;
	private Date createdAt;
	private Date updatedAt;

	public Memory() {

	}

	public Memory(String title, String introduction, String dataUri,
			double latitude, double longitude, Date createdAt, Date updatedAt) {
		this.title = title;
		this.introduction = introduction;
		this.dataUri = dataUri;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDataUri() {
		return dataUri;
	}

	public void setDataUri(String dataUri) {
		this.dataUri = dataUri;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getcreatedAt() {
		return createdAt;
	}

	public void setcreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getupdatedAt() {
		return updatedAt;
	}

	public void setupdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
