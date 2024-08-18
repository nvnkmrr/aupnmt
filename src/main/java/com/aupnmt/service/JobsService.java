package com.aupnmt.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Jobs;

@Service
public interface JobsService {

	String job(Jobs jobs) throws IOException, URISyntaxException;

	List<Jobs> jobs(boolean flag) throws Exception;

	List<Jobs> jobs(String phoneNumber) throws Exception;

	String delete(Long jobId) throws IOException, URISyntaxException;
	
	String approve(Long jobId) throws IOException, URISyntaxException;
}
