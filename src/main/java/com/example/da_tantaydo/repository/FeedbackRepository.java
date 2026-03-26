package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = """
        SELECT f.id, f.full_name, f.gmail, f.sick, f.text, f.created_at, f.evaluate,
               ds.media_url as imageUrl
        FROM feedback f
        LEFT JOIN customers c ON c.user_id = (SELECT id FROM users WHERE gmail = f.gmail)
        LEFT JOIN data_souses ds ON ds.id = CAST(c.img AS UNSIGNED)
        ORDER BY f.created_at DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5WithImage();
}