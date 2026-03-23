package com.example.da_tantaydo.repository;

import com.example.da_tantaydo.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserGmail(String gmail);
    @Query("""
    SELECT d FROM Doctor d
    WHERE (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:specialized IS NULL OR LOWER(d.specialized) LIKE LOWER(CONCAT('%', :specialized, '%')))
    AND (:lever IS NULL OR LOWER(d.lever) LIKE LOWER(CONCAT('%', :lever, '%')))
    ORDER BY d.lever ASC, d.specialized ASC, d.name ASC
""")
    List<Doctor> searchDoctor(
            @Param("name") String name,
            @Param("specialized") String specialized,
            @Param("lever") String lever
    );
}