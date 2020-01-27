package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.IP;
import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.payload.ServiceIPRequest;
import org.redapps.netmon.payload.ServiceIPResponse;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.repository.NetmonServiceRepository;
import org.redapps.netmon.repository.ServiceIPRepository;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Vector;

@Service
public class IPService {

    private final NetmonServiceRepository netmonServiceRepository;
    private final ServiceIPRepository serviceIPRepository;
    private final LogService logService;

    @Autowired
    public IPService(NetmonServiceRepository netmonServiceRepository, LogService logService,
                     ServiceIPRepository serviceIPRepository) {
        this.netmonServiceRepository = netmonServiceRepository;
        this.logService = logService;
        this.serviceIPRepository = serviceIPRepository;
    }

    /**
     * @param serviceIPRequest the ip information object
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @return ip
     */
    public IP create(ServiceIPRequest serviceIPRequest, UserPrincipal currentUser , Long serviceId, LocalDate createDate) {

        // find service by id
        NetmonService netmonService = netmonServiceRepository.getOne(new ServiceIdentity(serviceId, createDate));

        // create a new ip object
        IP ip = new IP(serviceIPRequest.getIp(), serviceIPRequest.getDescription(), netmonService);

        logService.createLog("CREATE_SERVICE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", serviceIPRequest.toString(), "");

        // store the object
        return serviceIPRepository.save(ip);
    }

    /**
     * @param ipId the unique ip number
     * @param serviceIPRequest the ip information object
     * @param currentUser the user id who currently logged in
     * @return ip
     */
    public IP update(Long ipId, ServiceIPRequest serviceIPRequest, UserPrincipal currentUser) {

        // find ip by id
        Optional<IP> ipOptional = serviceIPRepository.findById(ipId);
        if (!ipOptional.isPresent()) {
            logService.createLog("UPDATE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[ipId=" + ipId + "]", serviceIPRequest.toString(), "The ip not found.");
            throw new ResourceNotFoundException("IP", "id", ipId);
        }
        IP ip = ipOptional.get();

        // update ip and description
        ip.setIp(serviceIPRequest.getIp());
        ip.setDescription(serviceIPRequest.getDescription());

        logService.createLog("UPDATE_SERVICE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[ipId=" + ipId + "]", serviceIPRequest.toString(), "");

        // store ip changes
        return serviceIPRepository.save(ip);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param ipId the unique ip number
     */
    public void delete(UserPrincipal currentUser, Long ipId) {
        logService.createLog("DELETE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[ipId=" + ipId + "]", "", "");

        // delete the ip
        serviceIPRepository.deleteById(ipId);
    }

    /**
     * @param serviceId the unique service number
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return ip response page by page
     */
    public PagedResponse<ServiceIPResponse> getServiceIps(Long serviceId, UserPrincipal currentUser,
                                                          int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all ips by service id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<IP> serviceIPs = serviceIPRepository.findByNetmonServiceId(serviceId, pageable);

        if(serviceIPs.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), serviceIPs.getNumber(),
                    serviceIPs.getSize(), serviceIPs.getTotalElements(), serviceIPs.getTotalPages(), serviceIPs.isLast());
        }

        // store ips into a list
        Vector<ServiceIPResponse> serviceIPResponses = new Vector<>(10);
        ServiceIPResponse serviceIPResponse;
        for (IP ip : serviceIPs) {
            serviceIPResponse = new ServiceIPResponse(ip.getId(), ip.getIp(), ip.getDescription());
            serviceIPResponses.add(serviceIPResponse);
        }

        logService.createLog("GET_IPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", "", "");

        return new PagedResponse<>(serviceIPResponses, serviceIPs.getNumber(),
                serviceIPs.getSize(), serviceIPs.getTotalElements(), serviceIPs.getTotalPages(), serviceIPs.isLast());
    }

    /**
     * @param ipId the unique ip number
     * @param currentUser the user id who currently logged in
     * @return ip response
     */
    public ServiceIPResponse getServiceIpById(Long ipId, UserPrincipal currentUser) {

        // find the ip by id
        Optional<IP> ipOptional = serviceIPRepository.findById(ipId);
        if (!ipOptional.isPresent()) {
            logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[ipId=" + ipId + "]", "", "The ip not found.");
            throw new ResourceNotFoundException("IP", "id", ipId);
        }
        IP ip = ipOptional.get();

        logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[ipId=" + ipId + "]", "", "");

        // create a new response object
        return new ServiceIPResponse(ip.getId(), ip.getIp(),
                ip.getDescription());
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

