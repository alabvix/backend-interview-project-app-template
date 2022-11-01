package com.ninjaone.rmm.service;

import com.ninjaone.rmm.helper.IntegrationTestHelper;
import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.AddServiceOutput;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:IntegrationTest.properties")
public class ServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IntegrationTestHelper testHelper;

    @Value("${test.url}")
    private String testBaseUrl;

    @Value("${test.username}")
    private String username;

    @Value("${test.password}")
    private String password;

    private String baseUrl;

    private String authToken;

    @BeforeEach
    public void beforeEach() throws Exception {
        this.baseUrl = this.testBaseUrl + this.port +"/";
        final ResponseEntity<String> response = testHelper.authenticate(
                this.baseUrl,
                this.username,
                this.password);
        final String body = response.getBody();
        assert body != null;
        this.authToken = body.substring(4);
    }

    @Test
    @DisplayName("Try to create a service with duplicated name. Should return Http status 400")
    public void createDeviceWithDuplicatedName() throws Exception {
        final AddServiceInput input =
                new AddServiceInput("Mac Antivirus", new BigDecimal("150.00"));

        HttpEntity<AddServiceInput> addRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<String> response = testRestTemplate
                .exchange(getUri("service"), HttpMethod.POST, addRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Service with given name already exists"));
    }

    @Test
    @DisplayName("Try to create a service with negative cost. Should return Http status 400")
    public void createDeviceWithNegativeCost() throws Exception {
        final AddServiceInput input =
                new AddServiceInput("Mac Power book", new BigDecimal("-150.00"));

        HttpEntity<AddServiceInput> addRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<String> response = testRestTemplate
                .exchange(getUri("service"), HttpMethod.POST, addRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Service cost should be greater then zero"));

    }

    @Test
    @DisplayName("Create a new service and delete it")
    public void createGetAndDeleteService() throws Exception{
        final AddServiceInput input =
                new AddServiceInput("Network Monitoring", new BigDecimal("150.00"));

        HttpEntity<AddServiceInput> addRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<AddServiceOutput> response = testRestTemplate
                .exchange(getUri("service"), HttpMethod.POST, addRequest, AddServiceOutput.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getBody());

        AddServiceOutput serviceOutput = response.getBody();
        assertNotNull(serviceOutput.serviceId);
        assertEquals(7L, serviceOutput.serviceId);
        assertEquals("Network Monitoring", serviceOutput.name);
        assertEquals(new BigDecimal("150.00"), serviceOutput.cost);

        // get the service
        HttpEntity<String> getRequest = new HttpEntity<>(testHelper.getAuthHeaders(authToken));
        ResponseEntity<GetServiceOutput> getResponse = testRestTemplate.exchange(
                getUri("service/" + serviceOutput.serviceId),
                HttpMethod.GET,
                getRequest,
                GetServiceOutput.class);

        assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(getResponse.getBody());
        GetServiceOutput getServiceOutput = getResponse.getBody();
        assertEquals(7L, getServiceOutput.serviceId);
        assertEquals("Network Monitoring", getServiceOutput.name);
        assertEquals(new BigDecimal("150.00"), getServiceOutput.cost);

        // Delete it
        HttpEntity<String> deleteRequest = new HttpEntity<>(testHelper.getAuthHeaders(authToken));
        ResponseEntity<String> deleteResponse = testRestTemplate.exchange(
                getUri("service/" + getServiceOutput.serviceId),
                HttpMethod.DELETE,
                deleteRequest,
                String.class);
        assertEquals(deleteResponse.getStatusCode(), HttpStatus.NO_CONTENT);

        // Make sure that the device was deleted
        ResponseEntity<GetServiceOutput> getServiceDeleted = testRestTemplate.exchange(
                getUri("service/" + getServiceOutput.serviceId),
                HttpMethod.GET,
                getRequest,
                GetServiceOutput.class);

        assertEquals(getServiceDeleted.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    private URI getUri(String resource) throws URISyntaxException {
        final String url = baseUrl + resource;
        return new URI(url);
    }

}
