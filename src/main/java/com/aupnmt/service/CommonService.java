package com.aupnmt.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.AccessToken;

@Service
public interface CommonService {

	AccessToken userIdentification(String phoneNumber) throws IOException, URISyntaxException;

}
