package com.aupnmt.api;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.dto.Owner;
import com.aupnmt.dto.Response;
import com.aupnmt.service.OwnerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "Owner", description = "Owner APIs")
public class OwnerController {

	@Autowired
	OwnerService ownerService;

	@PostMapping("/owner")
	public Response owner(@RequestBody Owner owner) {
		Response response = new Response();
		try {
			String responseData = ownerService.owner(owner);
			response.setMessage(responseData);
			if (responseData.contains("owner exists"))
				response.setStatus("Failure");
			else
				response.setStatus("Success");
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to save/create a new owner");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to save/create a new owner");
		}
		return response;
	}

	@GetMapping(value = "/owner")
	public Response owner(@RequestParam String phoneNumber) {
		Response response = new Response();
		try {
			Owner owner = ownerService.owner(phoneNumber);
			if (Objects.nonNull(owner.getPhoneNumber())) {
				response.setStatus("Success");
				response.setData(owner);
				response.setMessage("Owner data for the mobile number :" + phoneNumber);
			} else {
				response.setStatus("Failure");
				response.setMessage("No data aviailable for the Owner with mobile number :" + phoneNumber);
			}

		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to fetch the owner record");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("Failure");
			response.setMessage("Failed to fetch the owner record");
		}
		return response;
	}

}
