package com.example.da_tantaydo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DOCTOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "SPECIALIZED")
    private String specialized; // CHUYÊN KHOA

    @Column(name = "INFORMATION", columnDefinition = "TEXT")
    private String information; // THÔNG TIN / KINH NGHIỆM

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "IMG")
    private String img;

    @Column(name = "LEVER")
    private String lever; // CẤP ĐỘ / HỌC HÀM

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}