package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.FeedbackRequestDTO;
import com.example.da_tantaydo.model.dto.response.FeedbackResponseDTO;
import java.util.List;

public interface FeedbackService {
    void create(String gmail, FeedbackRequestDTO request);
    List<FeedbackResponseDTO> getTop5();
}