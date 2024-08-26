package com.aupnmt.dto;

public class AccessToken {

	private String accessToken;
	private String role;
	private boolean premiumUser;
	private boolean applicationSubmittedStatus;
	private String name;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isPremiumUser() {
		return premiumUser;
	}

	public void setPremiumUser(boolean premiumUser) {
		this.premiumUser = premiumUser;
	}

	public boolean isApplicationSubmittedStatus() {
		return applicationSubmittedStatus;
	}

	public void setApplicationSubmittedStatus(boolean applicationSubmittedStatus) {
		this.applicationSubmittedStatus = applicationSubmittedStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
