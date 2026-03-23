package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.PostRequestDTO;
import com.example.da_tantaydo.model.dto.response.PostResponseDTO;
import com.example.da_tantaydo.model.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    void create(PostRequestDTO request, MultipartFile img);
    void update(Long id, PostRequestDTO request,MultipartFile img);
    void delete(Long id);
    List<PostResponseDTO> getAll();
    List<PostResponseDTO> search(String title, PostType status);
}