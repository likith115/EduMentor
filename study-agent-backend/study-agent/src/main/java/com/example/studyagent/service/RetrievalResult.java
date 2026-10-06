package com.example.studyagent.service;

import com.example.studyagent.entity.DocumentChunk;

public class RetrievalResult {

    private final DocumentChunk chunk;
    private final double distance;

    public RetrievalResult(DocumentChunk chunk, double distance) {
        this.chunk = chunk;
        this.distance = distance;
    }

    public DocumentChunk getChunk() {
        return chunk;
    }

    public double getDistance() {
        return distance;
    }
}