package it.logistics.tracking.domain.model.parcel;

import it.logistics.tracking.domain.model.employee.Employee;

public interface DeliveryWorkerService {

    public Administrator administratorFrom(Employee employee);

    public Courier courierFrom(Employee employee);

    public SortingWorker sortingWorkerFrom(Employee employee);

}
