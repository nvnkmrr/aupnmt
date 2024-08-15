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
import com.aupnmt.dto.Employee;
import com.aupnmt.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Override
	public String employee(Employee employee) throws IOException, URISyntaxException {

		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		File file = new File(database.toString());
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
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

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return "Successfully created new employee with Id:" + newId;
	}

	@Override
	public Employee employee(String PhoneNumber) throws IOException, URISyntaxException {

		Employee employee = new Employee();
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString()))).getSheet("Employee");
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
	public String findEmployee(String PhoneNumber) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString()))).getSheet("Employee");
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
