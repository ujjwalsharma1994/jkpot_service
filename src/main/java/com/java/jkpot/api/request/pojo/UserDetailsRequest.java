package com.java.jkpot.api.request.pojo;

import java.util.List;

public class UserDetailsRequest {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String location;
	private List<String> examPreferences;
	private String fcmToken;
	private String subscriptionId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getExamPreferences() {
		return examPreferences;
	}

	public void setExamPreferences(List<String> examPreferencesList) {

		if (this.getExamPreferences() == null || this.getExamPreferences().size() == 0)
			this.examPreferences= (examPreferencesList);
		else
			this.examPreferences.addAll(examPreferencesList);
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
}