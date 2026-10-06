package com.example.studyagent.service;

import com.example.studyagent.entity.DocumentChunk;
import com.example.studyagent.repository.DocumentChunkRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final RetrievalService retrievalService;
    private final DocumentChunkRepository repository;

    public ChatService(
            ChatClient.Builder builder,
            RetrievalService retrievalService, DocumentChunkRepository repository) {

        this.chatClient = builder.build();
        this.retrievalService = retrievalService;
        this.repository = repository;
    }

    public String ask(String question) {

        List<RetrievalResult> results = retrievalService.search(question);

        List<RetrievalResult> relevantResults = results.stream()
                        .filter(result -> result.getDistance() <= 0.40)
                        .toList();

        if (relevantResults.isEmpty()) {
            return "I couldn't find this information in the uploaded textbook.";
        }

        String context = buildContext(relevantResults);

        return chatClient.prompt().system("""
                    You are a textbook-based study assistant.

                    Answer ONLY using the provided textbook content.

                    Do not use outside knowledge.
                    Do not guess.
                    Do not make up information.

                    If the answer cannot be found in the
                    provided textbook content, say:

                    "I couldn't find this information in
                    the uploaded textbook."

                    Textbook content:
                    """ + context)
                .user(question)
                .call()
                .content();
    }

    public String generateQuestions(int pageNumber) {

        List<DocumentChunk> chunks = repository.findByPageNumber(pageNumber);

        if (chunks.isEmpty()) {
            return "No content found for this page.";
        }

        StringBuilder context = new StringBuilder();

        for (DocumentChunk chunk : chunks) {
            context.append(chunk.getContent());
            context.append("\n\n");
        }

        return chatClient
                .prompt().system("""
                    You are a textbook-based study assistant.

                    Generate questions ONLY from the provided
                    textbook content.

                    Do not use outside knowledge.
                    Do not guess.
                    Do not make up information.

                    Generate 2 questions.

                    Do not provide answers.

                    Textbook content:

                    """ + context)
                .user("Generate 2 questions from this page.")
                .call()
                .content();
    }

    private String buildContext(List<RetrievalResult> results) {

        StringBuilder context = new StringBuilder();
        for (RetrievalResult result : results) {
            context.append("Page ").append(result.getChunk().getPageNumber()).append(":\n");
            context.append(result.getChunk().getContent());
            context.append("\n\n");
        }
        return context.toString();
    }
}