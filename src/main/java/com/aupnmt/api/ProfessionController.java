package com.aupnmt.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.dto.Response;
import com.aupnmt.service.ProfessionsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "Profession", description = "Profession APIs")
public class ProfessionController {

	@Autowired
	ProfessionsService professionsService;

	@GetMapping("/professions")
	public Response professions() {
		Response r = new Response();
		try {
			r.setData(professionsService.professions());
			r.setStatus("Success");
			r.setMessage("Active professions list");
		} catch (IOException e) {;
			r.setStatus("Failure");
			r.setMessage("Failed to fetch Active professions list");
		} catch (Exception e) {
			r.setStatus("Failure");
			r.setMessage("Failed to fetch Active professions list");
		}
		return r;
	}

}
