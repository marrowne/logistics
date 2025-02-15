package it.logistics.hr.infrastructure.persistence;

import it.logistics.hr.domain.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // derived from JPA
}
