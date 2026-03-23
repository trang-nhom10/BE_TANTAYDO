package com.example.da_tantaydo.model.entity;

import com.example.da_tantaydo.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "APPOINTMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    private Doctor doctor;

    @Column(name = "NAME_CUSTOMER")
    private String nameCustomer;

    @Column(name = "YEAR")
    private LocalDateTime year;

    @Column(name ="PHONE")
    private String phone;

    @Column(name ="GMAIL")
    private String gmail;

    @Column(name = "ADDRESS")
    private  String  address;

    @Column(name ="CEATED_AT")
    private LocalDateTime createAt;

    @Column(name ="TIMEOPEN")
    private LocalDateTime timeopen;

    @Column(name ="NOTE")
    private  String note;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

}