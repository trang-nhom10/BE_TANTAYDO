package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Post;
import com.example.da_tantaydo.model.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByTypeOrderByCreatedAtDesc(PostType type, Pageable pageable);
    List<Post> findByCreatedByIdOrderByCreatedAtDesc(Long employeeId);
}