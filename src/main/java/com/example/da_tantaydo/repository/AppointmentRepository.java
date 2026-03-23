package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Appointment;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomerUserGmailOrderByCreateAtDesc(String gmail);
    List<Appointment> findByDoctorIdOrderByCreateAtDesc(Long doctorId);
    List<Appointment> findByStatusOrderByCreateAtDesc(AppointmentStatus status);

    @Query("""
        SELECT a FROM Appointment a
        WHERE LOWER(a.nameCustomer) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR a.phone LIKE CONCAT('%', :keyword, '%')
        OR a.gmail LIKE CONCAT('%', :keyword, '%')
        OR LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(a.customer.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY a.createAt DESC
    """)
    List<Appointment> search(@Param("keyword") String keyword);

    @Query("SELECT a FROM Appointment a WHERE a.gmail = :gmail")
    List<Appointment> findMyAppointments(@Param("gmail") String gmail);
}