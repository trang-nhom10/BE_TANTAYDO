package com.example.da_tantaydo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseCreatedEntity {
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt ;

    @Column(name = "UPDATED_AT", updatable = false)
    private LocalDateTime updateAt;
}
