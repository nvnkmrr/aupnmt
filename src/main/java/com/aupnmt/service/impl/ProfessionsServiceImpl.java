package com.aupnmt.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aupnmt.dto.Professions;
import com.aupnmt.service.ProfessionsService;

@Service
public class ProfessionsServiceImpl implements ProfessionsService {

	@Autowired
	AzureStorageServiceImpl azureStorageServiceImpl;

	@Override
	public List<Professions> professions() throws Exception {

		XSSFWorkbook workbook = azureStorageServiceImpl.readDatabase();
		XSSFSheet xSSFSheet = workbook.getSheet("Professions");
		List<Professions> data = new ArrayList<>();
		Iterator<Row> rowIterator = xSSFSheet.iterator();
		boolean skipHeader = false;
		while (rowIterator.hasNext()) {
			// skipping header
			if (skipHeader) {
				rowIterator.next();
				skipHeader = true;
			}
			Row row = rowIterator.next();
			if (row.getCell(4).toString().equalsIgnoreCase("Y")) {
				Professions professions = new Professions();
				professions.setProfessionId((new Double(row.getCell(0).getNumericCellValue())).longValue());
				professions.setProfession(row.getCell(1).toString());
				professions.setOwnerProfession(row.getCell(2).toString().equalsIgnoreCase("Y") ? true : false);
				professions.setEmployeeProfession(row.getCell(3).toString().equalsIgnoreCase("Y") ? true : false);
				data.add(professions);
			}
		}
		return data;
	}

}
