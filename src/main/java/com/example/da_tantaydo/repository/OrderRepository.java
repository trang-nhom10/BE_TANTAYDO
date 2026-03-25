package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Order;
import com.example.da_tantaydo.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    @Query("""
        SELECT o FROM Order o
        WHERE LOWER(o.customer.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(o.service) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR o.customer.phone LIKE CONCAT('%', :keyword, '%')
    """)
    List<Order> search(@Param("keyword") String keyword);

    List<Order> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
}