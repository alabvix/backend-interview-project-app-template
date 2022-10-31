package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.exception.DeviceNotFoundException;
import com.ninjaone.rmm.device.payload.*;
import com.ninjaone.rmm.service.ServiceEntity;
import com.ninjaone.rmm.service.ServiceNotFoundException;
import com.ninjaone.rmm.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeviceServiceUnitTest {

    private DeviceService deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private DeviceConverter deviceConverter;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        deviceService = new DeviceService(deviceRepository, serviceRepository, deviceConverter);
    }

    @Test
    @DisplayName("Given 2 windows with $13 services cost per device and 3 macs with $14 services cost per device, should calculate $71 as total cost.")
    public void calculateCost_2windows$13_3Macs$14_shouldCalculate$71(){
        // given
        CalculateInput input = new CalculateInput();
        input.setDevices(List.of(
                new CalculateInputDevice(1L,2),
                new CalculateInputDevice(2L,3)));

        final DeviceEntity deviceWindows =
                new DeviceEntity(1L, "Windows 10", DeviceType.WINDOWS_WORKSTATION,
                        Set.of( new ServiceEntity(1L, "Device Cost", new BigDecimal("4.00")),
                                new ServiceEntity(2L, "Backup", new BigDecimal("3.00")),
                                new ServiceEntity(3L, "Screen Share", new BigDecimal("1.00")),
                                new ServiceEntity(4L, "Antivirus Windows", new BigDecimal("5.00"))));

        final DeviceEntity deviceMac =
                new DeviceEntity(2L, "MacBook Pro", DeviceType.MAC,
                        Set.of( new ServiceEntity(1L, "Device Cost", new BigDecimal("4.00")),
                                new ServiceEntity(2L, "Backup", new BigDecimal("3.00")),
                                new ServiceEntity(3L, "Screen Share", new BigDecimal("1.00")),
                                new ServiceEntity(5L, "Antivirus Mac", new BigDecimal("7.00"))));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceWindows));
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(deviceMac));

        // when
        CalculateOutput output = deviceService.calculateCost(input);

        // then
        assertEquals(new BigDecimal("71"), output.total);
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).findById(2L);

    }

    @Test
    @DisplayName("Given 2 windows with $13 services cost per device and 3 macs with $14 services cost per device, should calculate $71 as total cost.")
    public void calculateCostDetailed_2windows$13_3Macs$14_shouldCalculate$71(){

        // given
        CalculateInput input = new CalculateInput();
        input.setDevices(List.of(
                new CalculateInputDevice(1L,2),
                new CalculateInputDevice(2L,3)));

        final DeviceEntity deviceWindows =
                new DeviceEntity(1L, "Windows 10", DeviceType.WINDOWS_WORKSTATION,
                        Set.of(new ServiceEntity(1L, "Device Cost", new BigDecimal("4.00")),
                                new ServiceEntity(2L, "Backup", new BigDecimal("3.00")),
                                new ServiceEntity(3L, "Screen Share", new BigDecimal("1.00")),
                                new ServiceEntity(4L, "Antivirus Windows", new BigDecimal("5.00"))));

        final DeviceEntity deviceMac =
                new DeviceEntity(2L, "MacBook Pro", DeviceType.MAC,
                        Set.of(new ServiceEntity(1L, "Device Cost", new BigDecimal("4.00")),
                                new ServiceEntity(2L, "Backup", new BigDecimal("3.00")),
                                new ServiceEntity(3L, "Screen Share", new BigDecimal("1.00")),
                                new ServiceEntity(5L, "Antivirus Mac", new BigDecimal("7.00"))));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceWindows));
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(deviceMac));

        // when
        CalculateDetailedOutput output = deviceService.calculateCostDetailed(input);

        // then
        assertEquals(new BigDecimal("71"), output.total);
        assertEquals(new BigDecimal("26"), output.devices.get(0).cost);
        assertEquals(new BigDecimal("45"), output.devices.get(1).cost);

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).findById(2L);

    }

    @Test
    @DisplayName("Given not associated services to a device should associate.")
    public void associateServices_notAssociatedServices_shouldAssociate(){
        // given
        AssociateDeviceServicesInput input = new AssociateDeviceServicesInput(1L, List.of(1L,2L,3L));
        final DeviceEntity deviceWindows = new DeviceEntity(1L, "Windows 10",
                DeviceType.WINDOWS_WORKSTATION, new HashSet<>());

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceWindows));
        when(serviceRepository.findById(1L)).thenReturn(
                Optional.of(new ServiceEntity(1L,"Device Cost", new BigDecimal("4"))));
        when(serviceRepository.findById(2L)).thenReturn(
                Optional.of(new ServiceEntity(2L,"Backup", new BigDecimal("6"))));
        when(serviceRepository.findById(3L)).thenReturn(
                Optional.of(new ServiceEntity(3L,"Antivirus Windows", new BigDecimal("16"))));

        // when
        deviceService.associateServices(input);

        // then
        List<ServiceEntity> services = new ArrayList<>(deviceWindows.getServices());
        services.sort(Comparator.comparing(ServiceEntity::getId));

        assertEquals(3, services.size());
        assertEquals(1L, services.get(0).getId());
        assertEquals("Device Cost", services.get(0).getName());
        assertEquals(new BigDecimal("4"), services.get(0).getCost());

        assertEquals(2L, services.get(1).getId());
        assertEquals("Backup", services.get(1).getName());
        assertEquals(new BigDecimal("6"), services.get(1).getCost());

        assertEquals(3L, services.get(2).getId());
        assertEquals("Antivirus Windows", services.get(2).getName());
        assertEquals(new BigDecimal("16"), services.get(2).getCost());

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(deviceWindows);

        verify(serviceRepository, times(1)).findById(1L);
        verify(serviceRepository, times(1)).findById(2L);
        verify(serviceRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Given 2 already associated services and 1 new, should associate just the new one.")
    public void associateServices_2AlreadyAssociatedServices1New_shouldAssociateTheNewOne(){
        // given
        AssociateDeviceServicesInput input = new AssociateDeviceServicesInput(1L, List.of(1L,2L,3L));
        Set<ServiceEntity> services = new HashSet<>();
        services.add(new ServiceEntity(1L,"Device Cost", new BigDecimal("4")));
        services.add(new ServiceEntity(2L,"Backup", new BigDecimal("6")));
        final DeviceEntity deviceWindows = new DeviceEntity(1L, "Windows 10",
                DeviceType.WINDOWS_WORKSTATION, services);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceWindows));
        when(serviceRepository.findById(1L)).thenReturn(
                Optional.of(new ServiceEntity(1L,"Device Cost", new BigDecimal("4"))));
        when(serviceRepository.findById(2L)).thenReturn(
                Optional.of(new ServiceEntity(2L,"Backup", new BigDecimal("6"))));
        when(serviceRepository.findById(3L)).thenReturn(
                Optional.of(new ServiceEntity(3L,"Antivirus Windows", new BigDecimal("16"))));

        // when
        deviceService.associateServices(input);

        // then
        List<ServiceEntity> serviceList = new ArrayList<>(deviceWindows.getServices());
        serviceList.sort(Comparator.comparing(ServiceEntity::getId));

        assertEquals(3, deviceWindows.getServices().size());
        assertEquals(1L, serviceList.get(0).getId());
        assertEquals("Device Cost", serviceList.get(0).getName());
        assertEquals(new BigDecimal("4"), serviceList.get(0).getCost());
        assertEquals(2L, serviceList.get(1).getId());
        assertEquals("Backup", serviceList.get(1).getName());
        assertEquals(new BigDecimal("6"), serviceList.get(1).getCost());
        assertEquals(3L, serviceList.get(2).getId());
        assertEquals("Antivirus Windows", serviceList.get(2).getName());
        assertEquals(new BigDecimal("16"),serviceList.get(2).getCost());

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(deviceWindows);

        verify(serviceRepository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Given a not found devices should throws a device not found exception.")
    public void associateServices_deviceNotFound_shouldTrowsDeviceNotFoundException(){
        // given
        AssociateDeviceServicesInput input = new AssociateDeviceServicesInput(1L, List.of(1L,2L,3L));
        final DeviceEntity deviceWindows = new DeviceEntity(1L, "Windows 10",
                DeviceType.WINDOWS_WORKSTATION, new HashSet<>());

        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> deviceService.associateServices(input)
        );

        // then
        assertTrue(exception.getMessage().contains("Device with id 1 not found"));
        verify(deviceRepository, times(1)).findById(1L);

    }

    @Test
    @DisplayName("Given a not found service should throws a service not found exception.")
    public void associateServices_serviceNotFound_shouldTrowsServiceNotFoundException(){
        // given
        AssociateDeviceServicesInput input = new AssociateDeviceServicesInput(1L, List.of(1L,2L,3L));
        final DeviceEntity deviceWindows = new DeviceEntity(1L, "Windows 10",
                DeviceType.WINDOWS_WORKSTATION, new HashSet<>());

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceWindows));
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        ServiceNotFoundException exception = assertThrows(
                ServiceNotFoundException.class,
                () -> deviceService.associateServices(input)
        );

        // then
        assertTrue(exception.getMessage().contains("Service not found with id: 1"));
        verify(deviceRepository, times(1)).findById(1L);
        verify(serviceRepository, times(1)).findById(1L);

    }

    @Test
    public void addDevice(){
        // given
        AddDeviceInput input = new AddDeviceInput("Ubuntu Linux", DeviceType.LINUX_WORKSTATION);
        AddDeviceOutput expectedOutput = new AddDeviceOutput(1L,"Ubuntu Linux", DeviceType.LINUX_WORKSTATION);
        final DeviceEntity device = new DeviceEntity(null, "Ubuntu Linux",
                DeviceType.LINUX_WORKSTATION, new HashSet<>());

        final DeviceEntity savedDevice = new DeviceEntity(1L, "Ubuntu Linux",
                DeviceType.LINUX_WORKSTATION, new HashSet<>());

        when(deviceConverter.toEntity(input)).thenReturn(device);
        when(deviceConverter. toAddDeviceOutput(savedDevice)).thenReturn(expectedOutput);
        when(deviceRepository.save(device)).thenReturn(savedDevice);

        // when
        final AddDeviceOutput output = deviceService.addDevice(input);

        // then
        assertEquals("Ubuntu Linux", output.systemName);
        assertEquals( DeviceType.LINUX_WORKSTATION, output.deviceType);
        assertEquals( 1L, output.id);

        verify(deviceRepository, times(1)).save(device);
        verify(deviceConverter, times(1)).toEntity(input);
        verify(deviceConverter, times(1)).toAddDeviceOutput(savedDevice);

    }

    @Test
    public void updateDevice(){
        // given
        final UpdateDeviceInput input =
                new UpdateDeviceInput(1L, "Win11", DeviceType.WINDOWS_SERVER);
        final DeviceEntity deviceWindows = new DeviceEntity(1L, "Windows 10",
                DeviceType.WINDOWS_WORKSTATION, new HashSet<>());
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceWindows));

        // when
        deviceService.updateDevice(input);

        // then
        assertEquals(1L, deviceWindows.getId());
        assertEquals("Win11", deviceWindows.getSystemName());
        assertEquals(DeviceType.WINDOWS_SERVER, deviceWindows.getDeviceType());
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(deviceWindows);
    }

    @Test
    public void deleteDevice(){

    }

}
