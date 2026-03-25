package com.example.da_tantaydo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "FEEDBACK")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="FULL_NAME")
    private String fullName;

    @Column(name ="GMAIL")
    private String gmail;

    @Column(name ="SICK")
    private String sick;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name ="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name= "EVALUATE")
    private Integer evaluate;
}