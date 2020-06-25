package it.logistics.tracking.port.adapter.service;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import it.logistics.tracking.domain.model.employee.Employee;
import it.logistics.tracking.domain.model.parcel.DeliveryWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource({ "classpath:application.properties", "classpath:application-${spring.profiles.active}.properties"})
public class HttpEmployeeAdapter implements EmployeeAdapter {

    @Value("${hr.endpoint.employee-details}")
    private String HR_URL_TEMPLATE;

    @Value("${hr.endpoint.host}")
    private String HR_HOST;

    @Value("${hr.endpoint.protocol}")
    private String HR_PROTOCOL;

    @Value("${hr.endpoint.port}")
    private String HR_PORT;

    @Value("${identity.endpoint.host}")
    private String AUTH_HOST;

    @Value("${identity.endpoint.tokens}")
    private String AUTH_URL_TEMPLATE;

    @Value("${identity.endpoint.port}")
    private String AUTH_PORT;

    @Value("${identity.endpoint.protocol}")
    private String AUTH_PROTOCOL;

    @Value("${identity.endpoint.admin.username}")
    private String AUTH_ADMIN_USERNAME;

    @Value("${identity.endpoint.client.id}")
    private String AUTH_CLIENT_ID;

    @Value("${identity.endpoint.client.secret}")
    private String AUTH_CLIENT_SECRET;

    @Value("${identity.endpoint.admin.password}")
    private String AUTH_ADMIN_PASSWORD;

    public HttpEmployeeAdapter() {
        super();
    }

    @Override
    public <T extends DeliveryWorker> T toDeliveryWorker(
            Employee employee,
            Class<T> deliveryWorkerClass) {

        T deliveryWorker = null;

        try {
            Response response = this.request(employee);

            if (response.getStatusCode() == 200) {
                deliveryWorker =
                        new TrackingTranslator()
                                .toDeliveryWorkerFromRepresentation(
                                        response.getBody().asString(),
                                        deliveryWorkerClass);
            } else if (response.getStatusCode() == 204) {
                // not an error, return null
            } else {
                throw new IllegalStateException(
                        "There was a problem requesting the user"
                                + " with resulting status: "
                                + response.getStatusCode());
            }

        } catch (Throwable t) {
            throw new IllegalStateException(
                    "Failed because: " + t.getMessage(), t);
        }

        return deliveryWorker;
    }

    private Response request(Employee employee) {

        final Response response = RestAssured.given().
                headers(new Headers(
                    new Header("Authorization", "Bearer " + obtainAccessToken()),
                    new Header("Content-Type", "application/json")
                        ))
                    .and()
                .pathParam("employeeId", employee.getId())
                .get(HttpEmployeeAdapter.buildURLFor(HR_URL_TEMPLATE, HR_HOST, HR_PROTOCOL, HR_PORT));

        return response;
    }

    private static String buildURLFor(String aTemplate, String aHost, String aProtocol, String aPort) {
        String url =
                aProtocol
                        + "://"
                        + aHost + ":" + aPort
                        + aTemplate;

        return url;
    }

    private String obtainAccessToken() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", AUTH_CLIENT_ID);
        params.put("username", AUTH_ADMIN_USERNAME);
        params.put("password", AUTH_ADMIN_PASSWORD);
        final Response response = RestAssured.given().auth().preemptive().basic(AUTH_CLIENT_ID, AUTH_CLIENT_SECRET)
                .and().with().params(params).when().post(
                        HttpEmployeeAdapter.buildURLFor(AUTH_URL_TEMPLATE, AUTH_HOST, AUTH_PROTOCOL, AUTH_PORT)
                );
        return response.jsonPath().getString("access_token");
    }

}
