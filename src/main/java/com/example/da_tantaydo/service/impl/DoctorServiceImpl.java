package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.DoctorCreateDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.Status;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AppointmentRepository appointmentRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaStorageService mediaStorageService;

    @Override
    public DoctorResponseDTO create(DoctorCreateDTO request) {
        if (doctorRepository.existsByUserGmail(request.getEmail()))
            throw new RuntimeException("Email already exists.");
        if (doctorRepository.existsByPhone(request.getPhone()))
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

        Doctor doctor = Doctor.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .specialized(request.getSpecialized())
                .information(request.getInformation())
                .address(request.getAddress())
                .lever(request.getLever())
                .build();

        return toDTO(doctorRepository.save(doctor));
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));
        doctorRepository.delete(doctor);
        userRepository.delete(doctor.getUser());
    }

    @Override
    public Page<DoctorResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return doctorRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<DoctorResponseDTO> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.search(keyword, pageable).map(this::toDTO);
    }

    @Override
    public DoctorResponseDTO getById(Long id) {
        return toDTO(doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found.")));
    }

    @Override
    public DoctorResponseDTO updateProfile(String gmail,
                                           DoctorProfileRequestDTO request,
                                           MultipartFile img) {
        Doctor doctor = doctorRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        doctor.setFullName(request.getFullName());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialized(request.getSpecialized());
        doctor.setInformation(request.getInformation());
        doctor.setAddress(request.getAddress());
        doctor.setLever(request.getLever());

        if (img != null && !img.isEmpty()) {
            if (doctor.getImg() != null) {
                mediaStorageService.deleteMedia(Long.parseLong(doctor.getImg()));
            }
            String mediaId = mediaStorageService.uploadMedia(img);
            doctor.setImg(mediaId);
        }

        return toDTO(doctorRepository.save(doctor));
    }

    @Override
    public Page<AppointmentResponseDTO> getMyAppointments(String gmail,
                                                          int page, int size) {
        Doctor doctor = doctorRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository
                .findByDoctorIdOrderByCreatedAtDesc(doctor.getId(), pageable)
                .map(a -> AppointmentResponseDTO.builder()
                        .id(a.getId())
                        .customerId(a.getCustomer().getId())
                        .customerName(a.getCustomer().getFullName())
                        .customerPhone(a.getCustomer().getPhone())
                        .scheduleId(a.getSchedule().getId())
                        .workDate(a.getSchedule().getWorkDate())
                        .startTime(a.getSchedule().getStartTime())
                        .endTime(a.getSchedule().getEndTime())
                        .service(a.getService())
                        .reason(a.getReason())
                        .note(a.getNote())
                        .status(a.getStatus())
                        .createdAt(a.getCreatedAt())
                        .build());
    }

    @Override
    public Page<OrderResponseDTO> getMyOrders(String gmail, int page, int size) {
        Doctor doctor = doctorRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        Pageable pageable = PageRequest.of(page, size);
        return orderRepository
                .findByDoctorIdOrderByCreatedAtDesc(doctor.getId(), pageable)
                .map(o -> OrderResponseDTO.builder()
                        .id(o.getId())
                        .customerId(o.getCustomer().getId())
                        .customerName(o.getCustomer().getFullName())
                        .customerPhone(o.getCustomer().getPhone())
                        .service(o.getService())
                        .totalPrice(o.getTotalPrice())
                        .status(o.getStatus())
                        .note(o.getNote())
                        .createdAt(o.getCreatedAt())
                        .build());
    }

    private DoctorResponseDTO toDTO(Doctor d) {
        return DoctorResponseDTO.builder()
                .id(d.getId())
                .email(d.getUser().getGmail())
                .fullName(d.getFullName())
                .phone(d.getPhone())
                .specialized(d.getSpecialized())
                .information(d.getInformation())
                .address(d.getAddress())
                .img(d.getImg())
                .lever(d.getLever())
                .createdAt(d.getCreatedAt())
                .build();
    }
}