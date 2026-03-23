package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Post;
import com.example.da_tantaydo.model.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByTitleContainingIgnoreCaseAndStatus(String title, PostType status);
    List<Post> findByTitleContainingIgnoreCase(String title);
    List<Post> findByStatus(PostType status);
}