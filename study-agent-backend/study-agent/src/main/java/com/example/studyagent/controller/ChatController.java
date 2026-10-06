package com.example.studyagent.controller;

import com.example.studyagent.dto.AskRequest;
import com.example.studyagent.dto.AskResponse;
import com.example.studyagent.dto.GenerateQuestionRequest;
import com.example.studyagent.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest request) {

        String answer = chatService.ask(request.getQuestion());
        return new AskResponse(answer);
    }

    @PostMapping("/ask/generate-questions")
    public ResponseEntity<String> generateQuestions(@RequestBody GenerateQuestionRequest request) {

        return ResponseEntity.ok(chatService.generateQuestions(request.getPageNumber()));
    }
}