package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.CategoryRequestDTO;
import com.example.da_tantaydo.model.dto.response.CategoryResponseDTO;
import com.example.da_tantaydo.model.entity.Category;
import com.example.da_tantaydo.repository.CategoryRepository;
import com.example.da_tantaydo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        if (categoryRepository.existsByName(request.getName()))
            throw new RuntimeException("Category name already exists.");

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id))
            throw new RuntimeException("Category not found.");
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO getById(Long id) {
        return toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found.")));
    }

    @Override
    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private CategoryResponseDTO toDTO(Category c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .createdAt(c.getCreatedAt())
                .build();
    }
}