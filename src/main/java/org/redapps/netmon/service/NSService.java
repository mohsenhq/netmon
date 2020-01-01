package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.*;
import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.NetmonServiceRepository;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.redapps.netmon.util.NetmonTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.redapps.netmon.exception.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Vector;

@Service
public class NSService {

    private final NetmonServiceRepository netmonServiceRepository;
    private final LogService logService;

    @Autowired
    public NSService(LogService logService, NetmonServiceRepository netmonServiceRepository) {
        this.logService = logService;
        this.netmonServiceRepository = netmonServiceRepository;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param companyId the unique company number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return colocation response page by page
     */
    public PagedResponse<ColocationResponse> getCompanyColocations(UserPrincipal currentUser,
                                                                     long companyId, int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all colocations by company id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<NetmonService> netmonServicesPage = netmonServiceRepository.findAllByServiceTypeAndCompanyId(
                NetmonTypes.SERVICE_TYPES.COLOCATION, companyId, pageable);

        if (netmonServicesPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), netmonServicesPage.getNumber(),
                    netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(), netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
        }

        // store colocations into a list
        Vector<ColocationResponse> colocationResponses = new Vector<>(10);
        ColocationResponse colocationResponse;
        for (NetmonService netmonService : netmonServicesPage) {
            colocationResponse = new ColocationResponse(netmonService.getId(), netmonService.getName(),
                    netmonService.getUnitNumber(), netmonService.getSlaType(),
                    netmonService.getDescription(), netmonService.getValidIp(), netmonService.getInvalidIp(),
                    netmonService.getStatus(), netmonService.getUsageType(), netmonService.getRackPosition(),
                    netmonService.getOsType().getId(), netmonService.getStartDate(), netmonService.getDuration(),
                    netmonService.getDiscountPercent());

            colocationResponses.add(colocationResponse);
        }

        logService.createLog("GET_ALL_COLOCATIONS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[companyId=" + companyId + "]", "", "");

        return new PagedResponse<>(colocationResponses, netmonServicesPage.getNumber(),
                netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(), netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return colocation responses page by page
     */
    public PagedResponse<ColocationResponse> getAllColocations(UserPrincipal currentUser,
                                                                 int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all colocations (type = 0)
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<NetmonService> netmonServicesPage = netmonServiceRepository.findAllByServiceType(0, pageable);

        if (netmonServicesPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), netmonServicesPage.getNumber(),
                    netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(), netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
        }

        // store colocations into a list
        Vector<ColocationResponse> colocationResponses = new Vector<>(10);
        ColocationResponse colocationResponse;
        for (NetmonService netmonService : netmonServicesPage) {
            colocationResponse = new ColocationResponse(netmonService.getId(), netmonService.getName(),
                    netmonService.getUnitNumber(), netmonService.getSlaType(),
                    netmonService.getDescription(), netmonService.getValidIp(), netmonService.getInvalidIp(),
                    netmonService.getStatus(), netmonService.getUsageType(),  netmonService.getRackPosition(),
                    netmonService.getOsType().getId(), netmonService.getStartDate(),
                    netmonService.getDuration(), netmonService.getDiscountPercent());

            colocationResponses.add(colocationResponse);

        }

        logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");
        return new PagedResponse<>(colocationResponses, netmonServicesPage.getNumber(),
                netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(), netmonServicesPage.getTotalPages(),
                netmonServicesPage.isLast());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param colocationRequest the document information object
     * @param company the company information object
     * @param technicalPerson the technicalPerson information object
     * @param osType the osType information object
     * @return service
     */
    public NetmonService createColocation(UserPrincipal currentUser, ColocationRequest colocationRequest,
                                           Company company, TechnicalPerson technicalPerson, OSType osType) {

        // create a new service object
        NetmonService netmonService = new NetmonService(colocationRequest.getSlaType(),
                colocationRequest.getDescription(),
                colocationRequest.getName(),
                NetmonTypes.SERVICE_TYPES.COLOCATION, colocationRequest.getUnitNumber(),
                colocationRequest.getValidIp(), colocationRequest.getInvalidIp(), colocationRequest.getStartDate(),
                colocationRequest.getDuration(), company, technicalPerson, osType);

        logService.createLog("CREATE_COLOCATION", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[companyId=" + company.getId() + "]", colocationRequest.toString(), "");

        // store the object
        return netmonServiceRepository.save(netmonService);
    }

    /**
     * @param colocationId the unique colocation number
     * @param currentUser the user id who currently logged in
     * @return colocation response
     */
    public ColocationResponse getColocationById(Long colocationId, UserPrincipal currentUser) {

        // find the colocation by id
        Optional<NetmonService> netmonServiceOptional = netmonServiceRepository.findById(colocationId);
        if (!netmonServiceOptional.isPresent()) {
            logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", "", "The colocation does not exists.");
            throw new ResourceNotFoundException("colocation", "colocationId", colocationId);
        }

        logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[colocationId=" + colocationId + "]", "", "");

        NetmonService netmonService = netmonServiceOptional.get();

        // create a new colocation response object
        return new ColocationResponse(netmonService.getId(), netmonService.getName(),
                netmonService.getUnitNumber(), netmonService.getSlaType(),
                netmonService.getDescription(), netmonService.getValidIp(), netmonService.getInvalidIp(),
                netmonService.getStatus(), netmonService.getUsageType(),
                netmonService.getRackPosition(), netmonService.getOsType().getId(),
                netmonService.getStartDate(), netmonService.getDuration(), netmonService.getDiscountPercent());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param vpsRequest the vps information object
     * @param company the company information object
     * @param technicalPerson the technicalPerson information object
     * @param osType the osType information object
     * @param vpsPlan the vpsPlan information object
     * @return service
     */
    public NetmonService createVPS(UserPrincipal currentUser, VpsRequest vpsRequest, Company company,
                                   TechnicalPerson technicalPerson, OSType osType, VpsPlan vpsPlan) {

        // create a new vps service
        NetmonService netmonService = new NetmonService(vpsRequest.getDescription(),
                vpsRequest.getName(), NetmonTypes.SERVICE_TYPES.VPS,
                vpsRequest.getValidIp(), vpsRequest.getInvalidIp(), vpsRequest.isVnc(), vpsPlan,
                vpsRequest.getExtraRam(), vpsRequest.getExtraCpu(), vpsRequest.getExtraDisk(),
                vpsRequest.getExtraTraffic(), vpsRequest.getDuration(), company, technicalPerson, osType,
                vpsPlan.getMonthlyPrice());

        logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "", vpsRequest.toString(), "");
        return netmonServiceRepository.save(netmonService);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param companyId the unique company number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return vps responses page by page
     */
    public PagedResponse<VpsResponse> getCustomerVPSes(UserPrincipal currentUser, Long companyId,
                                                       int page, int size) {
        validatePageNumberAndSize(page, size);

        // fid all vpses by service company id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<NetmonService> netmonServicesPage = netmonServiceRepository.findAllByServiceTypeAndCompanyId(
                NetmonTypes.SERVICE_TYPES.VPS, companyId, pageable);

        if (netmonServicesPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), netmonServicesPage.getNumber(),
                    netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(),
                    netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
        }

        // store vpses into a list
        Vector<VpsResponse> vpsResponses = new Vector<>(10);
        VpsResponse vpsResponse;
        for (NetmonService netmonService : netmonServicesPage) {
            vpsResponse = new VpsResponse(netmonService.getId(), netmonService.getName(),
                    netmonService.getDescription(), netmonService.getValidIp(), netmonService.getInvalidIp(),
                    netmonService.isVnc(),
                    netmonService.getStatus(), netmonService.getUsageType(), netmonService.getVpsPlan().getId(),
                    netmonService.getExtraRam(), netmonService.getExtraCpu(), netmonService.getExtraDisk(),
                    netmonService.getExtraTraffic(), netmonService.getTechnicalPerson().getId(),
                    netmonService.getOsType().getId(), netmonService.getStartDate(),
                    netmonService.getDuration(), netmonService.getDiscountPercent(), netmonService.getPrice(),
                    netmonService.getFinalPrice(), netmonService.getExtraPrice(), netmonService.getType(),
                    netmonService.getCompany().getUser().getId());

            vpsResponses.add(vpsResponse);
        }

        logService.createLog("GET_ALL_CUSTOMER_VPSES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[companyId=" + companyId + "]", "", "");

        return new PagedResponse<>(vpsResponses, netmonServicesPage.getNumber(),
                netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(), netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return vps responses page by page
     */
    public PagedResponse<VpsResponse> getAllVPSes(UserPrincipal currentUser,
                                                  int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all vpses (type = 1)
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<NetmonService> netmonServicesPage = netmonServiceRepository.findAllByServiceType(1, pageable);

        if (netmonServicesPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), netmonServicesPage.getNumber(),
                    netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(),
                    netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
        }

        // store the vpses int a list
        Vector<VpsResponse> vpsResponses = new Vector<>(10);
        VpsResponse vpsResponse;
        for (NetmonService netmonService : netmonServicesPage) {
            vpsResponse = new VpsResponse(netmonService.getId(), netmonService.getName(),
                    netmonService.getDescription(), netmonService.getValidIp(), netmonService.getInvalidIp(),
                    netmonService.isVnc(),
                    netmonService.getStatus(), netmonService.getUsageType(), netmonService.getVpsPlan().getId(),
                    netmonService.getExtraRam(), netmonService.getExtraCpu(), netmonService.getExtraDisk(),
                    netmonService.getExtraTraffic(), netmonService.getTechnicalPerson().getId(),
                    netmonService.getOsType().getId(), netmonService.getStartDate(),
                    netmonService.getDuration(), netmonService.getDiscountPercent(),
                    netmonService.getPrice(), netmonService.getFinalPrice(), netmonService.getExtraPrice(),
                    netmonService.getType(), netmonService.getCompany().getUser().getId());

            vpsResponses.add(vpsResponse);
        }

        logService.createLog("GET_ALL_VPSES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(vpsResponses, netmonServicesPage.getNumber(),
                netmonServicesPage.getSize(), netmonServicesPage.getTotalElements(), netmonServicesPage.getTotalPages(), netmonServicesPage.isLast());
    }

    /**
     * @param vpsId the unique vps number
     * @param currentUser the user id who currently logged in
     * @return vps response
     */
    public VpsResponse getVPSById(Long vpsId, UserPrincipal currentUser) {

        // find the service by id
        Optional<NetmonService> netmonServiceOptional = netmonServiceRepository.findById(vpsId);
        if (!netmonServiceOptional.isPresent()) {
            logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[vpsId=" + vpsId + "]", "", "This VPS does not exists.");
            throw new ResourceNotFoundException("VPS", "vpsId", vpsId);
        }

        NetmonService netmonService = netmonServiceOptional.get();
        logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[vpsId=" + vpsId + "]", "", "");

        // create a new response object
        return new VpsResponse(netmonService.getId(), netmonService.getName(), netmonService.getDescription(),
                netmonService.getValidIp(), netmonService.getInvalidIp(), netmonService.isVnc(),
                netmonService.getStatus(),
                netmonService.getUsageType(), netmonService.getVpsPlan().getId(), netmonService.getExtraRam(),
                netmonService.getExtraCpu(), netmonService.getExtraDisk(), netmonService.getExtraTraffic(),
                netmonService.getTechnicalPerson().getId(), netmonService.getOsType().getId(),
                netmonService.getStartDate(), netmonService.getDuration(), netmonService.getDiscountPercent(),
                netmonService.getPrice(), netmonService.getFinalPrice(), netmonService.getExtraPrice(),
                netmonService.getType(), netmonService.getCompany().getUser().getId());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @param serviceConfirmRequest the service information object to confirm
     * @return a new service object
     */
    public NetmonService managerConfirmService(UserPrincipal currentUser, Long serviceId,
                                               ServiceConfirmRequest serviceConfirmRequest) {

        // find the service by id
        Optional<NetmonService> netmonServiceOptional = netmonServiceRepository.findById(serviceId);
        if (!netmonServiceOptional.isPresent()) {
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(), "This service does not exists.");
            throw new ResourceNotFoundException("Service", "serviceId", serviceConfirmRequest);
        }
        NetmonService netmonService = netmonServiceOptional.get();

        // change the service status
        netmonService.setStatus(serviceConfirmRequest.getStatus());

        // change the price and discount percent if manager accepted this
        if(serviceConfirmRequest.getStatus() == NetmonStatus.ServiceStatus.ACCEPTED_BY_MANAGER){
            netmonService.setExtraPrice(serviceConfirmRequest.getExtraPrice());
            netmonService.setDescription(serviceConfirmRequest.getDescription());
            netmonService.setDiscountPercent(serviceConfirmRequest.getDiscountPercent());
            netmonService.setType(serviceConfirmRequest.getType());

            // check non-setadi service
            if(netmonService.getType() != 1 && netmonService.getDiscountPercent() != 0)
                netmonService.setFinalPrice(netmonService.getPrice()
                        * serviceConfirmRequest.getDiscountPercent() / 100);
        }

        logService.createLog("MANAGER_CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(), "");
        return netmonServiceRepository.save(netmonService);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @param serviceConfirmRequest the service information object to confirm
     * @return a new service object
     */
    public NetmonService customerConfirmService(UserPrincipal currentUser, Long serviceId,
                                                ServiceConfirmRequest serviceConfirmRequest) {

        // find the service by id
        Optional<NetmonService> netmonServiceOptional = netmonServiceRepository.findById(serviceId);
        if (!netmonServiceOptional.isPresent()) {
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(), "This service does not exists.");
            throw new ResourceNotFoundException("Service", "serviceId", serviceConfirmRequest);
        }
        NetmonService netmonService = netmonServiceOptional.get();

        // service is non-setadi
        if(netmonService.getStatus() == NetmonStatus.ServiceStatus.ACCEPTED_BY_MANAGER &&
                netmonService.getType() == 2){
            netmonService.setStatus(serviceConfirmRequest.getStatus());
        } else{
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(), "Bad request.");
            throw new AccessDeniedException("BadRequest");
        }

        logService.createLog("CUSTOMER_CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(), "");
        return netmonServiceRepository.save(netmonService);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @param renameServiceRequest the new name of service and start date
     * @return a new service object
     */
    public NetmonService renameService(UserPrincipal currentUser, Long serviceId,
                                       RenameServiceRequest renameServiceRequest) {

        // find a service by id
        Optional<NetmonService> netmonServiceOptional = netmonServiceRepository.findById(serviceId);
        if (!netmonServiceOptional.isPresent()) {
            logService.createLog("RENAME_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", renameServiceRequest.toString(), "This servicedoes not exists.");
            throw new ResourceNotFoundException("service", "serviceId", serviceId);
        }
        NetmonService netmonService = netmonServiceOptional.get();

        // service is setadi and accepted by manager or is non-setadi  and accepted by user
        if ((netmonService.getType() == 1 && netmonService.getStatus() == NetmonStatus.ServiceStatus.ACCEPTED_BY_MANAGER)
                || (netmonService.getType() == 2 && netmonService.getStatus() == NetmonStatus.ServiceStatus.ACCEPTED_BY_CUSTOMER)) {

            // update name, sett start date and change status to RUNNING
            netmonService.setName(renameServiceRequest.getName());
            netmonService.setStartDate(renameServiceRequest.getStartDate());
            netmonService.setStatus(NetmonStatus.ServiceStatus.RUNNING);
        } else{
            logService.createLog("RENAME_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", renameServiceRequest.toString(), "Bad request.");
            throw new BadRequestException("BadRequest");
        }

        logService.createLog("RENAME_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", renameServiceRequest.toString(), "");
        return netmonServiceRepository.save(netmonService);
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
