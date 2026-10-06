package com.example.studyagent.controller;

import com.example.studyagent.entity.DocumentChunk;
import com.example.studyagent.service.RetrievalResult;
import com.example.studyagent.service.RetrievalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class RetrievalController {

    private final RetrievalService retrievalService;

    public RetrievalController(RetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @GetMapping
    public List<RetrievalResult> search(@RequestParam String question) {
        return retrievalService.search(question);
    }
}