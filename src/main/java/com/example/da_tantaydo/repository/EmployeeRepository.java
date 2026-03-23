package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserGmail(String gmail);
    @Query("""
    SELECT e FROM Employee e
    WHERE (:fullName IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
    AND (:address IS NULL OR LOWER(e.address) LIKE LOWER(CONCAT('%', :address, '%')))
""")
    List<Employee> search(@Param("fullName") String fullName, @Param("address") String address);

}