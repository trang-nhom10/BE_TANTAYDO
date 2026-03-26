package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.FeedbackRequestDTO;
import com.example.da_tantaydo.model.dto.response.FeedbackResponseDTO;
import com.example.da_tantaydo.model.entity.Customer;
import com.example.da_tantaydo.model.entity.Feedback;
import com.example.da_tantaydo.repository.CustomerRepository;
import com.example.da_tantaydo.repository.FeedbackRepository;
import com.example.da_tantaydo.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void create(String gmail, FeedbackRequestDTO request) {
        Customer customer = customerRepository.findByUserGmail(gmail).orElseThrow(() -> new RuntimeException("Customer not found"));
        Feedback feedback = Feedback.builder()
                .fullName(customer.getFullName())
                .gmail(gmail)
                .sick(request.getSick())
                .evaluate(request.getEvaluate())
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .build();
        feedbackRepository.save(feedback);
    }

    @Override
    public List<FeedbackResponseDTO> getTop5() {
        return feedbackRepository.findTop5WithImage()
                .stream()
                .map(row -> FeedbackResponseDTO.builder()
                        .id(((Number) row[0]).longValue())
                        .fullName((String) row[1])
                        .gmail((String) row[2])
                        .sick((String) row[3])
                        .text((String) row[4])
                        .createdAt(row[5] != null ? ((LocalDateTime) row[5]) : null)
                        .evaluate(row[6] != null ? ((Number) row[6]).intValue() : null)
                        .imageUrl((String) row[7])
                        .build())
                .collect(Collectors.toList());
    }
}
