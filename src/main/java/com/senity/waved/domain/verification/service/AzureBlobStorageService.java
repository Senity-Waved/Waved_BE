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
        String fileExtension = getFileExtension(fileName);

        // 확장자가 없는 경우 기본적으로 jpg로
        if (fileExtension.isEmpty()) {
            fileName += ".jpg";
        }

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName + "." + fileExtension);
        blobClient.upload(new ByteArrayInputStream(pictureData), pictureData.length);
        return blobClient.getBlobUrl();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}
