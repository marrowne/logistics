package it.logistics.hr.port.adapter.service;

import it.logistics.hr.domain.model.employee.Employee;
import it.logistics.hr.domain.model.employee.OauthId;
import it.logistics.hr.domain.model.employee.OauthService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class TranslatingOauthService implements OauthService {

    EmployeeAdapter employeeAdapter;

    public TranslatingOauthService(EmployeeAdapter employeeAdapter) {
        this.employeeAdapter = employeeAdapter;
    }

    @Override
    public OauthId oauthIdFor(Employee employee) {
        return this.getEmployeeAdapter().requestNewOauthId(employee);
    }

    @Override
    public void removeOauthId(Long employeeId) {
        this.getEmployeeAdapter().removeOauthId(employeeId);
    }

    public EmployeeAdapter getEmployeeAdapter() {
        return employeeAdapter;
    }
}
