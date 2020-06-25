package it.logistics.hr;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Before running this live test make sure both authorization server and first resource server are running   

@Configuration
public class HrLiveTest {

    private String authserverClientId = "parcelsSecured";
    private String authserverSecret = "secret";
    private String authserverEndpoint = "http://localhost:8082/oauth/token";
    private String adminUsername = "100000";
    private String courierUsername = "100001";
    private String password = "password";
    private String employeeJSON = "{\"fullName\":{\"firstName\": \"Andy\", \"lastName\": \"Chassey\"}, \"mobile\": {\"number\": \"321 654 987\"}, \"position\": \"COURIER\"}";
    
    @Test
    public void givenAdmin_whenPost_thenOk() {
        final String accessToken = obtainAccessToken(adminUsername, password);
        final Response parcelPost = RestAssured.given().headers(new Headers(
                new Header("Authorization", "Bearer " + accessToken),
                new Header("Content-Type", "application/json")
        )).body(employeeJSON).post("http://localhost:8081/hr/employee");
        assertEquals(200, parcelPost.getStatusCode());
    }

    @Test
    public void givenAdmin_whenPost_thenHttp403() {
        final String accessToken = obtainAccessToken(courierUsername, password);
        final Response parcelPost = RestAssured.given().headers(new Headers(
                new Header("Authorization", "Bearer " + accessToken),
                new Header("Content-Type", "application/json")
        )).body(employeeJSON).post("http://localhost:8081/hr/employee");
        assertEquals(403, parcelPost.getStatusCode());
    }

    @Test
    public void givenAdmin_whenGet_thenOk() {
        final String accessToken = obtainAccessToken(adminUsername, password);
        final Response parcelGet = RestAssured.given().header("Authorization", "Bearer " + accessToken).get("http://localhost:8081/hr/employee");
        assertEquals(200, parcelGet.getStatusCode());
        assertTrue(parcelGet.jsonPath().getList("id").size() > 0);
    }

    @Test
    public void givenCourier_whenGet_thenHttp403() {
        final String accessToken = obtainAccessToken(courierUsername, password);
        final Response parcelGet = RestAssured.given().header("Authorization", "Bearer " + accessToken).get("http://localhost:8081/hr/employee");
        assertEquals(403, parcelGet.getStatusCode());
    }

    @Test
    public void givenAdmin_whenSoftDelete_thenOk() {
        final String employee_to_delete = "100003";
        final String accessToken = obtainAccessToken(adminUsername, password);
        final Response parcelDelete = RestAssured.given().header("Authorization", "Bearer " + accessToken).delete("http://localhost:8081/hr/employee/" + employee_to_delete);
        assertEquals(200, parcelDelete.getStatusCode());
        final Response parcelGet = RestAssured.given().header("Authorization", "Bearer " + accessToken).get("http://localhost:8081/hr/employee/" + employee_to_delete);
        assertEquals(404, parcelGet.getStatusCode());
    }

    private String obtainAccessToken(String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", authserverClientId);
        params.put("username", username);
        params.put("password", password);
        final Response response = RestAssured.given().auth().preemptive().basic(authserverClientId, authserverSecret)
                .and().with().params(params).when().post(authserverEndpoint);
        return response.jsonPath().getString("access_token");
    }

}
