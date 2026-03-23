package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Appointment;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomerUserGmailOrderByCreateAtDesc(String gmail);
    List<Appointment> findByDoctorIdOrderByCreateAtDesc(Long doctorId);
    List<Appointment> findByStatusOrderByCreateAtDesc(AppointmentStatus status);
    List<Appointment> findByDoctorIdAndCreateAtOrderByTimeopenAsc(Long doctorId, LocalDate createAt);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId AND a.createAt = :createAt AND a.timeopen = :timeopen AND a.status = :status")
    boolean isSlotBooked(
            @Param("doctorId") Long doctorId,
            @Param("createAt") LocalDate createAt,
            @Param("timeopen") LocalTime timeopen,
            @Param("status") AppointmentStatus status
    );

    @Query("""
    SELECT a FROM Appointment a
    WHERE (:nameCustomer IS NULL OR LOWER(a.nameCustomer) LIKE LOWER(CONCAT('%', :nameCustomer, '%')))
    AND (:createAt IS NULL OR a.createAt = :createAt)
    AND (:timeopen IS NULL OR a.timeopen = :timeopen)
    ORDER BY a.createAt DESC
""")
    List<Appointment> search(
            @Param("nameCustomer") String nameCustomer,
            @Param("createAt") LocalDate createAt,
            @Param("timeopen") LocalTime timeopen
    );

    @Query("SELECT a FROM Appointment a WHERE a.gmail = :gmail")
    List<Appointment> findMyAppointments(@Param("gmail") String gmail);

    @Query(value = """
    SELECT
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE CAST(CEATED_AT AS DATE) = CURDATE()) AS totalToday,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE CAST(CEATED_AT AS DATE) = CURDATE() - INTERVAL 1 DAY) AS totalYesterday,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE STATUS = 'PENDING' AND CAST(CEATED_AT AS DATE) = CURDATE()) AS totalPending,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE STATUS = 'PENDING' AND CAST(CEATED_AT AS DATE) = CURDATE() - INTERVAL 1 DAY) AS totalPendingYesterday,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE STATUS = 'CONFIRMED' AND CAST(CEATED_AT AS DATE) = CURDATE()) AS totalConfirmed,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE STATUS = 'CONFIRMED' AND CAST(CEATED_AT AS DATE) = CURDATE() - INTERVAL 1 DAY) AS totalConfirmedYesterday,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE STATUS = 'CANCELLED' AND CAST(CEATED_AT AS DATE) = CURDATE()) AS totalCancelled,
        (SELECT COUNT(*) FROM APPOINTMENTS WHERE STATUS = 'CANCELLED' AND CAST(CEATED_AT AS DATE) = CURDATE() - INTERVAL 1 DAY) AS totalCancelledYesterday,
        (SELECT COUNT(*) FROM DOCTOR) AS totalDoctors,
        (SELECT COUNT(*) FROM CUSTOMERS) AS totalCustomers
    """, nativeQuery = true)
    Object[] getOverviewStats();

    @Query(value = """
    SELECT
        CASE
            WHEN YEAR(CURDATE()) - YEAR(a.YEAR) < 14 THEN 'Dưới 14 tuổi'
            WHEN YEAR(CURDATE()) - YEAR(a.YEAR) BETWEEN 15 AND 35 THEN 'Từ 15-35 tuổi'
            WHEN YEAR(CURDATE()) - YEAR(a.YEAR) BETWEEN 36 AND 64 THEN 'Từ 36-64 tuổi'
            ELSE 'Từ 65 tuổi'
        END AS ageGroup,
        COUNT(*) AS total
    FROM APPOINTMENTS a
    WHERE a.YEAR IS NOT NULL
    GROUP BY ageGroup
    """, nativeQuery = true)
    List<Object[]> getAgeDistribution();

    @Query(value = """
    SELECT
        STATUS,
        COUNT(*) AS total,
        ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 1) AS percentage
    FROM APPOINTMENTS
    GROUP BY STATUS
    """, nativeQuery = true)
    List<Object[]> getStatusRate();

    @Query(value = """
    SELECT
        MONTH(CEATED_AT) AS month,
        COUNT(*) AS total,
        SUM(CASE WHEN STATUS = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed,
        SUM(CASE WHEN STATUS = 'PENDING' THEN 1 ELSE 0 END) AS pending,
        SUM(CASE WHEN STATUS = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled
    FROM APPOINTMENTS
    WHERE YEAR(CEATED_AT) = YEAR(CURDATE())
    GROUP BY MONTH(CEATED_AT)
    ORDER BY MONTH(CEATED_AT)
    """, nativeQuery = true)
    List<Object[]> getMonthlyStats();
}