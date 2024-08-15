package com.aupnmt.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.aupnmt.AupnmtApplication;
import com.aupnmt.dto.Owner;
import com.aupnmt.service.OwnerService;

@Service
public class OwnerServiceImpl implements OwnerService {

	@Override
	public String owner(Owner owner) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		File file = new File(database.toString());
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
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

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return "Successfully created new owner with Id:" + newId;
	}

	@Override
	public Owner owner(String PhoneNumber) throws IOException, URISyntaxException {
		Owner owner = new Owner();
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString()))).getSheet("Owner");
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
	public String findOwner(String PhoneNumber) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString()))).getSheet("Owner");
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
