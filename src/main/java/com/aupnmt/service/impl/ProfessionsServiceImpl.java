package com.aupnmt.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aupnmt.AupnmtApplication;
import com.aupnmt.dto.Professions;
import com.aupnmt.service.ProfessionsService;

@Service
public class ProfessionsServiceImpl implements ProfessionsService {

	@Value("${database.excel.file}")
	private String databaseFile;

	@Override
	public List<Professions> professions() throws IOException, URISyntaxException {

		@SuppressWarnings("resource")
		Path database = ((Paths.get(AupnmtApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getParent().normalize().toAbsolutePath()).getParent().normalize().toAbsolutePath())
				.resolve("Database.xlsx");
		XSSFSheet xSSFSheet = new XSSFWorkbook(new FileInputStream(new File(database.toString())))
				.getSheet("Professions");
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
