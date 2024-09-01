package com.aupnmt.service.impl;

import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aupnmt.service.OtpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class OtpServiceImpl implements OtpService {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${otp.service.provider.url}")
	private String otpServiceproviderUrl;

	@Value("${otp.service.provider.key}")
	private String otpServiceproviderKey;

	@Cacheable(value = "default", key = "#phoneNumber")
	@Override
	public int generateOTP(String phoneNumber, Boolean flag) throws Exception{
		int otp = (100000 + new Random().nextInt(900000));
//		if (flag) {
//			String response = sendOTP(otp, phoneNumber);
//			if (response.equalsIgnoreCase("Failure")) {
//				return 0;
//			}
//		}
		return otp;
	}

	public String sendOTP(Integer otp, String phoneNumber) throws Exception{
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("authorization", otpServiceproviderKey);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("variables_values", otp.toString());
		rootNode.put("route", "otp");
		rootNode.put("numbers", phoneNumber);
		String requestBody;
		try {
			requestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		} catch (JsonProcessingException e) {
			return "Failure";
		}
		try {
			HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(otpServiceproviderUrl, entity, String.class);
			if (response.getBody().contains("SMS sent successfully")) {
				return "Success";
			} else {
				return "Failure";
			}
		}catch(Exception e) {
			throw e;
		}
	}

}
