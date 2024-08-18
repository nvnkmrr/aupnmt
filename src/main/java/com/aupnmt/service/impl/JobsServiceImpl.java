package com.aupnmt.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aupnmt.AupnmtApplication;
import com.aupnmt.dto.Jobs;
import com.aupnmt.service.JobsService;

@Service
public class JobsServiceImpl implements JobsService {

	@Autowired
	AzureStorageServiceImpl azureStorageServiceImpl;

	@Override
	public String job(Jobs jobs) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		File file = new File(database.toString());
		Workbook workbook = WorkbookFactory.create(new FileInputStream(database.toString()));
		Sheet sheet = workbook.getSheet("Jobs");
		int lastRowNumber = sheet.getLastRowNum();
		Long newId = 0L;

		if (sheet.getRow(lastRowNumber).getCell(0).getCellType() == CellType.STRING) {
			newId = 1L;
		} else {
			newId = (new Double(sheet.getRow(lastRowNumber).getCell(0).getNumericCellValue()).longValue()) + 1;
		}
		Row row = sheet.createRow(lastRowNumber + 1);
		row.createCell(0).setCellValue(newId);
		row.createCell(1).setCellValue(jobs.getJobPostedBy());
		row.createCell(2).setCellValue(jobs.getJobDetails());
		row.createCell(3).setCellValue("Y");
		row.createCell(4).setCellValue(jobs.getCreatedDt());
		row.createCell(5).setCellValue(jobs.getModifiedDt());

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return "Successfully created new job with Id:" + newId;
	}

	@Override
	public List<Jobs> jobs(boolean flag) throws Exception {
		List<Jobs> jobs = new ArrayList<Jobs>();
		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Jobs");
		Iterator<Row> rowIterator = xSSFSheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}

			Row row = (Row) rowIterator.next();
			if (flag) {
				if (row.getCell(3).toString().equalsIgnoreCase("Y")) {
					Jobs job = new Jobs();
					job.setJobId(new Double(row.getCell(0).getNumericCellValue()).longValue());
					job.setJobPostedBy(row.getCell(1).toString());
					job.setJobDetails(row.getCell(2).toString());
					job.setActive(row.getCell(3).toString().equalsIgnoreCase("Y") ? true : false);
					jobs.add(job);
				}
			} else {
				if (row.getCell(3).toString().equalsIgnoreCase("N")) {
					Jobs job = new Jobs();
					job.setJobId(new Double(row.getCell(0).getNumericCellValue()).longValue());
					job.setJobPostedBy(row.getCell(1).toString());
					job.setJobDetails(row.getCell(2).toString());
					job.setActive(row.getCell(3).toString().equalsIgnoreCase("Y") ? true : false);
					jobs.add(job);
				}
			}
		}
		return jobs;
	}

	@Override
	public List<Jobs> jobs(String phoneNumber) throws Exception {
		List<Jobs> jobs = new ArrayList<Jobs>();
		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Jobs");
		Iterator<Row> rowIterator = xSSFSheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}

			Row row = (Row) rowIterator.next();
			if (row.getCell(1).toString().equalsIgnoreCase(phoneNumber)) {
				Jobs job = new Jobs();
				job.setJobId(new Double(row.getCell(0).getNumericCellValue()).longValue());
				job.setJobPostedBy(row.getCell(1).toString());
				job.setJobDetails(row.getCell(2).toString());
				job.setActive(row.getCell(3).toString().equalsIgnoreCase("Y") ? true : false);
				jobs.add(job);
			}
		}
		return jobs;
	}

	@Override
	public String delete(Long jobId) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		File file = new File(database.toString());
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = workbook.getSheet("Jobs");
		Iterator<Row> rowIterator = sheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}

			Row row = (Row) rowIterator.next();
			Long id = new Double(row.getCell(0).getNumericCellValue()).longValue();
			if (id.equals(jobId)) {
				row.getCell(3).setCellValue("N");
			}
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return "Successfully deleted the job with Id:" + jobId;
	}

	@Override
	public String approve(Long jobId) throws IOException, URISyntaxException {
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		File file = new File(database.toString());
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = workbook.getSheet("Jobs");
		Iterator<Row> rowIterator = sheet.iterator();
		boolean skipHeader = true;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = false;
			}

			Row row = (Row) rowIterator.next();
			Long id = new Double(row.getCell(0).getNumericCellValue()).longValue();
			if (id.equals(jobId)) {
				row.getCell(3).setCellValue("Y");
			}
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return "Successfully approved the job with Id:" + jobId;
	}
}
