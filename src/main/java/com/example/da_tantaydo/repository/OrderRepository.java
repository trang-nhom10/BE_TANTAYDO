package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Order;
import com.example.da_tantaydo.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Query("""
        SELECT o FROM Order o
        WHERE LOWER(o.customer.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(o.service) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR o.customer.phone LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Order> search(@Param("keyword") String keyword, Pageable pageable);
    Page<Order> findByDoctorIdOrderByCreatedAtDesc(Long doctorId, Pageable pageable);
}