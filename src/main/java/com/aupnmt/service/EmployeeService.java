package com.aupnmt.service;

import org.springframework.stereotype.Service;

import com.aupnmt.dto.Employee;

@Service
public interface EmployeeService {

	String employee(Employee employee) throws Exception;

	Employee employee(String PhoneNumber) throws Exception;

	String findEmployee(String PhoneNumber) throws Exception;
}
