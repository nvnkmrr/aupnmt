package com.aupnmt.api;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.dto.Employee;
import com.aupnmt.dto.Response;
import com.aupnmt.service.EmployeeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "Employee", description = "Employee APIs")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@PostMapping("/employee")
	public Response employee(@RequestBody Employee employee) {
		Response response = new Response();
		try {
			String responseData = employeeService.employee(employee);
			response.setMessage(responseData);
			if (responseData.contains("employee exists"))
				response.setStatus("Failure");
			else
				response.setStatus("Success");
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to save/create a new employee");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to save/create a new employee");
		}
		return response;
	}

	@GetMapping(value = "/employee")
	public Response employee(@RequestParam String phoneNumber) {
		Response response = new Response();
		try {
			Employee employee = employeeService.employee(phoneNumber);
			if (Objects.nonNull(employee.getPhoneNumber())) {
				response.setStatus("Success");
				response.setData(employee);
				response.setMessage("Employee data for the mobile number :" + phoneNumber);
			} else {
				response.setStatus("Failure");
				response.setMessage("No data aviailable for the Employee with mobile number :" + phoneNumber);
			}

		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to fetch the employee record");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to fetch the employee record");
		} 
		return response;
	}

}
