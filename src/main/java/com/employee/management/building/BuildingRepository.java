package com.employee.management.building;

import com.employee.management.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    boolean existsByLocationId(Long id);
}
