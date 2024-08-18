package com.aupnmt.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Owner;

@Service
public interface OwnerService {

	String owner(Owner owner) throws IOException, URISyntaxException;

	Owner owner(String PhoneNumber) throws Exception;
	
	String findOwner(String PhoneNumber) throws Exception;

}
