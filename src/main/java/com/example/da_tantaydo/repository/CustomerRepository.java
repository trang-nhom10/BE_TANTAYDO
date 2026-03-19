package com.example.da_tantaydo.repository;
import com.example.da_tantaydo.model.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserGmail(String gmail);

    boolean existsByPhone(String phone);
    boolean existsByCccd(String cccd);
    boolean existsByUserGmail(String gmail);
    @Query("""
        SELECT c FROM Customer c
        WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR c.phone LIKE CONCAT('%', :keyword, '%')
        OR c.cccd LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Customer> search(@Param("keyword") String keyword, Pageable pageable);
}