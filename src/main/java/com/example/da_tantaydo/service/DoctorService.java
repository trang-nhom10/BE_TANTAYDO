package com.example.da_tantaydo.service;
import com.example.da_tantaydo.model.dto.request.DoctorCreateDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface DoctorService {
    // ADMIN
    DoctorResponseDTO create(DoctorCreateDTO request);
    void delete(Long id);
    Page<DoctorResponseDTO> getAll(int page, int size);
    Page<DoctorResponseDTO> search(String keyword, int page, int size);
    DoctorResponseDTO getById(Long id);

    // BÁC SĨ TỰ UPDATE
    DoctorResponseDTO updateProfile(String gmail,
                                    DoctorProfileRequestDTO request,
                                    MultipartFile img);

    // BÁC SĨ XEM LỊCH KHÁCH ĐẶT
    Page<AppointmentResponseDTO> getMyAppointments(String gmail, int page, int size);

    // BÁC SĨ XEM ĐƠN HÀNG KHÁCH
    Page<OrderResponseDTO> getMyOrders(String gmail, int page, int size);
}