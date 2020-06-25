package it.logistics.tracking.application.integration;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Before running this live test make sure both authorization server and first resource server are running

@Configuration
public class ParcelLiveTest {

    private String authserverClientId = "parcelsSecured";
    private String authserverSecret = "secret";
    private String authserverEndpoint = "http://localhost:8082/oauth/token";
    private String adminUsername = "100000";
    private String courierUsername = "100001";
    private String sortingUsername = "100002";
    private String parcelId = "1000000";
    private String password = "password";
    private String parcelJSON = "{\"sender\":{\"name\":{\"firstName\":\"Grzegorz\",\"lastName\":\"Nowak\"},\"phone\":{\"number\":\"123456789\"},\"address\":{\"street\":\"Nowakowskiego 23/1\",\"postalCode\":\"30-300\",\"city\":\"Krakow\",\"country\":\"Poland\"}},\"receiver\":{\"name\":{\"firstName\":\"Jan\",\"lastName\":\"Kowalski\"},\"phone\":{\"number\":\"987654321\"},\"address\":{\"street\":\"Dolna 43/8\",\"postalCode\":\"53-658\",\"city\":\"Wroclaw\",\"country\":\"Poland\"}},\"courier\":{\"id\":\"" + courierUsername + "\"},\"weight\":\"1.82\"}";

    @Test
    public void givenEmployee_whenPost_thenHttp403() {
        final String accessToken = obtainAccessToken(sortingUsername, password);
        final Response parcelPost = RestAssured.given().headers(new Headers(
                new Header("Authorization", "Bearer " + accessToken),
                new Header("Content-Type", "application/json")
        )).body(parcelJSON).post("http://localhost:8080/tracking/parcel");
        assertEquals(403, parcelPost.getStatusCode());
    }

    @Test
    public void givenAdmin_whenPost_thenOk() {
        final String accessToken = obtainAccessToken(adminUsername, password);
        final Response parcelPost = RestAssured.given().headers(new Headers(
                new Header("Authorization", "Bearer " + accessToken),
                new Header("Content-Type", "application/json")
        )).body(parcelJSON).post("http://localhost:8080/tracking/parcel");
        assertEquals(200, parcelPost.getStatusCode());
    }

    @Test
    public void givenEmployee_whenGet_thenOk() {
        final String accessToken = obtainAccessToken(courierUsername, password);
        final Response parcelGet = RestAssured.given()
                .param("filter", "{}")
                .param("range", "[0,9]")
                .param("sort", "[\"id\",\"ASC\"]")
                .header("Authorization", "Bearer " + accessToken)
                .get("http://localhost:8080/tracking/parcel");
        assertEquals(200, parcelGet.getStatusCode());
        assertTrue(Long.parseLong(((ArrayList) parcelGet.jsonPath()
                .get("id")).get(1).toString()) > 100000);
    }

    @Test()
    public void givenCourier_whenConfirmReceived_thenOk() {
        final String accessToken = obtainAccessToken(courierUsername, password);
        final Response parcelPost = RestAssured.given().headers(new Headers(
                new Header("Authorization", "Bearer " + accessToken),
                new Header("Content-Type", "application/json")
        )).body(parcelJSON).patch("http://localhost:8080/tracking/parcel/courier/received/" + parcelId);
        assertEquals(200, parcelPost.getStatusCode());

        final Response parcelGet = RestAssured.given()
                .param("filter", "{}")
                .param("range", "[0,9]")
                .param("sort", "[\"id\",\"ASC\"]")
                .header("Authorization", "Bearer " + accessToken)
                .get("http://localhost:8080/tracking/parcel");
        assertEquals(200, parcelGet.getStatusCode());
        int jsonEntry = ((ArrayList) parcelGet.jsonPath().get("id")).indexOf(Integer.valueOf(parcelId));
        String parcelStatus = (String) ((ArrayList) parcelGet.jsonPath().get("status")).get(jsonEntry);
        assertTrue(parcelStatus.equals("PICKUP"));
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
