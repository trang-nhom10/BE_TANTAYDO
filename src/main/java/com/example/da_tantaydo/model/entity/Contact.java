package com.example.da_tantaydo.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CONTACT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name ="NAME")
    private String name;

    @Column (name="GMAIL")
    private String gmail;

    @Column(name="TEXT")
    private String text;

}
