package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.DoctorCreateRequestDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.Status;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.List;

@Transactional
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
    public void create(DoctorCreateRequestDTO request) {
        Role role = roleRepository.findById(4L).orElseThrow(() -> new RuntimeException("Role not found."));
        User user = User.builder()
                .gmail(request.getGmail())
                .password(passwordEncoder.encode("123456"))
                .role(role)
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);
        Doctor doctor = Doctor.builder()
                .user(user)
                .name(request.getName())
                .specialized(request.getSpecialized())
                .build();

        doctorRepository.save(doctor);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found."));

        Long userId = doctor.getUser().getId(); // lấy userId từ doctor

        doctorRepository.delete(doctor); // xóa doctor trước
        userRepository.deleteById(userId); // xóa user sau
    }

    @Override
    public List<DoctorResponseDTO> getAll() {
        return doctorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<DoctorResponseDTO> search(String name, String specialized, String lever) {
        return doctorRepository.searchDoctor(name, specialized, lever)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void updateProfile(String gmail, DoctorProfileRequestDTO request, MultipartFile img) {
        Doctor doctor = doctorRepository.findByUserGmail(gmail).orElseThrow(() -> new RuntimeException("Doctor not found."));

        if (request.getFullName() != null) doctor.setName(request.getFullName());
        if (request.getPhone() != null) doctor.setPhone(request.getPhone());
        if (request.getSpecialized() != null) doctor.setSpecialized(request.getSpecialized());
        if (request.getInformation() != null) doctor.setInformation(request.getInformation());
        if (request.getAddress() != null) doctor.setAddress(request.getAddress());
        if (request.getLever() != null) doctor.setLever(request.getLever());

        if (img != null && !img.isEmpty()) {
            if (doctor.getImg() != null) {
                mediaStorageService.deleteMedia(Long.parseLong(doctor.getImg()));
            }
            String mediaId = mediaStorageService.uploadMedia(img);
            doctor.setImg(mediaId);
        }

      doctorRepository.save(doctor);
    }

    @Override
    public List<AppointmentResponseDTO> getMyAppointments(Authentication authentication) {
        return appointmentRepository.findMyAppointments(authentication.getName())
                .stream()
                .map(this::toAppointmentDTO)
                .toList();
    }
    private DoctorResponseDTO toDTO(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .email(doctor.getUser().getGmail())
                .phone(doctor.getPhone())
                .specialized(doctor.getSpecialized())
                .information(doctor.getInformation())
                .address(doctor.getAddress())
                .lever(doctor.getLever())
                .build();
    }

    private AppointmentResponseDTO toAppointmentDTO(Appointment appointment) {
        return new AppointmentResponseDTO();
    }

}