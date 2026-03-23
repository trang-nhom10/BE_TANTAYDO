package com.example.da_tantaydo.service.impl;
import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.PostRequestDTO;
import com.example.da_tantaydo.model.dto.response.PostResponseDTO;
import com.example.da_tantaydo.model.entity.Post;
import com.example.da_tantaydo.model.enums.PostType;
import com.example.da_tantaydo.repository.DataSourceRepository;
import com.example.da_tantaydo.repository.EmployeeRepository;
import com.example.da_tantaydo.repository.PostRepository;
import com.example.da_tantaydo.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MediaStorageService mediaStorageService;
    private final DataSourceRepository dataSourceRepository;

    @Override
    public void create(PostRequestDTO request , MultipartFile img) {
        Post post= new Post();
        post.setTitle( request.getTitle());
        post.setContent(request.getContent());
        post.setStatus(PostType.DRAFT);
        post.setCreatedAt(LocalDateTime.now());

        if (img != null && !img.isEmpty()) {
            if (post.getImg() != null) {
                mediaStorageService.deleteMedia(Long.parseLong(post.getImg()));
            }
            String mediaId = mediaStorageService.uploadMedia(img);
            post.setImg(mediaId);
        }
        postRepository.save(post);
    }

    @Override
    public void update(Long id, PostRequestDTO request, MultipartFile img) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found."));

        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getStatus() != null) post.setContent(request.getContent());

        if (img != null && !img.isEmpty()) {
            if (post.getImg() != null) {
                mediaStorageService.deleteMedia(Long.parseLong(post.getImg()));
            }
            String mediaId = mediaStorageService.uploadMedia(img);
            post.setImg(mediaId);
        }

        post.setUpdateAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found."));
        if (post.getImg() != null) {
            mediaStorageService.deleteMedia(Long.parseLong(post.getImg()));
        }
        postRepository.delete(post);
    }


    @Override
    public List<PostResponseDTO> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<PostResponseDTO> search(String title, PostType status) {
        List<Post> posts;

        if (title != null && status != null) {
            posts = postRepository.findByTitleContainingIgnoreCaseAndStatus(title, status);
        } else if (title != null) {
            posts = postRepository.findByTitleContainingIgnoreCase(title);
        } else if (status != null) {
            posts = postRepository.findByStatus(status);
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        return posts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO toDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setStatus(String.valueOf(post.getStatus()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setPublishedAt(post.getPublishedAt());

        if (post.getImg() != null && post.getImg().matches("\\d+")) {
            dataSourceRepository.findById(Long.valueOf(post.getImg()))
                    .ifPresent(ds -> dto.setImg(ds.getImageUrl()));
        } else {
            dto.setImg(post.getImg());
        }

        return dto;
    }
}