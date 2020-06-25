package it.logistics.hr.application;

import it.logistics.hr.domain.model.employee.Employee;
import it.logistics.hr.infrastructure.persistence.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee findEmployee(long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void addEmployee(Employee employee) {
        validateName(employee);
        employeeRepository.save(employee);
    }

    private void validateName(Employee employee) {
        if(employee.getFullName().getFirstName() == null) {
            throw new IllegalArgumentException("Cannot set first name to null.");
        }
        if(employee.getFullName().getLastName() == null) {
            throw new IllegalArgumentException("Cannot set last name to null.");
        }
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

    public Page<Employee> allEmployees(PageRequest pageRequest) { return employeeRepository.findAll(pageRequest); }

}
