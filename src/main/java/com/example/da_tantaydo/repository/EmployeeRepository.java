package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserGmail(String gmail);
    @Query("""
        SELECT e FROM Employee e
        WHERE LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR e.phone LIKE CONCAT('%', :keyword, '%')
        OR e.cccd LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Employee> search(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByUserGmail(String gmail);
    boolean existsByCccd(String cccd);
    boolean existsByPhone(String phone);
}