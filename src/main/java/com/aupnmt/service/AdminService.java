package com.aupnmt.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Admin;

@Service
public interface AdminService {

	Admin admin(String PhoneNumber) throws IOException, URISyntaxException;
	
	String findAdmin(String PhoneNumber) throws IOException, URISyntaxException;
	
}
