package com.example.da_tantaydo.repository;


import com.example.da_tantaydo.model.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserGmail(String gmail);

    boolean existsByUserGmail(String gmail);
    boolean existsByPhone(String phone);

    // TÌM KIẾM THEO TÊN / CHUYÊN KHOA
    @Query("""
        SELECT d FROM Doctor d
        WHERE LOWER(d.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.specialized) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR d.phone LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Doctor> search(@Param("keyword") String keyword, Pageable pageable);
}