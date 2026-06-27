package com.ecommerce.ecomapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadImage(String path, MultipartFile file) throws IOException;
}
