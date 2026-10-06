package com.example.studyagent.service;

import com.example.studyagent.entity.DocumentChunk;
import com.example.studyagent.repository.DocumentChunkRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final TextChunker textChunker;
    private final DocumentChunkRepository repository;
    private final EmbeddingService embeddingService;

    public DocumentService(
            TextChunker textChunker,
            DocumentChunkRepository repository,
            EmbeddingService embeddingService) {

        this.textChunker = textChunker;
        this.repository = repository;
        this.embeddingService = embeddingService;
    }

    public List<String> extractText(MultipartFile file) {

        repository.deleteAll();

        List<String> allChunks = new ArrayList<>();

        try {
            byte[] pdfBytes = file.getBytes();
            PDDocument document = Loader.loadPDF(pdfBytes);
            PDFTextStripper stripper = new PDFTextStripper();

            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                String text = stripper.getText(document);
                List<String> chunks = textChunker.chunk(text);
                for (String chunk : chunks) {
                    float[] embedding = embeddingService.createEmbedding(chunk);
                    DocumentChunk documentChunk = new DocumentChunk(chunk, page);
                    documentChunk.setEmbedding(embedding);
                    repository.save(documentChunk);
                    allChunks.add(chunk);
                }
            }
            document.close();
            return allChunks;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF", e);
        }
    }
}