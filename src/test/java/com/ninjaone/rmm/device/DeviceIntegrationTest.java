package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.*;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import com.ninjaone.rmm.service.payload.ServiceDeviceOutput;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:DeviceIntegrationTest.properties")
public class DeviceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

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
        final ResponseEntity<String> response = authenticate();
        final String body = response.getBody();
        assert body != null;
        this.authToken = body.substring(4);
    }

    @Test
    public void test() throws Exception{
        HttpEntity<String> request = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<GetDeviceOutput> response = testRestTemplate
                .exchange(
                    getUri("device/1"),
                    HttpMethod.GET,
                    request,
                    GetDeviceOutput.class);

        System.out.println(response);
    }

    @Test
    @DisplayName("Creates a new device, associate 2 services, calculate the total cost and deletes the device")
    public void createDeviceTest() throws Exception {

        // Add a new Device
        AddDeviceInput input = new AddDeviceInput("Windows Server Ultimate", DeviceType.WINDOWS_SERVER);
        HttpEntity<AddDeviceInput> addRequest = new HttpEntity<>(input, getAuthHeaders());
        ResponseEntity<AddDeviceOutput> response = testRestTemplate
                .exchange(getUri("device"), HttpMethod.POST, addRequest, AddDeviceOutput.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getBody());

        AddDeviceOutput deviceOutput = response.getBody();
        assertNotNull(deviceOutput.id);
        assertEquals(deviceOutput.systemName, "Windows Server Ultimate");
        assertEquals(deviceOutput.deviceType, DeviceType.WINDOWS_SERVER);

        // Associate two existent services
        List<Long> serviceIds = new ArrayList<>();
        serviceIds.add(1L);
        serviceIds.add(2L);
        AssociateDeviceServicesInput associateInput = new AssociateDeviceServicesInput(deviceOutput.id, serviceIds);
        HttpEntity<AssociateDeviceServicesInput> associateRequest = new HttpEntity<>(associateInput, getAuthHeaders());

        ResponseEntity<String> associateResponse = testRestTemplate
                .exchange(getUri("device/associate/services"), HttpMethod.PUT, associateRequest, String.class);

        assertEquals(associateResponse.getStatusCode(), HttpStatus.NO_CONTENT);

        // Gets the service and check if the device was associated
        HttpEntity<String> getServiceRequest = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<GetServiceOutput> getServiceResponse = testRestTemplate.exchange(
                getUri("service/1"),
                HttpMethod.GET,
                getServiceRequest,
                GetServiceOutput.class);

        assertEquals(getServiceResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(getServiceResponse.getBody());
        GetServiceOutput serviceOutput = getServiceResponse.getBody();
        assertEquals("Cost Per Device", serviceOutput.name);
        assertEquals(3, serviceOutput.devices.size());
        serviceOutput.devices.sort(Comparator.comparing(ServiceDeviceOutput::getId));
        assertEquals(3L, serviceOutput.devices.get(2).id);
        assertEquals("Windows Server Ultimate", serviceOutput.devices.get(2).systemName);
        assertEquals(DeviceType.WINDOWS_SERVER, serviceOutput.devices.get(2).deviceType);

        // Gets the device and check the services
        HttpEntity<String> getRequest = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<GetDeviceOutput> getResponse = testRestTemplate.exchange(
                getUri("device/" + deviceOutput.id),
                HttpMethod.GET,
                getRequest,
                GetDeviceOutput.class);

        assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(getResponse.getBody());
        GetDeviceOutput getDeviceOutput = getResponse.getBody();

        assertEquals(2, getDeviceOutput.services.size());
        getDeviceOutput.services.sort(Comparator.comparing(DeviceServiceOutput::getServiceId));

        assertEquals(1L, getDeviceOutput.services.get(0).serviceId);
        assertEquals("Cost Per Device", getDeviceOutput.services.get(0).name);
        assertEquals(new BigDecimal("4.00"), getDeviceOutput.services.get(0).cost);
        assertEquals(2L, getDeviceOutput.services.get(1).serviceId);
        assertEquals("Windows Antivirus", getDeviceOutput.services.get(1).name);
        assertEquals(new BigDecimal("5.00"), getDeviceOutput.services.get(1).cost);

        // Calculates the cost for 5 new devices
        CalculateInputDevice calculateInputDevice = new CalculateInputDevice(deviceOutput.id, 5);
        List<CalculateInputDevice> devices = new ArrayList<>();
        devices.add(calculateInputDevice);
        CalculateInput calculateInput = new CalculateInput();
        calculateInput.setDevices(devices);

        HttpEntity<CalculateInput> calculateRequest = new HttpEntity<>(calculateInput, getAuthHeaders());
        ResponseEntity<CalculateOutput> calculateResponse = testRestTemplate.exchange(
                getUri("device/calculate"),
                HttpMethod.POST,
                calculateRequest,
                CalculateOutput.class);

        assertEquals(calculateResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(calculateResponse.getBody());
        assertEquals(new BigDecimal("45.00"), calculateResponse.getBody().total);

        // deletes the device
        HttpEntity<String> deleteRequest = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<String> deleteResponse = testRestTemplate.exchange(
                getUri("device/" + deviceOutput.id),
                HttpMethod.DELETE,
                deleteRequest,
                String.class);
        assertEquals(deleteResponse.getStatusCode(), HttpStatus.NO_CONTENT);

        // Make sure that the device was deleted
        ResponseEntity<GetDeviceOutput> getResponse2 = testRestTemplate.exchange(
                getUri("device/" + deviceOutput.id),
                HttpMethod.GET,
                getRequest,
                GetDeviceOutput.class);

        assertEquals(getResponse2.getStatusCode(), HttpStatus.NOT_FOUND);

        // Make sure that the service wasn't deleted
        ResponseEntity<GetServiceOutput> getServiceResponse2 = testRestTemplate.exchange(
                getUri("service/1"),
                HttpMethod.GET,
                getServiceRequest,
                GetServiceOutput.class);

        assertEquals(getServiceResponse2.getStatusCode(), HttpStatus.OK);
        assertNotNull(getServiceResponse2.getBody());
        GetServiceOutput serviceOutput2 = getServiceResponse2.getBody();
        assertEquals("Cost Per Device", serviceOutput2.name);
        assertEquals(2, serviceOutput2.devices.size());
    }

    private HttpHeaders getAuthHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }

    private URI getUri(String resource) throws URISyntaxException {
        final String url = baseUrl + resource;
        return new URI(url);
    }

    private ResponseEntity<String> authenticate() throws Exception {
        final String url = baseUrl + "login";
        final URI uri = new URI(url);

        JSONObject json = new JSONObject();
        json.put("username", this.username);
        json.put("password", this.password);

        ResponseEntity<String> response = testRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                new HttpEntity<>(json.toString()),
                String.class);

        return response;
    }


}
