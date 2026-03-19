package com.example.da_tantaydo.model.entity;

import com.example.da_tantaydo.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "GMAIL")
    private String gmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private Role role;

    @Column(name = "STATUS")
    private Status status;

}