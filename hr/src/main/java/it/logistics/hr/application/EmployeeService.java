package it.logistics.hr.application;

import it.logistics.hr.domain.model.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface EmployeeService {

    Employee findEmployee(long id);

    void addEmployee(Employee employee);

    void deleteEmployee(long id);

    Page<Employee> allEmployees(PageRequest pageRequest);

}
