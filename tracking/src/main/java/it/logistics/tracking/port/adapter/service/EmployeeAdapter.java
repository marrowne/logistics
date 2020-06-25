package it.logistics.tracking.port.adapter.service;

import it.logistics.tracking.domain.model.employee.Employee;
import it.logistics.tracking.domain.model.parcel.DeliveryWorker;

public interface EmployeeAdapter {

    public <T extends DeliveryWorker> T toDeliveryWorker(
            Employee employee,
            Class<T> deliveryWorkerClass);

}
