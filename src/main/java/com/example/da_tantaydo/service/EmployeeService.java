package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.*;
import com.example.da_tantaydo.model.dto.response.EmployeeResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface EmployeeService {
    void create(EmployeeCreateDTO request);
    void updateRole(Long id, EmployeeUpdateRoleDTO request);
    void delete(Long id);
    List<EmployeeResponseDTO> getAll();
    List<EmployeeResponseDTO> search(String fullName,String address);
    List<EmployeeResponseDTO> updateProfile(Authentication authentication, EmployeeRequestDTO request, MultipartFile img);
}