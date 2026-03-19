package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.*;
import com.example.da_tantaydo.model.dto.response.EmployeeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    // ADMIN
    EmployeeResponseDTO create(EmployeeCreateDTO request);
    EmployeeResponseDTO updateRole(Long id, EmployeeUpdateRoleDTO request);
    void delete(Long id);
    Page<EmployeeResponseDTO> getAll(int page, int size);
    Page<EmployeeResponseDTO> search(String keyword, int page, int size);
    EmployeeResponseDTO getById(Long id);
    EmployeeResponseDTO updateProfile(String gmail, EmployeeRequestDTO request, MultipartFile img);
}