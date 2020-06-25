package it.logistics.tracking.port.adapter.service;

import it.logistics.tracking.domain.model.employee.Employee;
import it.logistics.tracking.domain.model.parcel.Administrator;
import it.logistics.tracking.domain.model.parcel.Courier;
import it.logistics.tracking.domain.model.parcel.DeliveryWorkerService;
import it.logistics.tracking.domain.model.parcel.SortingWorker;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class TranslatingDeliveryWorkerService implements DeliveryWorkerService {

    EmployeeAdapter employeeAdapter;

    public TranslatingDeliveryWorkerService(EmployeeAdapter employeeAdapter) {
        super();

        this.employeeAdapter = employeeAdapter;
    }

    @Override
    public Administrator administratorFrom(Employee employee) {
        Administrator administrator =
                this.getEmployeeAdapter().toDeliveryWorker(employee, Administrator.class);

        return administrator;
    }

    @Override
    public Courier courierFrom(Employee employee) {
        Courier courier =
                this.getEmployeeAdapter().toDeliveryWorker(employee, Courier.class);

        return courier;
    }

    @Override
    public SortingWorker sortingWorkerFrom(Employee employee) {
        SortingWorker sortingWorker =
                this.getEmployeeAdapter().toDeliveryWorker(employee, SortingWorker.class);

        return sortingWorker;
    }

    public EmployeeAdapter getEmployeeAdapter() {
        return employeeAdapter;
    }

}
