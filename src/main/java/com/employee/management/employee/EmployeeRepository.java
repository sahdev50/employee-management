package com.employee.management.employee;

import com.employee.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    boolean existsByDepartmentId(Long id);

    @Query("SELECT COUNT(e) > 0 FROM Employee e JOIN e.teams t WHERE t.id = :teamId")
    boolean hasActiveEmployees(@Param("teamId") Long teamId);
}
