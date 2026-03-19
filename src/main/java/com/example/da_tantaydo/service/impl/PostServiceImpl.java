package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.PostRequestDTO;
import com.example.da_tantaydo.model.dto.response.PostResponseDTO;
import com.example.da_tantaydo.model.entity.Employee;
import com.example.da_tantaydo.model.entity.Post;
import com.example.da_tantaydo.model.enums.PostType;
import com.example.da_tantaydo.repository.EmployeeRepository;
import com.example.da_tantaydo.repository.PostRepository;
import com.example.da_tantaydo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public PostResponseDTO create(PostRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .createdBy(employee)
                .build();

        return toDTO(postRepository.save(post));
    }

    @Override
    public PostResponseDTO update(Long id, PostRequestDTO request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

        Employee employee = employeeRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setType(request.getType());
        post.setCreatedBy(employee);

        return toDTO(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        if (!postRepository.existsById(id))
            throw new RuntimeException("Không tìm thấy bài viết");
        postRepository.deleteById(id);
    }

    @Override
    public PostResponseDTO getById(Long id) {
        return toDTO(postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết")));
    }

    @Override
    public Page<PostResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDTO);
    }

    @Override
    public Page<PostResponseDTO> getByType(PostType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByTypeOrderByCreatedAtDesc(type, pageable)
                .map(this::toDTO);
    }

    private PostResponseDTO toDTO(Post p) {
        return PostResponseDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .type(p.getType())
                .createdByName(p.getCreatedBy() != null ? p.getCreatedBy().getFullName() : null)
                .createdAt(p.getCreatedAt())
                .build();
    }
}