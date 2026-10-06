package com.example.studyagent.service;

import com.example.studyagent.entity.DocumentChunk;
import com.example.studyagent.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetrievalService {

    private final EmbeddingService embeddingService;
    private final VectorUtils vectorUtils;
    private final DocumentChunkRepository repository;

    public RetrievalService(
            EmbeddingService embeddingService,
            VectorUtils vectorUtils,
            DocumentChunkRepository repository) {

        this.embeddingService = embeddingService;
        this.vectorUtils = vectorUtils;
        this.repository = repository;
    }

    public List<RetrievalResult> search(String question) {

        float[] questionEmbedding = embeddingService.createEmbedding(question);

        String vector = vectorUtils.toVectorString(questionEmbedding);

        List<Object[]> results = repository.findSimilarChunks(vector, 5);

        List<RetrievalResult> retrievalResults = new ArrayList<>();

        for (Object[] result : results) {

            DocumentChunk chunk = new DocumentChunk();

            chunk.setContent((String) result[1]);

            chunk.setPageNumber(((Number) result[3]).intValue());

            double distance = ((Number) result[4]).doubleValue();

            retrievalResults.add(new RetrievalResult(chunk, distance));
        }

        return retrievalResults;
    }
}