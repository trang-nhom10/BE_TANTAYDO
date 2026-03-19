package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.entity.Customer;
import com.example.da_tantaydo.model.entity.Doctor;
import com.example.da_tantaydo.model.entity.Role;
import com.example.da_tantaydo.model.entity.User;
import com.example.da_tantaydo.model.enums.Status;
import com.example.da_tantaydo.model.dto.response.LoginReponseDTO;
import com.example.da_tantaydo.model.dto.request.LoginRequestDto;
import com.example.da_tantaydo.model.dto.request.RegisterRequestDTO;
import com.example.da_tantaydo.repository.CustomerRepository;
import com.example.da_tantaydo.repository.DoctorRepository;
import com.example.da_tantaydo.repository.RoleRepository;
import com.example.da_tantaydo.repository.UserRepository;
import com.example.da_tantaydo.security.JwtUtil;
import com.example.da_tantaydo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;




    @Override
    public LoginReponseDTO login(LoginRequestDto request) {
        User user = userRepository.findByGmail(request.getGmail()).orElseThrow(() -> new BadCredentialsException("Gmail is incorrect"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }
        if (user.getRole() == null) {
            throw new BadCredentialsException("User role is not assigned");
        }
        String token = jwtUtil.generateToken(user);
        return new LoginReponseDTO(token);
    }
    @Transactional
    @Override
    public User register(RegisterRequestDTO request) {
        if (userRepository.findByGmail(request.getGmail()).isPresent()) {
            throw new BadCredentialsException("Gmail already exists");
        }

        Long roleId = request.getRole();
        if (roleId == null || (!roleId.equals(3L) && !roleId.equals(4L))) {
            throw new IllegalArgumentException("Chỉ cho phép đăng ký CUSTOMER(4) hoặc MERCHANT(4)");
        }

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        User user = new User();
        user.setGmail(request.getGmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole(role);
        User savedUser = userRepository.save(user);
        if (roleId.equals(3L)) {
            Customer customer = new Customer();
            customer.setUser(savedUser);
            customerRepository.save(customer);
        }
        if (roleId.equals(4L)) {
            Doctor merchant = new Doctor();
            merchant.setUser(savedUser);
            doctorRepository.save(merchant);
        }

        return savedUser;
    }
}
