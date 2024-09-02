package com.aupnmt.api;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.dto.Admin;
import com.aupnmt.dto.Response;
import com.aupnmt.service.AdminService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "Admin", description = "Admin APIs")
public class AdminController {

	@Autowired
	AdminService adminService;

	@GetMapping(value = "/admin")
	public Response admin(@RequestParam String phoneNumber) {
		Response response = new Response();
		try {
			Admin admin = adminService.admin(phoneNumber);
			if (Objects.nonNull(admin.getPhoneNumber())) {
				response.setStatus("Success");
				response.setData(admin);
				response.setMessage("Admin data for the mobile number :" + phoneNumber);
			} else {
				response.setStatus("Failure");
				response.setMessage("No data aviailable for the Admin with mobile number :" + phoneNumber);
			}

		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to fetch the admin record");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to fetch the admin record");
		}
		return response;
	}
}
