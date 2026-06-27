package com.ecommerce.ecomapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String newFileName = randomId.concat(extension);
        Path uploadPath = Paths.get(path);

        // Create directory if it doesn't exist
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path destination = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), destination);

        return newFileName;
    }
}
