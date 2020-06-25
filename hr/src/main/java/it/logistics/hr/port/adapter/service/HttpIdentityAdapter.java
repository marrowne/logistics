package it.logistics.hr.port.adapter.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.logistics.hr.domain.model.employee.Employee;
import it.logistics.hr.domain.model.employee.OauthId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource({ "classpath:application.properties", "classpath:application-${spring.profiles.active}.properties"})
public class HttpIdentityAdapter implements EmployeeAdapter {

    @Value("${identity.endpoint.host}")
    private String AUTH_HOST;

    @Value("${identity.endpoint.user-data}")
    private String AUTH_DATA_URL_TEMPLATE;

    @Value("${identity.endpoint.user}")
    private String AUTH_USER_URL_TEMPLATE;

    @Value("${identity.endpoint.port}")
    private String AUTH_PORT;

    @Value("${identity.endpoint.protocol}")
    private String AUTH_PROTOCOL;

    public HttpIdentityAdapter() {
        super();
    }

    @Override
    public OauthId requestNewOauthId(Employee employee) {

        OauthId oauthId = null;

        try {
            Response response = this.requestIdForPosition(employee);

            if (response.getStatusCode() == 200) {
                oauthId =
                        new OauthTranslator()
                                .toOauthIdFromRepresentation(
                                        response.getBody().asString()
                                );
            } else if (response.getStatusCode() == 204) {
                // not an error, return null
            } else {
                throw new IllegalStateException(
                        "There was a problem requesting OAuth ID"
                                + " with resulting status: "
                                + response.getStatusCode());
            }

        } catch (Throwable t) {
            throw new IllegalStateException(
                    "Failed because: " + t.getMessage(), t);
        }

        return oauthId;
    }

    @Override
    public void removeOauthId(Long employeeId) {
        try {
            Response response = requestOauthIdRemoval(employeeId);

            if (response.getStatusCode() == 200) {
                // not an error, return null
            } else if (response.getStatusCode() == 204) {
                // not an error, return null
            } else {
                throw new IllegalStateException(
                        "There was a problem removing OAuth ID"
                                + " with resulting status: "
                                + response.getStatusCode());
            }
        } catch (Throwable t) {
            throw new IllegalStateException(
                    "Failed because: " + t.getMessage(), t);
        }

    }

    private Response requestIdForPosition(Employee employee) {

        final Map<String, String> params = new HashMap<String, String>();
        params.put("role", employee.getPosition().toString());

        Response response = RestAssured
                .given().params(params).when().post(
                        this.buildURLFor(AUTH_DATA_URL_TEMPLATE, AUTH_HOST, AUTH_PROTOCOL, AUTH_PORT)
                );

        return response;
    }

    private Response requestOauthIdRemoval(Long employeeId) {

        Response response = RestAssured.given()
                .pathParam("id", employeeId).when().delete(
                        this.buildURLFor(AUTH_USER_URL_TEMPLATE, AUTH_HOST, AUTH_PROTOCOL, AUTH_PORT)
                );

        return response;
    }

    private String buildURLFor(String aTemplate, String aHost, String aProtocol, String aPort) {
        String url =
                aProtocol
                        + "://"
                        + aHost + ":" + aPort
                        + aTemplate;

        return url;
    }

}
