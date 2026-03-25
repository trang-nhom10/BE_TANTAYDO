package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.AppointmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentFileRepository extends JpaRepository<AppointmentFile, Long> {
    List<AppointmentFile> findByAppointmentId(Long appointmentId);
}