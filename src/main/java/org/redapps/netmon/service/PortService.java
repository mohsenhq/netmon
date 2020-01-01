package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.Port;
import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.NetmonServiceRepository;
import org.redapps.netmon.repository.ServicePortRepository;
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
import java.util.Optional;
import java.util.Vector;

@Service
public class PortService {

    private final NetmonServiceRepository netmonServiceRepository;
    private final ServicePortRepository servicePortRepository;
    private final LogService logService;

    @Autowired
    public PortService(LogService logService, NetmonServiceRepository netmonServiceRepository, ServicePortRepository servicePortRepository) {
        this.logService = logService;
        this.netmonServiceRepository = netmonServiceRepository;
        this.servicePortRepository = servicePortRepository;
    }

    /**
     * @param servicePortRequest the port information object
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @return port response
     */
    public Port create(ServicePortRequest servicePortRequest, UserPrincipal currentUser, Long serviceId) {

        // create a new port object
        Port port = new Port();

        // set description and port
        port.setDescription(servicePortRequest.getDescription());
        port.setPort(servicePortRequest.getPort());

        // find the service by id and port assign to it
        NetmonService netmonService = netmonServiceRepository.getOne(serviceId);
        port.setNetmonService(netmonService);

        logService.createLog("CREATE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", servicePortRequest.toString(), "");

        // store port object
        return servicePortRepository.save(port);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param portId the unique port number
     */
    public void delete(UserPrincipal currentUser, Long portId) {
        logService.createLog("DELETE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[portId=" + portId + "]", "", "");
        // delete the port
        servicePortRepository.deleteById(portId);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param portId the unique port number
     * @param servicePortRequest the port information object
     * @return port response
     */
    public Port update(UserPrincipal currentUser, Long portId, ServicePortRequest servicePortRequest) {

        // find the port by id
        Optional<Port> portOptional = servicePortRepository.findById(portId);
        if(!portOptional.isPresent()) {
            logService.createLog("UPDATE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[portId=" + portId + "]", servicePortRequest.toString(), "The port not found.");
            throw new ResourceNotFoundException("Port", "portId", portId);
        }
        Port port = portOptional.get();

        // update port number and description
        port.setPort(servicePortRequest.getPort());
        port.setDescription(servicePortRequest.getDescription());

        logService.createLog("UPDATE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[portId=" + portId + "]", servicePortRequest.toString(), "");

        // store port changes
        return servicePortRepository.save(port);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return port responses page by page
     */
    public PagedResponse<ServicePortResponse> getServicePorts(UserPrincipal currentUser, Long serviceId,
                                                              int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all ports by service id and sorted by createdAt
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Port> servicePorts = servicePortRepository.findAllByNetmonServiceId(serviceId, pageable);

        if(servicePorts.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), servicePorts.getNumber(),
                    servicePorts.getSize(), servicePorts.getTotalElements(), servicePorts.getTotalPages(),
                    servicePorts.isLast());
        }

        // store ports into a list
        Vector<ServicePortResponse> servicePortResponses = new Vector<>(10);
        ServicePortResponse servicePortResponse;
        for (Port port : servicePorts) {
                servicePortResponse = new ServicePortResponse();
                servicePortResponse.setDescription(port.getDescription());
                servicePortResponse.setId(port.getId());
                servicePortResponse.setPort(port.getPort());

                servicePortResponses.add(servicePortResponse);
        }

        logService.createLog("GET_ALL_PORTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", "", "");

        return new PagedResponse<>(servicePortResponses, servicePorts.getNumber(),
                servicePorts.getSize(), servicePorts.getTotalElements(), servicePorts.getTotalPages(),
                servicePorts.isLast());
    }

    /**
     * @param portId the unique port number
     * @param currentUser the user id who currently logged in
     * @return port response
     */
    public ServicePortResponse getServicePortById(Long portId, UserPrincipal currentUser) {

        // find the port by id
        Optional<Port> portOptional = servicePortRepository.findById(portId);
        if(!portOptional.isPresent()) {
            logService.createLog("GET_OS_TYPE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[portId=" + portId + "]", "", "The port not found.");
            throw new ResourceNotFoundException("Port", "portId", portId);
        }
        Port port = portOptional.get();

        logService.createLog("GET_SERVICE_PORT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[portId=" + portId + "]", "", "");

        // create a new response object
        return new ServicePortResponse(port.getId(), port.getPort(),
                port.getDescription());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
