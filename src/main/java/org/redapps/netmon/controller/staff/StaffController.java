package org.redapps.netmon.controller.staff;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.*;
import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.*;
import org.redapps.netmon.security.CurrentUser;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.service.*;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.redapps.netmon.util.NetmonTypes;
import org.redapps.netmon.util.ProcessIpUsageFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.redapps.netmon.exception.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Vector;

@RestController
@RequestMapping(value = { "/api/office", "/api/technical", "/api/manager" })
public class StaffController {

    private final NSService nsService;
    private final UserManagementService userManagementService;
    private final IPService ipService;
    private final PortService portService;
    private final TicketService ticketService;
    private final VpsPlanService vpsPlanService;
    // private final DeviceService colocationDeviceService;
    private final NetmonServiceRepository netmonServiceRepository;
    private final ServiceIPRepository serviceIPRepository;
    private final LogService logService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ResourcePriceService resourcePriceService;
    // private final BillingService billingService;
    // private final ServiceBillingRepository serviceBillingRepository;

    @Autowired
    public StaffController(TicketService ticketService, PortService portService,
            NetmonServiceRepository netmonServiceRepository, LogService logService,
            ServiceIPRepository serviceIPRepository, NSService nsService,
            // DeviceService colocationDeviceService,
            VpsPlanService vpsPlanService, UserManagementService userManagementService, IPService ipService,
            UserRepository userRepository, CompanyRepository companyRepository‍, ResourcePriceService resourcePriceService
    // BillingService billingService,
    // ServiceBillingRepository serviceBillingRepository
    ) {
        this.ticketService = ticketService;
        this.portService = portService;
        this.netmonServiceRepository = netmonServiceRepository;
        this.logService = logService;
        this.serviceIPRepository = serviceIPRepository;
        this.nsService = nsService;
        // this.colocationDeviceService = colocationDeviceService;
        this.vpsPlanService = vpsPlanService;
        this.userManagementService = userManagementService;
        this.ipService = ipService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository‍;
        this.resourcePriceService = resourcePriceService;

        // this.billingService = billingService;
        // this.serviceBillingRepository = serviceBillingRepository;
    }

    /**
     * getting list of all customer
     * 
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return user responses page by page
     */
    @GetMapping("/customers/all")
    // this method exists for 3 roles (office, technical and manager)
    @PreAuthorize("hasRole('OFFICE') OR hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public PagedResponse<UserResponse> getAllCustomers(@CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return userManagementService.getAllCustomers(currentUser, page, size);
    }

    /**
     * getting customer info by id
     * 
     * @param currentUser the user id who currently logged in the user id who
     *                    currently logged in
     * @param customerId  the customer unique number who requested the service
     * @return user response
     */
    @GetMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('OFFICE') OR hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public UserResponse getCurrentUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long customerId) {

        return userManagementService.getCustomerById(customerId, currentUser);
    }

    // /**
    // * getting colocation info by id
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param colocationId the unique colocation number
    // * @return collocation response
    // */
    // @GetMapping("/customers/{customerId}/colocations/{colocationId}")
    // @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    // public ColocationResponse getColocationById(@CurrentUser UserPrincipal
    // currentUser,
    // @PathVariable Long customerId,
    // @PathVariable Long colocationId) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "",
    // "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "",
    // "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // if
    // (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(colocationId,
    // companyId,
    // NetmonTypes.SERVICE_TYPES.COLOCATION)) {
    // logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "",
    // "This service does not belong to the company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // return nsService.getColocationById(colocationId, currentUser);
    // }

    // /**
    // * getting list of all colocations
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param page the page number of the response (default value is 0)
    // * @param size the page size of each response (default value is 30)
    // * @return colocation responses page by page
    // */
    // @GetMapping("/customers/{customerId}/colocations/all")
    // @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    // public PagedResponse<ColocationResponse> getAllColocations(@CurrentUser
    // UserPrincipal currentUser,
    // @PathVariable Long customerId,
    // @RequestParam(value = "page",
    // defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
    // @RequestParam(value = "size",
    // defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_ALL_COLOCATIONS", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + "]", "", "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_ALL_COLOCATIONS", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + "]", "", "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // return nsService.getCompanyColocations(currentUser, companyId, page, size);
    // }

    /**
     * getting list of all vps
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return vps responses page by page
     */
    @GetMapping("/customers/{customerId}/vps/all")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public PagedResponse<VpsResponse> getAllVPSes(@CurrentUser UserPrincipal currentUser, @PathVariable Long customerId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_ALL_VPSES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + "]", "", "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_ALL_VPSES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        return nsService.getCustomerVPSes(currentUser, companyId, page, size);
    }

    /**
     * getting vps info by id
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param vpsId       the unique vps number
     * @param createDate the service create date
     * @return vps response
     */
    @GetMapping("/customers/{customerId}/vps/{vpsId}/{createDate}")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public VpsResponse getVPSById(@CurrentUser UserPrincipal currentUser, @PathVariable Long customerId,
            @PathVariable Long vpsId, @PathVariable String createDate) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",vpsId=" + vpsId + ",createDate=" + createDate + "]", "", "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",vpsId=" + vpsId + ",createDate=" + createDate + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(vpsId, LocalDate.parse(createDate), companyId,
                NetmonTypes.SERVICE_TYPES.VPS)) {
            logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",vpsId=" + vpsId + ",createDate=" + createDate + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return nsService.getVPSById(vpsId, LocalDate.parse(createDate), currentUser);
    }

    /**
     * getting list of all plans.
     * 
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return plan responses page by page
     */
    @GetMapping("/plans/all")
    @PreAuthorize("hasRole('OFFICE') OR hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public PagedResponse<VpsPlanResponse> getVpsPlans(@CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return vpsPlanService.getPlans(currentUser, page, size);
    }

    /**
     * getting list of service ips by service id
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return ip responses page by page
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/ips/all")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public PagedResponse<ServiceIPResponse> getServiceIPs(@CurrentUser UserPrincipal currentUser,
            @PathVariable Long customerId, @PathVariable String serviceType, @PathVariable Long serviceId,
            @PathVariable String createDate,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_SERVICE_IPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "", "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_SERVICE_IPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_SERVICE_IPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ipService.getServiceIps(serviceId, LocalDate.parse(createDate), currentUser, page, size);
    }

    /**
     * getting info of ip by id
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param IPId        the unique ip number
     * @return ip response
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/ips/{IPId}")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public ServiceIPResponse getServiceIPById(@CurrentUser UserPrincipal currentUser, @PathVariable Long customerId,
            @PathVariable String serviceType, @PathVariable Long serviceId, @PathVariable String createDate, @PathVariable Long IPId) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + IPId + "]", "",
                    "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + IPId + "]", "",
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + IPId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ipService.getServiceIpById(IPId, currentUser);
    }

    /**
     * getting value of ip usage
     * 
     * @param startDate   the start date to calculate ip usage
     * @param endDate     the end date to calculate ip usage
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param ipId        the unique ip number
     * @param currentUser the user id who currently logged in
     * @return ip usage response
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/ip/{ipId}/usage")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public IpUsageResponse getServiceIpUsage(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PathVariable Long customerId, @PathVariable String serviceType, @PathVariable Long serviceId,
            @PathVariable String createDate,
            @PathVariable Long ipId, @CurrentUser UserPrincipal currentUser) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + ipId + ",startDate="
                            + startDate + ",endDate=" + endDate + "]",
                    "", "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + ipId + ",startDate="
                            + startDate + ",endDate=" + endDate + "]",
                    "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + ipId + ",startDate="
                            + startDate + ",endDate=" + endDate + "]",
                    "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        if (!serviceIPRepository.existsByIdAndNetmonServiceIdAndNetmonServiceCreateDate(ipId, serviceId, LocalDate.parse(createDate))) {
            logService
                    .createLog("GET_IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                            "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + ipId + ",startDate="
                                    + startDate + ",endDate=" + endDate + "]",
                            "", "This ip does not belong to the service.");
            throw new AccessDeniedException("This ip does not belong to the service.");
        }

        if (endDate.isBefore(startDate)) {
            logService.createLog("GET_IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + ipId + ",startDate="
                            + startDate + ",endDate=" + endDate + "]",
                    "", "The endDate is smaller than the startDate.");
            throw new BadRequestException("The endDate is smaller than the startDate.");
        }

        IP ip = serviceIPRepository.getOne(ipId);

        // upload files
        Vector<IpUsage> ipUpUsages = ProcessIpUsageFiles.processFiles(startDate, endDate, ip.getIp(), "up.csv");

        // download files
        Vector<IpUsage> ipDownUsages = ProcessIpUsageFiles.processFiles(startDate, endDate, ip.getIp(), "dl.csv");

        logService.createLog("GET_IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ipId=" + ipId + ",startDate=" + startDate
                        + ",endDate=" + endDate + "]",
                "", "");

        return new IpUsageResponse(startDate, endDate, ip.getIp(), ipUpUsages, ipDownUsages);
    }

    // /**
    // * geting list of service devices by service id
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param colocationId the unique colocation number
    // * @param page the page number of the response (default value is 0)
    // * @param size the page size of each response (default value is 30)
    // * @return devices page by page
    // */
    // @GetMapping("/customers/{customerId}/colocations/{colocationId}/devices/all")
    // @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    // public PagedResponse<ColocationDeviceResponse>
    // getColocationDevices(@CurrentUser UserPrincipal currentUser,
    // @PathVariable Long customerId,
    // @PathVariable Long colocationId,
    // @RequestParam(value = "page",
    // defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
    // @RequestParam(value = "size",
    // defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_ALL_COLOCATION_DEVICES", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "",
    // "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_ALL_COLOCATION_DEVICES", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "", "The
    // customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // if
    // (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(colocationId,
    // companyId,
    // NetmonTypes.SERVICE_TYPES.COLOCATION)) {
    // logService.createLog("GET_ALL_COLOCATION_DEVICES", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "",
    // "This service does not belong to the company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }
    // return colocationDeviceService.getColocationDevices(currentUser,
    // colocationId, page, size);
    // }

    // /**
    // * getting info of device by id
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param colocationId the unique colocation number
    // * @param deviceId the unique device number
    // * @return device
    // */
    // @GetMapping("/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}")
    // @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    // public ColocationDeviceResponse getColocationDeviceById(@CurrentUser
    // UserPrincipal currentUser,
    // @PathVariable Long customerId,
    // @PathVariable Long colocationId,
    // @PathVariable Long deviceId) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + ",deviceId="
    // + deviceId + "]", "",
    // "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + "]", "", "The
    // customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // if
    // (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(colocationId,
    // companyId,
    // NetmonTypes.SERVICE_TYPES.COLOCATION)) {
    // logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",colocationId=" + colocationId + ",deviceId="
    // + deviceId + "]", "",
    // "This service does not belong to the company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // return colocationDeviceService.getColocationDeviceById(deviceId,
    // currentUser);
    // }

    /**
     * getting list of service ports
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return port responses page by page
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/ports/all")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public PagedResponse<ServicePortResponse> getServicePorts(@CurrentUser UserPrincipal currentUser,
            @PathVariable Long customerId, @PathVariable String serviceType, @PathVariable Long serviceId,
            @PathVariable String createDate,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_ALL_SERVICE_PORTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "", "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_ALL_SERVICE_PORTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_ALL_SERVICE_PORTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return portService.getServicePorts(currentUser, serviceId, LocalDate.parse(createDate), page, size);
    }

    /**
     * getting info of an ip by id
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param portId      the unique port number
     * @return post response
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/ports/{PortId}")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public ServicePortResponse getServicePortById(@CurrentUser UserPrincipal currentUser, @PathVariable Long customerId,
            @PathVariable String serviceType, @PathVariable Long serviceId, @PathVariable String createDate, @PathVariable Long portId) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_SERVICE_PORT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",portId=" + portId + "]", "",
                    "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_SERVICE_PORT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",portId=" + portId + "]", "",
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_SERVICE_PORT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",portId=" + portId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return portService.getServicePortById(portId, currentUser);
    }

    /**
     * getting service ticket list by service id
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return ticket responses page by page
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/tickets/all")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public PagedResponse<ServiceTicketResponse> getServiceTickets(@CurrentUser UserPrincipal currentUser,
            @PathVariable Long customerId, @PathVariable String serviceType, @PathVariable Long serviceId,
            @PathVariable String createDate,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_ALL_SERVICE_TICKETS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "", "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_ALL_SERVICE_TICKETS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + "],serviceId=" + serviceId + ",createDate=" + createDate + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_ALL_SERVICE_TICKETS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ticketService.getServiceTickets(currentUser, serviceId, LocalDate.parse(createDate), page, size);
    }

    /**
     * getting info of a ticket by id
     * 
     * @param currentUser the user id who currently logged in
     * @param customerId  the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId   the unique service number
     * @param createDate the service create date
     * @param ticketId    the unique ticket number
     * @return ticket response
     */
    @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/tickets/{ticketId}")
    @PreAuthorize("hasRole('TECHNICAL') OR hasRole('MANAGER')")
    public ServiceTicketResponse getServiceTicketById(@CurrentUser UserPrincipal currentUser,
            @PathVariable Long customerId, @PathVariable String serviceType, @PathVariable Long serviceId,
            @PathVariable String createDate, @PathVariable Long ticketId) {
        if (!userRepository.existsById(customerId)) {
            logService.createLog("GET_SERVICE_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ticketId=" + ticketId + "]", "",
                    "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if (!companyOptional.isPresent()) {
            logService.createLog("GET_SERVICE_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ticketId=" + ticketId + "]", "",
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if (serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("GET_SERVICE_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",ticketId=" + ticketId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ticketService.getServiceTicketById(ticketId, currentUser);
    }

    // /**
    // * getting service billings list by service id
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param serviceType vps / collocation
    // * @param serviceId the unique service number
    // * @param createDate the service create date
    // * @param page the page number of the response (default value is 0)
    // * @param size the page size of each response (default value is 30)
    // * @return billing responses page by page
    // */
    // @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/billings/all")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER')")
    // public PagedResponse<ServiceBillingResponse> getServiceBillings(@CurrentUser
    // UserPrincipal currentUser,
    // @PathVariable Long customerId,
    // @PathVariable String serviceType,
    // @PathVariable Long serviceId,
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createDate,
    // @RequestParam(value = "page", defaultValue =
    // AppConstants.DEFAULT_PAGE_NUMBER) int page,
    // @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE)
    // int size) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + "]", "",
    // "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[serviceId=" + serviceId + "]", "", "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
    // if(serviceType.compareToIgnoreCase("vps") == 0)
    // srvType = NetmonTypes.SERVICE_TYPES.VPS;

    // if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId,
    // companyId, srvType)) {
    // logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + "]", "",
    // "This service does not belong to the company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // return billingService.getServiceBillings(currentUser, serviceId, page, size);
    // }

    // /**
    // * getting info of a billing by id
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param serviceType vps / collocation
    // * @param serviceId the unique service number
    // * @param createDate the service create date
    // * @param billingId the unique billing number
    // * @return billing response
    // */
    // @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/billings/{billingId}")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER')")
    // public ServiceBillingResponse getServiceBillingById(@CurrentUser
    // UserPrincipal currentUser,
    // @PathVariable Long customerId,
    // @PathVariable String serviceType,
    // @PathVariable Long serviceId,
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createDate,
    // @PathVariable Long billingId) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",billingId=" +
    // billingId + "]", "",
    // "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",billingId=" +
    // billingId + "]", "",
    // "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
    // if(serviceType.compareToIgnoreCase("vps") == 0)
    // srvType = NetmonTypes.SERVICE_TYPES.VPS;

    // if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId,
    // companyId, srvType)) {
    // logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",billingId=" +
    // billingId + "]", "",
    // "This service does not belong to the company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // return billingService.getServiceBillingById(billingId, currentUser);
    // }

    /**
     * getting all vpses
     * 
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return vps responses page by page
     */
    @GetMapping("/vps/all")
    @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER') OR hasRole('TECHNICAL')")
    public PagedResponse<VpsResponse> getVPSes(@CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return nsService.getAllVPSes(currentUser, page, size);
    }

    // /**
    // * getting all colocations
    // * @param currentUser the user id who currently logged in
    // * @param page the page number of the response (default value is 0)
    // * @param size the page size of each response (default value is 30)
    // * @return colocation responses page by page
    // */
    // @GetMapping("/colocations/all")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER') OR
    // hasRole('TECHNICAL')")
    // public PagedResponse<ColocationResponse> getColocations(@CurrentUser
    // UserPrincipal currentUser,
    // @RequestParam(value = "page",
    // defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
    // @RequestParam(value = "size",
    // defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    // return nsService.getAllColocations(currentUser, page, size);
    // }

    // /**
    // * getting info of a colocation by id
    // * @param currentUser the user id who currently logged in
    // * @param colocationId the unique colocation number
    // * @return colocation response
    // */
    // @GetMapping("/colocations/{colocationId}")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER') OR
    // hasRole('TECHNICAL')")
    // public ColocationResponse getColocationById(@CurrentUser UserPrincipal
    // currentUser,
    // @PathVariable Long colocationId) {

    // return nsService.getColocationById(colocationId, currentUser);
    // }

    /**
    * getting info of a vps by id
    * @param currentUser the user id who currently logged in
    * @param vpsId the unique vps number
    * @param createDate the service create date
    * @return vps response
    */
    @GetMapping("/vps/{vpsId}/{createDate}")
    @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER') OR hasRole('TECHNICAL')")
    public VpsResponse getVPSById(@CurrentUser UserPrincipal currentUser,
    @PathVariable Long vpsId, @PathVariable String createDate) {

    return nsService.getVPSById(vpsId, LocalDate.parse(createDate), currentUser);
    }

    // /**
    // * creating a billing for a service
    // * @param currentUser the user id who currently logged in
    // * @param startDate the start date to calculate a billing
    // * @param endDate the end date to calculate a billing
    // * @param description an explanation of the action
    // * @param customerId the customer unique number who requested the service
    // * @param serviceType vps / collocation
    // * @param serviceId the unique service number
    // * @return OK response or report error
    // */
    // @GetMapping("/customers/{customerId}/{serviceType}/{serviceId}/billing/calculate")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER')")
    // public ResponseEntity<?> calculateBilling(@CurrentUser UserPrincipal
    // currentUser,
    // @RequestParam("startDate")
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    // @RequestParam("endDate")
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
    // @RequestParam("description") String description,
    // @PathVariable Long customerId,
    // @PathVariable String serviceType,
    // @PathVariable Long serviceId) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("CALCULATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",startDate=" +
    // startDate +
    // ",endDate=" + endDate + "]", "", "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("CALCULATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",startDate=" +
    // startDate +
    // ",endDate=" + endDate + "]", "", "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
    // if(serviceType.compareToIgnoreCase("vps") == 0)
    // srvType = NetmonTypes.SERVICE_TYPES.VPS;

    // if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId,
    // companyId, srvType)) {
    // logService.createLog("CALCULATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",startDate=" +
    // startDate +
    // ",endDate=" + endDate + "]", "", "This service does not belong to the
    // company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // // checking start and end date
    // if (startDate.isAfter(endDate)) {
    // logService.createLog("CALCULATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",startDate=" +
    // startDate +
    // ",endDate=" + endDate + "]", "",
    // "The endDate should be greater then the startDate.");
    // throw new BadRequestException("The endDate should be greater then the
    // startDate.");
    // }

    // Billing billing = billingService.calculateBilling(currentUser, serviceId,
    // startDate, endDate,
    // description);

    // serviceBillingRepository.save(billing);

    // URI location = ServletUriComponentsBuilder
    // .fromCurrentRequest().path("/{billingId}")
    // .buildAndExpand(billing.getId()).toUri();

    // return ResponseEntity.created(location)
    // .body(new ApiResponse(true, "The billing created successfully."));
    // }

    // /**
    // * updating the status (PAID), description and payment date for a billing by
    // id.
    // * @param serviceBillingRequest the billing information object
    // * @param currentUser the user id who currently logged in
    // * @param customerId the customer unique number who requested the service
    // * @param serviceType vps / collocation
    // * @param serviceId the unique service number
    // * @param createDate the service create date
    // * @param billingId the unique billing number
    // * @return OK response or report error
    // */
    // @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/billings/{billingId}/update")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER')")
    // public ResponseEntity<?> updateServiceBillingById(@Valid @RequestBody
    // ServiceBillingRequest serviceBillingRequest,
    // @CurrentUser UserPrincipal currentUser,
    // @PathVariable Long customerId,
    // @PathVariable String serviceType,
    // @PathVariable Long serviceId,
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createDate,
    // @PathVariable Long billingId) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("UPDATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",billingId=" +
    // billingId + "]",
    // serviceBillingRequest.toString(), "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("UPDATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",billingId=" +
    // billingId + "]",
    // serviceBillingRequest.toString(), "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
    // if(serviceType.compareToIgnoreCase("vps") == 0)
    // srvType = NetmonTypes.SERVICE_TYPES.VPS;

    // if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId,
    // companyId, srvType)) {
    // logService.createLog("UPDATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + ",billingId=" +
    // billingId + "]",
    // serviceBillingRequest.toString(), "This service does not belong to the
    // company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // billingService.update(currentUser, billingId, serviceBillingRequest);

    // URI location = ServletUriComponentsBuilder
    // .fromCurrentRequest().path("/{billingId}")
    // .buildAndExpand(billingId).toUri();

    // return ResponseEntity.created(location)
    // .body(new ApiResponse(true, "The billing updated successfully."));
    // }

    // /**
    // * getting info of last paid billing for a service by service id
    // * @param currentUser the user id who currently logged in
    // * @param serviceType vps / collocation
    // * @param customerId the customer unique number who requested the service
    // * @param serviceId the unique service number
    // * @return billing
    // */
    // @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/billings/lastPaid")
    // @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER')")
    // public Billing getLastPaidBilling(@CurrentUser UserPrincipal currentUser,
    // @PathVariable String serviceType,
    // @PathVariable Long customerId,
    // @PathVariable Long serviceId) {

    // if (!userRepository.existsById(customerId)) {
    // logService.createLog("GET_LAST_PAID_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + "]",
    // "", "The customer does not exists.");
    // throw new ResourceNotFoundException("Customer", "customerId", customerId);
    // }

    // Optional<Company> companyOptional =
    // companyRepository.findByUserId(customerId);
    // if(!companyOptional.isPresent()){
    // logService.createLog("GET_LAST_PAID_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + "]",
    // "", "The customer has no company.");
    // throw new ResourceNotFoundException("Company", "userId", customerId);
    // }
    // Long companyId = companyOptional.get().getId();

    // NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
    // if(serviceType.compareToIgnoreCase("vps") == 0)
    // srvType = NetmonTypes.SERVICE_TYPES.VPS;

    // if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId,
    // companyId, srvType)) {
    // logService.createLog("UPDATE_BILLING", currentUser.getUsername(),
    // NetmonStatus.LOG_STATUS.FAILED,
    // "[customerId=" + customerId + ",serviceId=" + serviceId + "]",
    // "", "This service does not belong to the company.");
    // throw new AccessDeniedException("This service does not belong to the
    // company.");
    // }

    // return billingService.getLastPaidBilling(serviceId);
    // }


    /**
     * getting list of all resourcePrices.
     * 
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return resource price responses page by page
     */
    @GetMapping("/resourceprice/all")
    @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER') OR hasRole('CUSTOMER') ")
    public PagedResponse<ResourcePriceResponse> getResourcePrice(@CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return resourcePriceService.getResourcePrice(currentUser, page, size);
    }

     /**
     * getting list of all resourcePrices.
     * 
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return resource price responses page by page
     */
    @GetMapping("/resourceprice/last")
    @PreAuthorize("hasRole('OFFICE') OR hasRole('MANAGER') OR hasRole('CUSTOMER') ")
    public ResourcePriceResponse getResourcePrices(@CurrentUser UserPrincipal currentUser) {
        return resourcePriceService.getResourcePriceLast(currentUser);
    }
}
