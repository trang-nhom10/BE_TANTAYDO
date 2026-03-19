package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.PostRequestDTO;
import com.example.da_tantaydo.model.dto.response.PostResponseDTO;
import com.example.da_tantaydo.model.enums.PostType;
import org.springframework.data.domain.Page;

public interface PostService {
    PostResponseDTO create(PostRequestDTO request);
    PostResponseDTO update(Long id, PostRequestDTO request);
    void delete(Long id);
    PostResponseDTO getById(Long id);
    Page<PostResponseDTO> getAll(int page, int size);
    Page<PostResponseDTO> getByType(PostType type, int page, int size);
}