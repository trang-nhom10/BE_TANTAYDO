package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
