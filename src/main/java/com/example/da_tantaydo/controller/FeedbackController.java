package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.FeedbackRequestDTO;
import com.example.da_tantaydo.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody FeedbackRequestDTO request,
            Authentication authentication) {
        String gmail = authentication.getName();
        feedbackService.create(gmail, request);
        return ResponseEntity.ok("Feedback created successfully");
    }

    @GetMapping("/top5")
    public ResponseEntity<?> getTop5() {
        return ResponseEntity.ok(feedbackService.getTop5());
    }
}