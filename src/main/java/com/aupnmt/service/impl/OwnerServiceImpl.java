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

import com.aupnmt.dto.Owner;
import com.aupnmt.service.OwnerService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class OwnerServiceImpl implements OwnerService {
	
	@Autowired
	AzureStorageServiceImpl azureStorageServiceImpl;
	
	@Value("${azure.storage.blob.endpoint}")
	private String storageEndPoint;

	@Value("${azure.storage.blob.container}")
	private String storageContainer;

	@Value("${azure.storage.blob.filename}")
	private String filename;

	@Override
	public String owner(Owner owner) throws IOException, URISyntaxException {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageEndPoint).buildClient();
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainer);
		BlobClient blobClient = containerClient.getBlobClient(filename);
		XSSFWorkbook workbook = new XSSFWorkbook(blobClient.openInputStream());
		Sheet sheet = workbook.getSheet("Owner");
		int lastRowNumber = sheet.getLastRowNum();
		Long newId = 0L;

		Iterator<Row> iterator = sheet.rowIterator();
		while (iterator.hasNext()) {
			Row row = (Row) iterator.next();
			if (row.getCell(2).toString().equalsIgnoreCase(owner.getPhoneNumber())) {
				return "Already an owner exists with the Phone number : " + owner.getPhoneNumber();
			}
		}

		if (sheet.getRow(lastRowNumber).getCell(0).getCellType() == CellType.STRING) {
			newId = 1L;
		} else {
			newId = (new Double(sheet.getRow(lastRowNumber).getCell(0).getNumericCellValue()).longValue()) + 1;
		}
		Row row = sheet.createRow(lastRowNumber + 1);
		row.createCell(0).setCellValue(newId);
		row.createCell(1).setCellValue(owner.getName());
		row.createCell(2).setCellValue(owner.getPhoneNumber());
		row.createCell(3).setCellValue(owner.getOccupation());
		row.createCell(4).setCellValue(owner.getExperience());
		row.createCell(5).setCellValue(owner.getOrganizationName());
		row.createCell(6).setCellValue(owner.getOrganizationCity());
		row.createCell(7).setCellValue(owner.getAadharNumber());
		row.createCell(8).setCellValue("");
		row.createCell(9).setCellValue(owner.getCurrentAddress());
		row.createCell(10).setCellValue(owner.getPermanentAddress());
		row.createCell(11).setCellValue("");
		row.createCell(12).setCellValue(owner.getPremiumUser().equals(true) ? "Y" : "N");
		row.createCell(13).setCellValue(owner.getCreatedDt());
		row.createCell(14).setCellValue(owner.getModifiedDt());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		blobClient.upload(new ByteArrayInputStream(bos.toByteArray()));
		workbook.close();
		return "Successfully created new owner with Id:" + newId;
	}

	@Override
	public Owner owner(String PhoneNumber) throws Exception {
		Owner owner = new Owner();
		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Owner");
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
				owner.setOwnerId(new Double(row.getCell(0).getNumericCellValue()).longValue());
				owner.setName(row.getCell(1).toString());
				owner.setPhoneNumber(PhoneNumber);
				owner.setOccupation(row.getCell(3).toString());
				owner.setExperience(row.getCell(4).toString());
				owner.setOrganizationName(row.getCell(5).toString());
				owner.setOrganizationCity(row.getCell(6).toString());
				owner.setAadharNumber(row.getCell(7).toString());
				owner.setCurrentAddress(row.getCell(9).toString());
				owner.setPermanentAddress(row.getCell(10).toString());
				owner.setPremiumUser(row.getCell(12).toString().equalsIgnoreCase("Y") ? true : false);
				break;
			}
		}
		return owner;
	}

	@Override
	public String findOwner(String PhoneNumber) throws Exception {
		
		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Owner");
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
				return "Success" + row.getCell(12).toString();
			}
		}
		return "Failure";
	}
}
