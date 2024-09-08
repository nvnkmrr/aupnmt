package com.aupnmt.service.impl;

import java.util.Iterator;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

	@Autowired
	AzureStorageServiceImpl azureStorageServiceImpl;

	@Override
	public AccessToken userIdentification(String phoneNumber, XSSFWorkbook workbook) throws Exception {
		AccessToken accessToken = new AccessToken();
		String employeeIdentification = "";
		String ownerIdentification = "";
		String adminIdentification = "";
		String employeeName = "";
		String ownerName = "";
		String adminName = "";
		if(Objects.isNull(workbook))
			workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Employee");
		Iterator<Row> rowIterator = xSSFSheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}
			if (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				if (row.getCell(2).toString().equalsIgnoreCase(phoneNumber)) {
					employeeName = row.getCell(1).toString();
					employeeIdentification = "Success" + row.getCell(13).toString();
				}
			}
		}
		employeeIdentification = employeeIdentification.isEmpty() ? "Failure" : employeeIdentification;
		xSSFSheet = workbook.getSheet("Owner");
		rowIterator = xSSFSheet.iterator();
		skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}
			if (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				if (row.getCell(2).toString().equalsIgnoreCase(phoneNumber)) {
					ownerName = row.getCell(1).toString();
					ownerIdentification = "Success" + row.getCell(12).toString();
				}
			}
		}
		ownerIdentification = ownerIdentification.isEmpty() ? "Failure" : ownerIdentification;
		xSSFSheet = workbook.getSheet("Admin");
		rowIterator = xSSFSheet.iterator();
		skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}
			if (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				adminName = row.getCell(1).toString();
				if (row.getCell(2).toString().equalsIgnoreCase(phoneNumber)) {
					adminIdentification = "Success";
				}
			}
		}
		adminIdentification = adminIdentification.isEmpty() ? "Failure" : adminIdentification;
		if (employeeIdentification.equalsIgnoreCase("SuccessY")) {
			accessToken.setRole("EMPLOYEE");
			accessToken.setPremiumUser(true);
			accessToken.setName(employeeName);
		} else if (employeeIdentification.equalsIgnoreCase("SuccessN")) {
			accessToken.setRole("EMPLOYEE");
			accessToken.setPremiumUser(false);
			accessToken.setName(employeeName);
		}

		if (ownerIdentification.equalsIgnoreCase("SuccessY")) {
			accessToken.setRole("OWNER");
			accessToken.setPremiumUser(true);
			accessToken.setName(ownerName);
		} else if (ownerIdentification.equalsIgnoreCase("SuccessN")) {
			accessToken.setRole("OWNER");
			accessToken.setPremiumUser(false);
			accessToken.setName(ownerName);
		}
		if (adminIdentification.equalsIgnoreCase("Success")) {
			accessToken.setRole("ADMIN");
			accessToken.setName(adminName);
		}
		return accessToken;
	}

}