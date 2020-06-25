package it.logistics.tracking.port;

import it.logistics.tracking.domain.model.employee.Employee;
import it.logistics.tracking.domain.model.parcel.DeliveryWorker;
import it.logistics.tracking.port.adapter.service.EmployeeAdapter;
import it.logistics.tracking.port.adapter.service.TrackingTranslator;

public class HrServiceStub implements EmployeeAdapter {

    private static final String USER_IN_ROLE_REPRESENTATION = "{" +
                "\"id\":$employee_id$," +
                "\"fullName\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}," +
                "\"mobile\":{\"number\":\"123 456 789\"}," +
                "\"employeeStatus\":\"EMPLOYEED\"," +
                "\"position\":\"ADMINISTRATOR\"" +
            "}";

    @Override
    public <T extends DeliveryWorker> T toDeliveryWorker(Employee employee, Class<T> deliveryWorkerClass) {


        String representation = USER_IN_ROLE_REPRESENTATION.replace("$employee_id$", employee.getId());

        T deliveryWorker = null;

        try {
            deliveryWorker =
                    new TrackingTranslator()
                            .toDeliveryWorkerFromRepresentation(
                                    representation,
                                    deliveryWorkerClass);

        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create employee.");
        }

        return deliveryWorker;
    }
}
