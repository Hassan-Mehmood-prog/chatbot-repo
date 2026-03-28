package com.restaurant.chatbot.controller;

import com.restaurant.chatbot.model.Menu;
import com.restaurant.chatbot.repository.MenuRepository;
import com.restaurant.chatbot.service.OCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuRepository menuRepository;
    private final OCRService ocrService;

    @PostMapping("/upload")
    public String uploadMenu(@RequestParam("file") MultipartFile file) throws IOException {

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        String text = ocrService.extractText(convFile);

        // Save in DB
        Menu menu = new Menu();
        menu.setText(text);
        menuRepository.save(menu);

        return "Menu uploaded successfully!";
    }

    @GetMapping
    public List<Menu> getMenus() {
        return menuRepository.findAll();
    }
}