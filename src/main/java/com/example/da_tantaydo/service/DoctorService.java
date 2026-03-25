package com.example.da_tantaydo.service;
import com.example.da_tantaydo.model.dto.request.DoctorCreateRequestDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.request.EmployeeProfileRequest;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public interface DoctorService {

    void create(DoctorCreateRequestDTO request);
    void delete(Long id);
    List<DoctorResponseDTO> getAll();
    List<DoctorResponseDTO> search(String name, String specialized,String lever);
    List<AppointmentResponseDTO> getMyAppointments(Authentication authentication);
    void updateProFile(String gmail, DoctorProfileRequestDTO request, MultipartFile img);
}