package com.example.studyagent.controller;

import com.example.studyagent.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> upload(@RequestParam("file") MultipartFile file) {

        List<String> chunks = documentService.extractText(file);
        return ResponseEntity.ok(chunks);
    }
}