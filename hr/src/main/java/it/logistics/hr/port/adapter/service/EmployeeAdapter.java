package it.logistics.hr.port.adapter.service;

import it.logistics.hr.domain.model.employee.Employee;
import it.logistics.hr.domain.model.employee.OauthId;

public interface EmployeeAdapter {

    public OauthId requestNewOauthId(Employee employee);

    public void removeOauthId(Long employeeId);

}
