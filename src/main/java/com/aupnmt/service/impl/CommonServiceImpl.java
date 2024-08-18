package com.aupnmt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aupnmt.dto.AccessToken;
import com.aupnmt.service.AdminService;
import com.aupnmt.service.CommonService;
import com.aupnmt.service.EmployeeService;
import com.aupnmt.service.OwnerService;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	OwnerService ownerService;

	@Autowired
	AdminService adminService;

	@Override
	public AccessToken userIdentification(String phoneNumber) throws Exception {
		AccessToken accessToken = new AccessToken();
		String employeeIdentification = employeeService.findEmployee(phoneNumber);
		String ownerIdentification = ownerService.findOwner(phoneNumber);
		String adminIdentification = adminService.findAdmin(phoneNumber);
		if (employeeIdentification.equalsIgnoreCase("SuccessY")) {
			accessToken.setRole("EMPLOYEE");
			accessToken.setPremiumUser(true);
		} else if (employeeIdentification.equalsIgnoreCase("SuccessN")) {
			accessToken.setRole("EMPLOYEE");
			accessToken.setPremiumUser(false);
		}

		if (ownerIdentification.equalsIgnoreCase("SuccessY")) {
			accessToken.setRole("OWNER");
			accessToken.setPremiumUser(true);
		} else if (ownerIdentification.equalsIgnoreCase("SuccessN")) {
			accessToken.setRole("OWNER");
			accessToken.setPremiumUser(false);
		}

		if (adminIdentification.equalsIgnoreCase("Success")) {
			accessToken.setRole("ADMIN");
		}

		return accessToken;
	}

}
