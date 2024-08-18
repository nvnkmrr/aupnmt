package com.aupnmt.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public interface AzureStorageService {

	XSSFWorkbook readDatabase() throws Exception;

}
