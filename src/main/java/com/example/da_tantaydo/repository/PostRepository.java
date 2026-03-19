package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Post;
import com.example.da_tantaydo.model.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // LẤY BÀI VIẾT MỚI NHẤT (DÙNG PAGEABLE ĐỂ PHÂN TRANG)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // LẤY BÀI VIẾT THEO TRẠNG THÁI, MỚI NHẤT TRƯỚC
    Page<Post> findByTypeOrderByCreatedAtDesc(PostType type, Pageable pageable);

    // LẤY BÀI VIẾT THEO NHÂN VIÊN TẠO
    List<Post> findByCreatedByIdOrderByCreatedAtDesc(Long employeeId);
}