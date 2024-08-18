package com.aupnmt.service;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.AccessToken;

@Service
public interface CommonService {

	AccessToken userIdentification(String phoneNumber) throws Exception;

}
