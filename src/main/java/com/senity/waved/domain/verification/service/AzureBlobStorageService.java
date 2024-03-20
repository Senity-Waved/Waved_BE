package com.senity.waved.domain.verification.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class AzureBlobStorageService {

    @Value("${azure.storage.container-name}")
    private String containerName;

    private final BlobServiceClient blobServiceClient;

    public String uploadPicture(byte[] pictureData, String fileName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(new ByteArrayInputStream(pictureData), pictureData.length);
        return blobClient.getBlobUrl();
    }
}