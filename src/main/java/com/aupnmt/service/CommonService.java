package com.aupnmt.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.aupnmt.dto.AccessToken;

@Service
public interface CommonService {

	AccessToken userIdentification(String phoneNumber, XSSFWorkbook workbook) throws Exception;
	
	String userIdentificationAndDelete(String phoneNumber, XSSFWorkbook workbook) throws Exception;

}
