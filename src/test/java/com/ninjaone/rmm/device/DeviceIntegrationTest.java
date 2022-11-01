package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.*;
import com.ninjaone.rmm.helper.IntegrationTestHelper;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import com.ninjaone.rmm.service.payload.ServiceDeviceOutput;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:IntegrationTest.properties")
public class DeviceIntegrationTest {

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
    @DisplayName("Try to create a device with an empty name. Should return Http status 400")
    public void createDeviceWithEmptyName() throws Exception {
        AddDeviceInput input = new AddDeviceInput("", DeviceType.WINDOWS_SERVER);
        HttpEntity<AddDeviceInput> addRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<AddDeviceOutput> response = testRestTemplate
                .exchange(getUri("device"), HttpMethod.POST, addRequest, AddDeviceOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Try to create a device with duplicated name. Should return Http status 400")
    public void createDeviceWithDuplicatedName() throws Exception {
        AddDeviceInput input = new AddDeviceInput("MacBook Pro", DeviceType.MAC);
        HttpEntity<AddDeviceInput> addRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<String> response = testRestTemplate
                .exchange(getUri("device"), HttpMethod.POST, addRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Device with given system name already exists"));
    }

    @Test
    @DisplayName("Change device system name and type")
    public void changeDevice() throws Exception {
        // Get existent device
        HttpEntity<String> getRequest = new HttpEntity<>(testHelper.getAuthHeaders(authToken));
        ResponseEntity<GetDeviceOutput> getResponse = testRestTemplate.exchange(
                getUri("device/1"),
                HttpMethod.GET,
                getRequest,
                GetDeviceOutput.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        GetDeviceOutput getDeviceOutput = getResponse.getBody();

        // Update name and type
        UpdateDeviceInput input = new UpdateDeviceInput(getDeviceOutput.id,"Linux Arc Server v2",
                DeviceType.LINUX_SERVER);

        HttpEntity<UpdateDeviceInput> putRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<String> putResponse = testRestTemplate.exchange(
                getUri("device/"),
                HttpMethod.PUT,
                putRequest,
                String.class);

        assertEquals(HttpStatus.NO_CONTENT, putResponse.getStatusCode());

        // Checks the updated device
        getResponse = testRestTemplate.exchange(
                getUri("device/1"),
                HttpMethod.GET,
                getRequest,
                GetDeviceOutput.class);

        assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(getResponse.getBody());
        getDeviceOutput = getResponse.getBody();

        assertEquals("Linux Arc Server v2", getDeviceOutput.systemName);
        assertEquals(DeviceType.LINUX_SERVER, getDeviceOutput.deviceType);

    }

    @Test
    @DisplayName("Creates a new device, associate 2 services, calculate the total cost and deletes the device")
    public void createDeviceTest() throws Exception {

        // Add a new Device
        AddDeviceInput input = new AddDeviceInput("Windows Server Ultimate", DeviceType.WINDOWS_SERVER);
        HttpEntity<AddDeviceInput> addRequest = new HttpEntity<>(input, testHelper.getAuthHeaders(authToken));
        ResponseEntity<AddDeviceOutput> response = testRestTemplate
                .exchange(getUri("device"), HttpMethod.POST, addRequest, AddDeviceOutput.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        AddDeviceOutput deviceOutput = response.getBody();
        assertNotNull(deviceOutput.id);
        assertEquals(deviceOutput.systemName, "Windows Server Ultimate");
        assertEquals(deviceOutput.deviceType, DeviceType.WINDOWS_SERVER);

        // Associates two existent services
        List<Long> serviceIds = new ArrayList<>();
        serviceIds.add(1L);
        serviceIds.add(2L);
        AssociateDeviceServicesInput associateInput = new AssociateDeviceServicesInput(deviceOutput.id, serviceIds);
        HttpEntity<AssociateDeviceServicesInput> associateRequest = new HttpEntity<>(associateInput,
                testHelper.getAuthHeaders(authToken));

        ResponseEntity<String> associateResponse = testRestTemplate
                .exchange(getUri("device/associate/services"), HttpMethod.PUT, associateRequest, String.class);

        assertEquals(associateResponse.getStatusCode(), HttpStatus.NO_CONTENT);

        // Gets the service and check if the device was associated
        HttpEntity<String> getServiceRequest = new HttpEntity<>(testHelper.getAuthHeaders(authToken));
        ResponseEntity<GetServiceOutput> getServiceResponse = testRestTemplate.exchange(
                getUri("service/1"),
                HttpMethod.GET,
                getServiceRequest,
                GetServiceOutput.class);

        assertEquals(HttpStatus.OK, getServiceResponse.getStatusCode());
        assertNotNull(getServiceResponse.getBody());
        GetServiceOutput serviceOutput = getServiceResponse.getBody();
        assertEquals("Cost Per Device", serviceOutput.name);
        assertEquals(3, serviceOutput.devices.size());
        serviceOutput.devices.sort(Comparator.comparing(ServiceDeviceOutput::getId));
        assertEquals(3L, serviceOutput.devices.get(2).id);
        assertEquals("Windows Server Ultimate", serviceOutput.devices.get(2).systemName);
        assertEquals(DeviceType.WINDOWS_SERVER, serviceOutput.devices.get(2).deviceType);

        // Gets the device and check the services
        HttpEntity<String> getRequest = new HttpEntity<>(testHelper.getAuthHeaders(authToken));
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

        HttpEntity<CalculateInput> calculateRequest = new HttpEntity<>(calculateInput,
                testHelper.getAuthHeaders(authToken));
        ResponseEntity<CalculateOutput> calculateResponse = testRestTemplate.exchange(
                getUri("device/calculate"),
                HttpMethod.POST,
                calculateRequest,
                CalculateOutput.class);

        assertEquals(calculateResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(calculateResponse.getBody());
        assertEquals(new BigDecimal("45.00"), calculateResponse.getBody().total);

        // deletes the device
        HttpEntity<String> deleteRequest = new HttpEntity<>(testHelper.getAuthHeaders(authToken));
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

    private URI getUri(String resource) throws URISyntaxException {
        final String url = baseUrl + resource;
        return new URI(url);
    }

}
