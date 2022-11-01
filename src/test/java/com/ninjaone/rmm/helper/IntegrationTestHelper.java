package com.ninjaone.rmm.helper;

import org.json.JSONObject;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class IntegrationTestHelper {

    private TestRestTemplate testRestTemplate;

    public IntegrationTestHelper(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    public ResponseEntity<String> authenticate(String baseUrl, String username, String password) throws Exception {
        final String url = baseUrl + "login";
        final URI uri = new URI(url);

        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);

        ResponseEntity<String> response = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                new HttpEntity<>(json.toString()),
                String.class);

        return response;
    }

    public HttpHeaders getAuthHeaders(String authToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }
}
