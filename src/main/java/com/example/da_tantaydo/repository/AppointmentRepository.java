package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Appointment;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByCustomerUserGmailOrderByCreatedAtDesc(String gmail, Pageable pageable);
    Page<Appointment> findByDoctorIdOrderByCreatedAtDesc(Long doctorId, Pageable pageable);
    Page<Appointment> findByStatusOrderByCreatedAtDesc(AppointmentStatus status, Pageable pageable);
    boolean existsByCustomerIdAndScheduleId(Long customerId, Long scheduleId);
    @Query("""
        SELECT COUNT(a) FROM Appointment a
        WHERE a.schedule.id = :scheduleId
        AND a.status != 'CANCELLED'
    """)
    int countActiveByScheduleId(@Param("scheduleId") Long scheduleId);
    @Query("""
        SELECT a FROM Appointment a
        WHERE LOWER(a.customer.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR a.customer.phone LIKE CONCAT('%', :keyword, '%')
        OR LOWER(a.service) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(a.doctor.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Appointment> search(@Param("keyword") String keyword, Pageable pageable);
}