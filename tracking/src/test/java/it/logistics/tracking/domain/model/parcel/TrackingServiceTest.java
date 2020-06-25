package it.logistics.tracking.domain.model.parcel;

import it.logistics.tracking.domain.model.employee.Employee;
import it.logistics.tracking.port.HrServiceStub;
import it.logistics.tracking.port.adapter.service.TrackingTranslator;
import it.logistics.tracking.port.adapter.service.TranslatingDeliveryWorkerService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TrackingServiceTest {

    private static final String EMPLOYEE_REPRESENTATION = "{" +
            "\"id\":100000," +
            "\"fullName\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}," +
            "\"mobile\":{\"number\":\"123 456 789\"}," +
            "\"employeeStatus\":\"EMPLOYEED\"," +
            "\"position\":\"COURIER\"" +
            "}";

    private DeliveryWorkerService parcelService;

    @Before
    public void setUp() {
        this.parcelService =
                new TranslatingDeliveryWorkerService(
                        new HrServiceStub()
                    );
    }

    @Test
    public void testCourierFrom() {
        // given
        Employee employee = new Employee("100000");

        // when
        Courier courier = this.parcelService.courierFrom(employee);

        // then
        assertNotNull(courier);
    }

    @Test
    public void testAdministratorFrom() {
        // given
        Employee employee = new Employee("100000");

        // when
        Administrator administrator = this.parcelService.administratorFrom(employee);

        // then
        assertNotNull(administrator);
    }

    @Test
    public void testSortingWorkerFrom() {
        // given
        Employee employee = new Employee("100000");

        // when
        SortingWorker sortingWorker = this.parcelService.sortingWorkerFrom(employee);

        // then
        assertNotNull(sortingWorker);
    }

    @Test
    public void testTrackingTranslator() throws Exception {
        // given

        // when
        Courier employee =
                new TrackingTranslator().toDeliveryWorkerFromRepresentation(
                        EMPLOYEE_REPRESENTATION,
                        Courier.class);

        // then
        assertNotNull(employee);
        assertEquals("100000", employee.getId());
    }

}























