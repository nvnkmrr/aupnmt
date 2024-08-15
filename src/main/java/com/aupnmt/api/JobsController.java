package com.aupnmt.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.dto.Jobs;
import com.aupnmt.dto.Response;
import com.aupnmt.service.JobsService;

@RestController
public class JobsController {

	@Autowired
	JobsService jobsService;

	@PostMapping("/job")
	public Response job(@RequestBody Jobs jobs) {
		Response response = new Response();
		try {
			String responseData = jobsService.job(jobs);
			response.setMessage(responseData);
			response.setStatus("Success");
		} catch (IOException e) {
			response.setStatus("Failure");
			response.setMessage("Failed to save/create a new job");
		} catch (Exception e) {
			response.setStatus("Failure");
			response.setMessage("Failed to save/create a new job");
		}
		return response;
	}

	@GetMapping("/activeJobs")
	public Response activeJobs() {
		Response r = new Response();
		try {
			r.setData(jobsService.jobs(true));
			r.setStatus("Success");
			r.setMessage("Active Jobs list");
		} catch (IOException e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch Active Jobs list");
		} catch (Exception e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch Active Jobs list");
		}
		return r;
	}

	@GetMapping("/inActiveJobs")
	public Response inActiveJobs() {
		Response r = new Response();
		try {
			r.setData(jobsService.jobs(false));
			r.setStatus("Success");
			r.setMessage(" In Active Jobs list");
		} catch (IOException e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch In Active Jobs list");
		} catch (Exception e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch In Active Jobs list");
		}
		return r;
	}

	@GetMapping("/jobsByMe")
	public Response jobsByMe(@RequestParam String phoneNumber) {
		Response r = new Response();
		try {
			r.setData(jobsService.jobs(phoneNumber));
			r.setStatus("Success");
			r.setMessage("List of Jobs posted by Me: " + phoneNumber);
		} catch (IOException e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch List of Jobs posted by Me: " + phoneNumber);
		} catch (Exception e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch List of Jobs posted by Me: " + phoneNumber);
		}
		return r;
	}

	@PutMapping("/jobs/delete")
	public Response delete(@RequestParam Long jobId) {
		Response r = new Response();
		try {
			r.setStatus("Success");
			r.setMessage(jobsService.delete(jobId));
		} catch (IOException e) {
			r.setStatus("Failure");
			r.setMessage("Failed to delete Job with ID: " + jobId);
		} catch (Exception e) {
			r.setStatus("Failure");
			r.setMessage("Failed to delete Job with ID: " + jobId);
		}
		return r;
	}

	@PutMapping("/jobs/approve")
	public Response approve(@RequestParam Long jobId) {
		Response r = new Response();
		try {
			r.setStatus("Success");
			r.setMessage(jobsService.approve(jobId));
		} catch (IOException e) {
			r.setStatus("Failure");
			r.setMessage("Failed to approve Job with ID: " + jobId);
		} catch (Exception e) {
			r.setStatus("Failure");
			r.setMessage("Failed to approve Job with ID: " + jobId);
		}
		return r;
	}
}
