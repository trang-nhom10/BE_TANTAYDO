package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.CategoryRequestDTO;
import com.example.da_tantaydo.model.dto.response.CategoryResponseDTO;
import java.util.List;

public interface CategoryService {
    CategoryResponseDTO create(CategoryRequestDTO request);
    CategoryResponseDTO update(Long id, CategoryRequestDTO request);
    void delete(Long id);
    CategoryResponseDTO getById(Long id);
    List<CategoryResponseDTO> getAll();
}