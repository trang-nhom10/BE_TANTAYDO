package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.*;
import com.example.da_tantaydo.model.dto.response.EmployeeResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.Gender;
import com.example.da_tantaydo.model.enums.Status;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaStorageService mediaStorageService;
    private final DoctorRepository doctorRepository;

    @Transactional
    @Override
    public void create(EmployeeCreateDTO request) {
        if (userRepository.findByGmail(request.getGmail()).isPresent()) throw new BadCredentialsException("Gmail already exists");
        Role role = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Role not found."));
        User user = new User();
        user.setGmail(request.getGmail());
        user.setPassword(passwordEncoder.encode("8888@"));
        user.setRole(role);
        user.setStatus(Status.ACTIVE);
            User saved = userRepository.save(user);
            Employee employee = new Employee();
            employee.setFullName(request.getName());
            employee.setGender(Gender.valueOf(request.getGender()));
            employee.setUser(saved);
            employeeRepository.save(employee);

}

    @Override
    public void updateRole(Long id, EmployeeUpdateRoleDTO request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found."));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found."));

        employee.getUser().setRole(role);
        userRepository.save(employee.getUser());

        employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found."));
        Long userId = employee.getUser().getId();
        employeeRepository.delete(employee);
        userRepository.deleteById(userId);
    }

    @Override
    public List<EmployeeResponseDTO> updateProfile(Authentication authentication, EmployeeRequestDTO request, MultipartFile img) {
        String authen = authentication.getName();
        Employee employee = employeeRepository.findByUserGmail(authen).orElseThrow(() -> new RuntimeException("Employee not found."));
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

       employeeRepository.save(employee);
        return null;
    }

    @Override
    public List<EmployeeResponseDTO> getAll() {
        return employeeRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<EmployeeResponseDTO> search(String fullName, String address) {
        return employeeRepository.search(fullName, address)
                .stream()
                .map(this::toDTO)
                .toList();
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