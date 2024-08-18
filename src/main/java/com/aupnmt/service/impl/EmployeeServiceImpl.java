package com.aupnmt.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aupnmt.dto.Employee;
import com.aupnmt.service.EmployeeService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	AzureStorageServiceImpl azureStorageServiceImpl;

	@Value("${azure.storage.blob.endpoint}")
	private String storageEndPoint;

	@Value("${azure.storage.blob.container}")
	private String storageContainer;

	@Value("${azure.storage.blob.filename}")
	private String filename;

	@Override
	public String employee(Employee employee) throws IOException, URISyntaxException {

		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageEndPoint).buildClient();
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainer);
		BlobClient blobClient = containerClient.getBlobClient(filename);
		XSSFWorkbook workbook = new XSSFWorkbook(blobClient.openInputStream());
		Sheet sheet = workbook.getSheet("Employee");
		int lastRowNumber = sheet.getLastRowNum();
		Long newId = 0L;

		Iterator<Row> iterator = sheet.rowIterator();
		while (iterator.hasNext()) {
			Row row = (Row) iterator.next();
			if (row.getCell(2).toString().equalsIgnoreCase(employee.getPhoneNumber())) {
				return "Already an employee exists with the Phone number : " + employee.getPhoneNumber();
			}
		}

		if (sheet.getRow(lastRowNumber).getCell(0).getCellType() == CellType.STRING) {
			newId = 1L;
		} else {
			newId = (new Double(sheet.getRow(lastRowNumber).getCell(0).getNumericCellValue()).longValue()) + 1;
		}
		Row row = sheet.createRow(lastRowNumber + 1);
		row.createCell(0).setCellValue(newId);
		row.createCell(1).setCellValue(employee.getName());
		row.createCell(2).setCellValue(employee.getPhoneNumber());
		row.createCell(3).setCellValue(employee.getOccupation());
		row.createCell(4).setCellValue(employee.getExperience());
		row.createCell(5).setCellValue(employee.getCurrentSalary());
		row.createCell(6).setCellValue(employee.getCurrentEmployer());
		row.createCell(7).setCellValue(employee.getEmployerPlace());
		row.createCell(8).setCellValue(employee.getAadharNumber());
		row.createCell(9).setCellValue("");
		row.createCell(10).setCellValue(employee.getCurrentAddress());
		row.createCell(11).setCellValue(employee.getPermanentAddress());
		row.createCell(12).setCellValue("");
		row.createCell(13).setCellValue(employee.getPremiumUser().equals(true) ? "Y" : "N");
		row.createCell(14).setCellValue(employee.getCreatedDt());
		row.createCell(15).setCellValue(employee.getModifiedDt());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		blobClient.upload(new ByteArrayInputStream(bos.toByteArray()));
		return "Successfully created new employee with Id:" + newId;
	}

	@Override
	public Employee employee(String PhoneNumber) throws Exception {

		Employee employee = new Employee();
		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Employee");
		Iterator<Row> rowIterator = xSSFSheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}
			Row row = (Row) rowIterator.next();
			if (row.getCell(2).toString().equalsIgnoreCase(PhoneNumber)) {
				employee.setEmployeeId(new Double(row.getCell(0).getNumericCellValue()).longValue());
				employee.setName(row.getCell(1).toString());
				employee.setPhoneNumber(PhoneNumber);
				employee.setOccupation(row.getCell(3).toString());
				employee.setExperience(row.getCell(4).toString());
				employee.setCurrentSalary(row.getCell(5).toString());
				employee.setCurrentEmployer(row.getCell(6).toString());
				employee.setEmployerPlace(row.getCell(7).toString());
				employee.setAadharNumber(row.getCell(8).toString());
				employee.setCurrentAddress(row.getCell(10).toString());
				employee.setPermanentAddress(row.getCell(11).toString());
				employee.setPremiumUser(row.getCell(13).toString().equalsIgnoreCase("Y") ? true : false);
				break;
			}
		}
		return employee;
	}

	@Override
	public String findEmployee(String PhoneNumber) throws Exception {
		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Employee");
		Iterator<Row> rowIterator = xSSFSheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}
			Row row = (Row) rowIterator.next();
			if (row.getCell(2).toString().equalsIgnoreCase(PhoneNumber)) {
				return "Success" + row.getCell(13).toString();
			}
		}
		return "Failure";
	}
}
