package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.CategoryRequestDTO;
import com.example.da_tantaydo.model.dto.response.CategoryResponseDTO;
import com.example.da_tantaydo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<?> create(
            @RequestBody CategoryRequestDTO request) {
        categoryService.create(request);
         return ResponseEntity.ok("create success");
    }

    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO request) {
        categoryService.update(id, request);
        return ResponseEntity.ok("update success");
    }

    @PostMapping("delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_WEB')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("delete success");
    }


    @GetMapping("/getall")
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}