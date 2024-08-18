package com.aupnmt.service.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aupnmt.service.AzureStorageService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class AzureStorageServiceImpl implements AzureStorageService {

	@Value("${azure.storage.blob.endpoint}")
	private String storageEndPoint;

	@Value("${azure.storage.blob.container}")
	private String storageContainer;

	@Value("${azure.storage.blob.filename}")
	private String filename;

	@Override
	public XSSFWorkbook readDatabase() throws Exception {

		//BlobContainerSasPermission blobContainerSasPermission = new BlobContainerSasPermission().setReadPermission(true)
		//		.setWritePermission(true).setListPermission(true);
		//BlobServiceSasSignatureValues builder = new BlobServiceSasSignatureValues(OffsetDateTime.now().plusDays(1),
		//		blobContainerSasPermission).setProtocol(SasProtocol.HTTPS_ONLY);

		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageEndPoint).buildClient();
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainer);
		BlobClient blobClient = containerClient.getBlobClient(filename);
		//blobClient.generateSas(builder);
		return new XSSFWorkbook(blobClient.openInputStream());
	}

}
