package com.restaurant.chatbot.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OCRService {

    public String extractText(File file) {
        // TODO: Implement OCR using Tesseract or another library
        // For now, return a placeholder
        return "Extracted text from image: " + file.getName();
    }
}
