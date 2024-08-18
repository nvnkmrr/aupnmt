package com.aupnmt.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aupnmt.dto.Jobs;
import com.aupnmt.service.JobsService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class JobsServiceImpl implements JobsService {

	@Autowired
	AzureStorageServiceImpl azureStorageServiceImpl;
	
	@Value("${azure.storage.blob.endpoint}")
	private String storageEndPoint;

	@Value("${azure.storage.blob.container}")
	private String storageContainer;

	@Value("${azure.storage.blob.filename}")
	private String filename;

	@Override
	public String job(Jobs jobs) throws IOException, URISyntaxException {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageEndPoint).buildClient();
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainer);
		BlobClient blobClient = containerClient.getBlobClient(filename);
		XSSFWorkbook workbook = new XSSFWorkbook(blobClient.openInputStream());
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
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		blobClient.upload(new ByteArrayInputStream(bos.toByteArray()));
		workbook.close();
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
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageEndPoint).buildClient();
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainer);
		BlobClient blobClient = containerClient.getBlobClient(filename);
		XSSFWorkbook workbook = new XSSFWorkbook(blobClient.openInputStream());
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
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		blobClient.upload(new ByteArrayInputStream(bos.toByteArray()));
		workbook.close();
		return "Successfully deleted the job with Id:" + jobId;
	}

	@Override
	public String approve(Long jobId) throws IOException, URISyntaxException {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageEndPoint).buildClient();
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainer);
		BlobClient blobClient = containerClient.getBlobClient(filename);
		XSSFWorkbook workbook = new XSSFWorkbook(blobClient.openInputStream());
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
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		blobClient.upload(new ByteArrayInputStream(bos.toByteArray()));
		workbook.close();
		return "Successfully approved the job with Id:" + jobId;
	}
}
