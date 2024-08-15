package com.aupnmt.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.aupnmt.AupnmtApplication;
import com.aupnmt.dto.Admin;
import com.aupnmt.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Override
	public Admin admin(String PhoneNumber) throws IOException, URISyntaxException {

		Admin admin = new Admin();
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString()))).getSheet("Admin");
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
				admin.setAdminId(new Double(row.getCell(0).getNumericCellValue()).longValue());
				admin.setName(row.getCell(1).toString());
				admin.setPhoneNumber(PhoneNumber);
				admin.setOccupation(row.getCell(3).toString());
				admin.setExperience(row.getCell(4).toString());
				admin.setCurrentSalary(row.getCell(5).toString());
				admin.setCurrentEmployer(row.getCell(6).toString());
				admin.setEmployerPlace(row.getCell(7).toString());
				admin.setAadharNumber(row.getCell(8).toString());
				admin.setCurrentAddress(row.getCell(10).toString());
				admin.setPermanentAddress(row.getCell(11).toString());
				admin.setPremiumUser(row.getCell(13).toString().equalsIgnoreCase("Y") ? true : false);
				break;
			}
		}
		return admin;

	}
	
	@Override
	public String findAdmin(String PhoneNumber) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString()))).getSheet("Admin");
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
				return "Success";
			}
		}
		return "Failure";
	}

}
