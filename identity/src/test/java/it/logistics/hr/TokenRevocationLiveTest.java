package it.logistics.hr;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenRevocationLiveTest {

    private final String courierId = "100001";

    @Test
    public void whenObtainingAccessToken_thenCorrect() {
        final Response authServerResponse = obtainAccessToken("parcelsSecured", courierId, "password");
        final String accessToken = authServerResponse.jsonPath().getString("access_token");
        assertNotNull(accessToken);
        System.out.println(accessToken);

        String refreshedToken = obtainRefreshToken("parcelsSecured", accessToken);
        System.out.println(refreshedToken);

        Response checkResponse = checkToken("parcelsSecured", accessToken);
        assertEquals(checkResponse.getStatusCode(), 200);
        assertEquals(checkResponse.jsonPath().getList("authorities").get(0), "COURIER");
    }

    private Response obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        return RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8082/oauth/token");
    }

    private String obtainRefreshToken(String clientId, final String refreshToken) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "refresh_token");
        params.put("client_id", clientId);
        params.put("refresh_token", refreshToken);
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8082/oauth/token");
        return response.jsonPath().getString("access_token");
    }

    private Response checkToken(String clientId, final String tokenId) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("token", tokenId);
        return RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8082/oauth/check_token");
    }

}