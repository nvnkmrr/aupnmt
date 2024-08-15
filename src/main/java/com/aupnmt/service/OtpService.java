package com.aupnmt.service;

import org.springframework.stereotype.Service;

@Service
public interface OtpService {

	int generateOTP(String phoneNumber, Boolean flag);

}
