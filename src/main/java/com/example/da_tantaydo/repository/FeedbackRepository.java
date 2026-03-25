package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findTop5ByOrderByCreatedAtDesc();
}