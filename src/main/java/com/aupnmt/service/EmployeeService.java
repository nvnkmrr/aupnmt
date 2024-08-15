package com.aupnmt.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Employee;

@Service
public interface EmployeeService {

	String employee(Employee employee) throws IOException, URISyntaxException;
	
	Employee employee(String PhoneNumber) throws IOException, URISyntaxException;
	
	String findEmployee(String PhoneNumber) throws IOException, URISyntaxException;
}
