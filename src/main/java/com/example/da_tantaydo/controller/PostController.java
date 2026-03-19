package com.example.da_tantaydo.controller;
import com.example.da_tantaydo.model.dto.request.PostRequestDTO;
import com.example.da_tantaydo.model.dto.response.PostResponseDTO;
import com.example.da_tantaydo.model.enums.PostType;
import com.example.da_tantaydo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<PostResponseDTO> create(
            @RequestBody PostRequestDTO request) {
        return ResponseEntity.ok(postService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<PostResponseDTO> update(
            @PathVariable Long id,
            @RequestBody PostRequestDTO request) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok("Xóa bài viết thành công");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    // LẤY TẤT CẢ - MỚI NHẤT TRƯỚC - PHÂN TRANG
    // GET /api/posts?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAll(page, size));
    }

    // LẤY THEO TRẠNG THÁI - MỚI NHẤT TRƯỚC
    // GET /api/posts/type?type=PUBLISHED&page=0&size=10
    @GetMapping("/type")
    public ResponseEntity<Page<PostResponseDTO>> getByType(
            @RequestParam PostType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getByType(type, page, size));
    }
}