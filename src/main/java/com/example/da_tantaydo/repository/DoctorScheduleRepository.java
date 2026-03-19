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

    // LẤY CA KHÁM THEO BÁC SĨ
    List<DoctorSchedule> findByDoctorId(Long doctorId);

    // LẤY CA KHÁM THEO NGÀY
    List<DoctorSchedule> findByWorkDate(LocalDate workDate);

    // LẤY CA KHÁM THEO BÁC SĨ VÀ NGÀY
    List<DoctorSchedule> findByDoctorIdAndWorkDate(Long doctorId, LocalDate workDate);

    // LẤY CA KHÁM CÒN TRỐNG
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

    // ĐẾM SỐ BỆNH NHÂN HIỆN TẠI TRONG CA
    @Query("""
        SELECT COUNT(a) FROM Appointment a
        WHERE a.schedule.id = :scheduleId
        AND a.status != 'CANCELLED'
    """)
    int countCurrentPatient(@Param("scheduleId") Long scheduleId);
}