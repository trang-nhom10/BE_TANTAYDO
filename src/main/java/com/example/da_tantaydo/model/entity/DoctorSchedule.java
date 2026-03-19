package com.example.da_tantaydo.model.entity;


import com.example.da_tantaydo.model.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "DOCTOR_SCHEDULES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    private Doctor doctor;

    @Column(name = "WORK_DATE", nullable = false)
    private LocalDate workDate;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

    @Column(name = "MAX_PATIENT")
    private Integer maxPatient = 10;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ScheduleStatus status = ScheduleStatus.AVAILABLE;

}