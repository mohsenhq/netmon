package org.redapps.netmon.service;

import org.redapps.netmon.exception.AccessDeniedException;
import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.Device;
import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.payload.DeviceRequest;
import org.redapps.netmon.payload.ColocationDeviceResponse;
import org.redapps.netmon.payload.DeviceStatusRequest;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.repository.NetmonServiceRepository;
import org.redapps.netmon.repository.ColocationDeviceRepository;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

@Service
public class DeviceService {

    private final NetmonServiceRepository colocationRepository;
    private final ColocationDeviceRepository colocationDeviceRepository;
    private final LogService logService;

    @Autowired
    public DeviceService(ColocationDeviceRepository colocationDeviceRepository, LogService logService,
                         NetmonServiceRepository colocationRepository) {
        this.colocationDeviceRepository = colocationDeviceRepository;
        this.logService = logService;
        this.colocationRepository = colocationRepository;
    }

    /**
     * Create a colocation device.
     * @param colocationDeviceRequest the device information object
     * @param currentUser the user id who currently logged in
     * @param colocationId the unique colocation number
     * @return a new device object
     */
    public Device create(DeviceRequest colocationDeviceRequest, UserPrincipal currentUser, ServiceIdentity colocationId) {

        // find service by id
        NetmonService netmonService = colocationRepository.getOne(colocationId);

        // find all devices by colocation id
        List<Device> allServiceDevices = colocationDeviceRepository.findAllByNetmonServiceId(colocationId);

        // check the device capacity
        if(allServiceDevices.size() >= netmonService.getUnitNumber()){
            logService.createLog("CREATE_COLOCATION_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", colocationDeviceRequest.toString(),
                    "Device capacity is completed.");
            throw new AccessDeniedException("Device capacity is completed.");
        }

        // create a new device object
        Device colocationDevice = new Device(colocationDeviceRequest.getName(),
                colocationDeviceRequest.getDescription(), netmonService);

        logService.createLog("CREATE_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[colocationId=" + colocationId + "]", colocationDeviceRequest.toString(), "");
        return colocationDeviceRepository.save(colocationDevice);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param deviceId the unique device number
     */
    public void delete(UserPrincipal currentUser, Long deviceId) {
        logService.createLog("DELETE_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[deviceId=" + deviceId + "]", "", "");

        // delete the device
        colocationDeviceRepository.deleteById(deviceId);
    }

    /**
     * Changing the name of device.
     * @param deviceId the unique device number
     * @param deviceRequest the device information object
     * @param currentUser the user id who currently logged in
     * @return update device
     */
    public Device update(Long deviceId, DeviceRequest deviceRequest, UserPrincipal currentUser) {

        // find the device by id
        Optional<Device> deviceOptional = colocationDeviceRepository.findById(deviceId);
        if (!deviceOptional.isPresent()) {
            logService.createLog("UPDATE_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[deviceId=" + deviceId + "]", deviceRequest.toString(), "The device not found.");
            throw new ResourceNotFoundException("Device", "id", deviceId);
        }
        Device device = deviceOptional.get();

        // update the name and description
        device.setName(deviceRequest.getName());
        device.setDescription(deviceRequest.getDescription());

        logService.createLog("UPDATE_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "[deviceId=" + deviceId + "]",
                deviceRequest.toString(), "");

        // store the device changes
        return colocationDeviceRepository.save(device);
    }

    /**
     * Changing the status of device.
     * @param deviceId the unique device number
     * @param deviceStatusRequest the status information object
     * @param currentUser the user id who currently logged in
     * @return update device
     */
    public Device changeStatus(Long deviceId, DeviceStatusRequest deviceStatusRequest,
                               UserPrincipal currentUser) {

        // find the device by id
        Optional<Device> deviceOptional = colocationDeviceRepository.findById(deviceId);
        if (!deviceOptional.isPresent()) {
            logService.createLog("CHANGE_DEVICE_STATUS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[deviceId=" + deviceId + "]", deviceStatusRequest.toString(), "The device not found.");
            throw new ResourceNotFoundException("Device", "id", deviceId);
        }
        Device device = deviceOptional.get();

        // change the device status
        device.setStatus(deviceStatusRequest.getStatus());

        logService.createLog("CHANGE_DEVICE_STATUS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[deviceId=" + deviceId + "]", deviceStatusRequest.toString(), "");

        // store device changes
        return colocationDeviceRepository.save(device);
    }

    /**
     * Get list of collocation devices
     * @param currentUser the user id who currently logged in
     * @param colocationId the unique colocation number
     * @param page the page number of the response (default value is 0) page number
     * @param size the page size of each response (default value is 30) page size
     * @return list of devices
     */
    public PagedResponse<ColocationDeviceResponse> getColocationDevices(UserPrincipal currentUser,
                                                                          ServiceIdentity colocationId,
                                                                          int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all devices by colocation id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Device> colocationDevices = colocationDeviceRepository.findAllByNetmonServiceId(colocationId, pageable);

        if (colocationDevices.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), colocationDevices.getNumber(),
                    colocationDevices.getSize(), colocationDevices.getTotalElements(), colocationDevices.getTotalPages(), colocationDevices.isLast());
        }

        // store devices into a list
        Vector<ColocationDeviceResponse> colocationDeviceResponses = new Vector<>(10);
        ColocationDeviceResponse colocationDeviceResponse;
        for (Device device : colocationDevices) {
            colocationDeviceResponse = new ColocationDeviceResponse(device.getId(), device.getName(),
                    device.getDescription(), device.getStatus());

            colocationDeviceResponses.add(colocationDeviceResponse);
        }

        logService.createLog("GET_ALL_COLOCATION_DEVICES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[colocationId=" + colocationId + "]", "", "");

        return new PagedResponse<>(colocationDeviceResponses, colocationDevices.getNumber(),
                colocationDevices.getSize(), colocationDevices.getTotalElements(), colocationDevices.getTotalPages(), colocationDevices.isLast());
    }

    /**
     * Get a collocation device by id
     * @param deviceId the unique device number
     * @param currentUser the user id who currently logged in
     * @return a device response
     */
    public ColocationDeviceResponse getColocationDeviceById(Long deviceId, UserPrincipal currentUser) {

        // find the device by id
        Optional<Device> deviceOptional = colocationDeviceRepository.findById(deviceId);
        if (!deviceOptional.isPresent()) {
            logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[deviceId=" + deviceId + "]", "", "The device not found.");
            throw new ResourceNotFoundException("Device", "id", deviceId);
        }
        Device device = deviceOptional.get();

        logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[deviceId=" + deviceId + "]", "", "");

        // create a new response object
        return new ColocationDeviceResponse(device.getId(), device.getName(),
                device.getDescription(), device.getStatus());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
