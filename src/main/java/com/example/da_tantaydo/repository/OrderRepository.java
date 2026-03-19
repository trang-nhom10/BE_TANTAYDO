package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Order;
import com.example.da_tantaydo.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // LẤY ĐƠN HÀNG THEO KHÁCH HÀNG
    Page<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

    // LẤY ĐƠN HÀNG THEO TRẠNG THÁI
    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    // LẤY TẤT CẢ MỚI NHẤT
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // TÌM KIẾM THEO TÊN KHÁCH / DỊCH VỤ
    @Query("""
        SELECT o FROM Order o
        WHERE LOWER(o.customer.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(o.service) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR o.customer.phone LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Order> search(@Param("keyword") String keyword, Pageable pageable);

    // OrderRepository.java - BỔ SUNG THÊM DÒNG NÀY
    Page<Order> findByDoctorIdOrderByCreatedAtDesc(Long doctorId, Pageable pageable);
}