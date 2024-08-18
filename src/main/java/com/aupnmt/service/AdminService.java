package com.aupnmt.service;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Admin;

@Service
public interface AdminService {

	Admin admin(String PhoneNumber) throws Exception;
	
	String findAdmin(String PhoneNumber) throws Exception;
	
}
