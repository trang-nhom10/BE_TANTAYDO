package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.DoctorSchedule;
import com.example.da_tantaydo.model.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorId(Long doctorId);
    List<DoctorSchedule> findByWorkDate(LocalDate workDate);
    List<DoctorSchedule> findByDoctorIdAndWorkDate(Long doctorId, LocalDate workDate);
    List<DoctorSchedule> findByStatus(ScheduleStatus status);
    @Query("""
        SELECT COUNT(s) > 0 FROM DoctorSchedule s
        WHERE s.doctor.id = :doctorId
        AND s.workDate = :workDate
        AND s.id != :excludeId
        AND (
            (s.startTime < :endTime AND s.endTime > :startTime)
        )
    """)
    boolean existsOverlap(
            @Param("doctorId")  Long doctorId,
            @Param("workDate")  LocalDate workDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime")   LocalTime endTime,
            @Param("excludeId") Long excludeId
    );
}