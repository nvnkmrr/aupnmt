package com.aupnmt.dto;

import java.time.LocalDate;

public class Jobs {

	private Long jobId;
	private String jobPostedBy;
	private String jobDetails;
	private boolean active;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobPostedBy() {
		return jobPostedBy;
	}

	public void setJobPostedBy(String jobPostedBy) {
		this.jobPostedBy = jobPostedBy;
	}

	public String getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(String jobDetails) {
		this.jobDetails = jobDetails;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
