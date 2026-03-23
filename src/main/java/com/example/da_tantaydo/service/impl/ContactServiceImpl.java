package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.ContactRequestDTO;
import com.example.da_tantaydo.model.entity.Contact;
import com.example.da_tantaydo.repository.ContactRepository;
import com.example.da_tantaydo.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public void create(ContactRequestDTO request) {
        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setGmail(request.getGmail());
        contact.setText(request.getText());
        contact.setCreatedAt(LocalDateTime.now());
        contactRepository.save(contact);
    }

    @Override
    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found.");
        }
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }
}