package com.example.studyagent.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunker {

    private static final int CHUNK_SIZE = 100;

    public List<String> chunk(String text) {

        List<String> chunks = new ArrayList<>();

        for (int start = 0; start < text.length(); start += CHUNK_SIZE) {

            int end = Math.min(start + CHUNK_SIZE, text.length());

            String chunk = text.substring(start, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }
        }

        return chunks;
    }
}