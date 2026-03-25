package com.example.da_tantaydo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String fileUrl;

    private String fileName;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}