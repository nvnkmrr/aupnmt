package com.aupnmt.dto;

import java.time.LocalDate;

public class Employee {

	private Long employeeId;
	private String name;
	private String phoneNumber;
	private String occupation;
	private String experience;
	private String currentSalary;
	private String currentEmployer;
	private String employerPlace;
	private String AadharNumber;
	private String currentAddress;
	private String permanentAddress;
	private Boolean premiumUser;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCurrentSalary() {
		return currentSalary;
	}

	public void setCurrentSalary(String currentSalary) {
		this.currentSalary = currentSalary;
	}

	public String getCurrentEmployer() {
		return currentEmployer;
	}

	public void setCurrentEmployer(String currentEmployer) {
		this.currentEmployer = currentEmployer;
	}

	public String getEmployerPlace() {
		return employerPlace;
	}

	public void setEmployerPlace(String employerPlace) {
		this.employerPlace = employerPlace;
	}

	public String getAadharNumber() {
		return AadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		AadharNumber = aadharNumber;
	}

	public String getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public Boolean getPremiumUser() {
		return premiumUser;
	}

	public void setPremiumUser(Boolean premiumUser) {
		this.premiumUser = premiumUser;
	}

}