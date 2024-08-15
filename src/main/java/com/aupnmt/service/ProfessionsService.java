package com.aupnmt.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Professions;

@Service
public interface ProfessionsService {

	List<Professions> professions() throws IOException, URISyntaxException;
	
}
