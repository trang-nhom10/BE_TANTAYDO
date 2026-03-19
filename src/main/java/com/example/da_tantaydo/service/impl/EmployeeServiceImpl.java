package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.*;
import com.example.da_tantaydo.model.dto.response.EmployeeResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.Status;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaStorageService mediaStorageService;

    @Override
    public EmployeeResponseDTO create(EmployeeCreateDTO request) {
        if (employeeRepository.existsByUserGmail(request.getEmail()))
            throw new RuntimeException("Email already exists.");
        if (employeeRepository.existsByCccd(request.getCccd()))
            throw new RuntimeException("CCCD already exists.");
        if (employeeRepository.existsByPhone(request.getPhone()))
            throw new RuntimeException("Phone number already exists.");

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found."));

        User user = User.builder()
                .gmail(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(Status.ACTIVE)
                .build();
        userRepository.save(user);

        Employee employee = Employee.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .date(request.getDate())
                .address(request.getAddress())
                .cccd(request.getCccd())
                .build();

        return toDTO(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponseDTO updateRole(Long id, EmployeeUpdateRoleDTO request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found."));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found."));

        employee.getUser().setRole(role);
        userRepository.save(employee.getUser());

        return toDTO(employeeRepository.save(employee));
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found."));

        employeeRepository.delete(employee);
        userRepository.delete(employee.getUser());
    }

    @Override
    public EmployeeResponseDTO updateProfile(String gmail, EmployeeRequestDTO request,
                                             MultipartFile img) {
        Employee employee = employeeRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Employee not found."));

        employee.setFullName(request.getFullName());
        employee.setPhone(request.getPhone());
        employee.setGender(request.getGender());
        employee.setDate(request.getDate());
        employee.setAddress(request.getAddress());
        employee.setCccd(request.getCccd());

        if (img != null && !img.isEmpty()) {
            if (employee.getImg() != null) {
                mediaStorageService.deleteMedia(Long.parseLong(employee.getImg()));
            }
            String mediaId = mediaStorageService.uploadMedia(img);
            employee.setImg(mediaId);
        }

        return toDTO(employeeRepository.save(employee));
    }

    @Override
    public Page<EmployeeResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<EmployeeResponseDTO> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRepository.search(keyword, pageable).map(this::toDTO);
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        return toDTO(employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found.")));
    }

    private EmployeeResponseDTO toDTO(Employee e) {
        return EmployeeResponseDTO.builder()
                .id(e.getId())
                .email(e.getUser().getGmail())
                .fullName(e.getFullName())
                .phone(e.getPhone())
                .gender(e.getGender())
                .date(e.getDate())
                .address(e.getAddress())
                .cccd(e.getCccd())
                .img(e.getImg())
                .roleName(e.getUser().getRole().getRoleName())
                .createdAt(e.getCreatedAt())
                .build();
    }
}