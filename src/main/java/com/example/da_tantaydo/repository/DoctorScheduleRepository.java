package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.DoctorSchedule;
import com.example.da_tantaydo.model.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorId(Long doctorId);
    List<DoctorSchedule> findByWorkDate(LocalDate workDate);
    Optional<DoctorSchedule> findByDoctorIdAndWorkDateAndStatus(Long doctorId, LocalDate workDate, ScheduleStatus status);

    @Query("SELECT s FROM DoctorSchedule s WHERE s.doctor.id = :doctorId AND s.workDate = :workDate")
    Optional<DoctorSchedule> findByDoctorIdAndWorkDate(
            @Param("doctorId") Long doctorId,
            @Param("workDate") LocalDate workDate
    );

}