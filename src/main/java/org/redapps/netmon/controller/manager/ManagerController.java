package org.redapps.netmon.controller.manager;

import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.Company;
import org.redapps.netmon.model.PlanPrice;
import org.redapps.netmon.model.PlanPriceId;
import org.redapps.netmon.model.ResourcePrice;
import org.redapps.netmon.model.VpsPlan;
import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.*;
import org.redapps.netmon.security.CurrentUser;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.service.*;
import org.redapps.netmon.util.NetmonStatus;
import org.redapps.netmon.util.NetmonTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.redapps.netmon.exception.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import org.redapps.netmon.util.AppConstants;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final VpsPlanService vpsPlanService;
    private final NetmonServiceRepository netmonServiceRepository;
    private final VpsPlanRepository vpsPlanRepository;
    private final LogService logService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final NSService nsService;
    private final ResourcePriceRepository resourcePriceRepository;
    private final PlanPriceRepository planPriceRepository;
    private final PlanPriceService planPriceService;
    private final ResourcePriceService resourcePriceService;
    private final BillingService billingService;

    @Autowired
        public ManagerController(LogService logService, NetmonServiceRepository netmonServiceRepository,
                        VpsPlanRepository planRepository, VpsPlanService vpsPlanService, UserRepository userRepository,
                        CompanyRepository companyRepository, NSService nsService,
                        ResourcePriceRepository resourcePriceRepository, ResourcePriceService resourcePriceService,
                        PlanPriceRepository planPriceRepository, PlanPriceService planPriceService
                        , BillingService billingService
        ) {
        this.logService = logService;
        this.netmonServiceRepository = netmonServiceRepository;
        this.vpsPlanRepository = planRepository;
        this.vpsPlanService = vpsPlanService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.nsService = nsService;
        this.resourcePriceRepository = resourcePriceRepository;
        this.resourcePriceService = resourcePriceService;
        this.planPriceRepository = planPriceRepository;
        this.planPriceService = planPriceService;
        this.billingService = billingService;
    }

    /**
     * getting list of all urls for the manager role
     * @return an array of urls
     */
    @GetMapping("/urls")
    @PreAuthorize("hasRole('MANAGER')")
    public String[] getManagerUrls() {

        return new String[]{"GET: /api/manager/urls",
                "GET: /api/manager/customers/all",
                "GET: /api/manager/customers/{customerId}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/ips/all",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/ips/{ipId}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/ip/{ipId}/usage",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/ports/all",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/ports/{portId}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/devices/all",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/tickets/all",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/tickets/{ticketId}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/billings/all",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/billings/{billingId}",
                "PUT: /api/manager/customers/{customerId}/colocations/{colocationId}/billings/{billingId}/update",
                "GET: /api/manager/customers/{customerId}/colocations/all",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/billing/calculate}",
                "GET: /api/manager/customers/{customerId}/colocations/{colocationId}/billings/lastPaid",
                "PUT: /api/manager/customers/{customerId}/colocations/{colocationId}/confirm",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/ips/all",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/ips/{ipId}",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/ip/{ipId}/usage",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/ports/all",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/ports/{portId}",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/tickets/all",
                "GET: /api/manager/customers/{customerId}/vps/{companyId}/tickets/{ticketId}",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/billings/all",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/billings/{billingId}",
                "PUT: /api/manager/customers/{customerId}/vps/{vpsId}/billings/{billingId}/update",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/billings/lastPaid",
                "GET: /api/manager/customers/{customerId}/vps/all",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}/billing/calculate}",
                "GET: /api/manager/colocations/all",
                "GET: /api/manager/colocations/{colocationId}",
                "GET: /api/manager/vps/all",
                "GET: /api/manager/vps/{vpsId}",
                "GET: /api/manager/customers/{customerId}/vps/{vpsId}",
                "PUT: /api/manager/customers/{customerId}/vps/{vpsId}/confirm",
                "POST: /api/manager/plans/new",
                "GET: /api/manager/plans/{planId}",
                "GET: /api/manager/plans/all",
                "PUT: /api/manager/plans/{planId}/active",
                "PUT: /api/manager/plans/{planId}/inactive"};
    }

    /**
     * geting a plan info by id
     * @param currentUser the user id who currently logged in
     * @param planId the unique plan number
     * @return plan response
     */
    @GetMapping("/plans/{planId}")
    @PreAuthorize("hasRole('MANAGER')")
    public VpsPlanResponse getPlanById(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable Long planId) {


        return vpsPlanService.getPlanById(planId, currentUser);
    }

    /**
     * creating a new plan
     * @param vpsPlanRequest the plan information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/plans/new")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createVpsPlan(@Valid @RequestBody VpsPlanRequest vpsPlanRequest,
                                           @CurrentUser UserPrincipal currentUser) {

        if (vpsPlanRepository.existsByName(vpsPlanRequest.getName())) {
            logService.createLog("CREATE_PLAN", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                    vpsPlanRequest.toString(), "This plan is already exists.");
            return new ResponseEntity<>(new ApiResponse(false, "This plan is already exists."),
                    HttpStatus.BAD_REQUEST);
        }

        VpsPlan vpsPlan = vpsPlanService.create(vpsPlanRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{planId}")
                .buildAndExpand(vpsPlan.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The plan created successfully."));
    }

    /**
     * activating a plan
     * @param planId the unique plan number
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PutMapping("/plans/{planId}/active")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> activeVpsPlan(@PathVariable Long planId,
                                           @CurrentUser UserPrincipal currentUser) {

        vpsPlanService.active(planId, currentUser, true);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{planId}")
                .buildAndExpand(planId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The plan active successfully."));
    }

    /**
     * inactivating a plan
     * @param planId the unique plan number
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PutMapping("/plans/{planId}/inactive")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> inactiveVpsPlan(@PathVariable Long planId,
                                             @CurrentUser UserPrincipal currentUser) {


        vpsPlanService.active(planId, currentUser, false);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{planId}")
                .buildAndExpand(planId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The plan inactive successfully."));
    }

    /**
     * setting the price and discount percent for a service by manager
     * @param serviceConfirmRequest the service information object to confirm
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param createDate the service create
     * @return OK response or report error
     */
    @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/confirm")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> confirmService(@Valid @RequestBody ServiceConfirmRequest serviceConfirmRequest,
                                            @CurrentUser UserPrincipal currentUser,
                                            @PathVariable Long customerId,
                                            @PathVariable String serviceType,
                                            @PathVariable Long serviceId,
                                            @PathVariable String createDate) {
        if (!userRepository.existsById(customerId)) {
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", serviceConfirmRequest.toString(),
                    "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if(!companyOptional.isPresent()){
            logService.createLog("UPDATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", serviceConfirmRequest.toString(),
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + "]", serviceConfirmRequest.toString(),
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        nsService.managerConfirmService(currentUser, serviceId, LocalDate.parse(createDate), serviceConfirmRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{serviceId}/{createDate}")
                .buildAndExpand(serviceId, createDate).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The service updated successfully."));
    }

    /**
     * changing the billing status to VOID
     * @param serviceBillingRequest the billing information object
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param createDate the service create date
     * @param billingId the unique billing number
     * @return OK response or report error
     */
    @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/{createDate}/billings/{billingId}/void")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> voidServiceBillingById(@Valid @RequestBody ServiceBillingRequest serviceBillingRequest,
                                                      @CurrentUser UserPrincipal currentUser,
                                                      @PathVariable Long customerId,
                                                      @PathVariable String serviceType,
                                                      @PathVariable Long serviceId,
                                                      @PathVariable String createDate,
                                                      @PathVariable Long billingId) {

        if (!userRepository.existsById(customerId)) {
            logService.createLog("VOID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate + ",billingId=" + billingId + "]",
                    serviceBillingRequest.toString(), "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if(!companyOptional.isPresent()){
            logService.createLog("VOID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate+ ",billingId=" + billingId + "]",
                    serviceBillingRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCreateDateAndCompanyIdAndServiceType(serviceId, LocalDate.parse(createDate), companyId, srvType)) {
            logService.createLog("VOID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",createDate=" + createDate+ ",billingId=" + billingId + "]",
                    serviceBillingRequest.toString(), "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        billingService.voidBilling(currentUser, billingId, serviceBillingRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{billingId}")
                .buildAndExpand(billingId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The billing updated successfully."));
    }

    /**
     * geting a resourcePrice info by id
     * @param currentUser the user id who currently logged in
     * @param resourcePriceId the unique resourcePrice number
     * @return resourcePrice response
     */
    @GetMapping("/resourceprice/{resourcePriceId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResourcePriceResponse getResourcePriceById(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable Long resourcePriceId) {


        return resourcePriceService.getResourcePriceById(resourcePriceId, currentUser);
    }



    /**
     * creating a new resource price
     * @param resourcePriceRequest the resourcePrice information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/resourceprice/new")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createrResourePrice(@Valid @RequestBody ResourcePriceRequest resourcePriceRequest,
                                           @CurrentUser UserPrincipal currentUser) {

        if (resourcePriceRepository.existsByDate(resourcePriceRequest.getDate())) {
            logService.createLog("CREATE_RESOURCE_PRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                    resourcePriceRequest.toString(), "This resource price already exists.");
            return new ResponseEntity<>(new ApiResponse(false, "This resource price already exists."),
                    HttpStatus.BAD_REQUEST);
        }

        ResourcePrice resourcePrice = resourcePriceService.create(resourcePriceRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{resourcePriceId}")
                .buildAndExpand(resourcePrice.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The resource Price created successfully."));
    }
       
        /**
         * creating a new plan price
         * 
         * @param planPriceRequest the planPrice information object
         * @param currentUser      the user id who currently logged in
         * @param planId           the plan id
         * @return OK response or report error
         */
        @PostMapping("/plan/{planId}/price/new")
        @PreAuthorize("hasRole('MANAGER')")
        public ResponseEntity<?> createPlanPrice(@Valid @RequestBody PlanPriceRequest planPriceRequest,
                        @PathVariable Long planId, @CurrentUser UserPrincipal currentUser) {

                if (planPriceRepository.existsById(new PlanPriceId(planId, planPriceRequest.getDate()))) {
                        logService.createLog("CREATE_PLAN_PRICE", currentUser.getUsername(),
                                        NetmonStatus.LOG_STATUS.FAILED, "", planPriceRequest.toString(),
                                        "This plan price already exists.");
                        return new ResponseEntity<>(new ApiResponse(false, "This plan price already exists."),
                                        HttpStatus.BAD_REQUEST);
                }

                VpsPlan plan = vpsPlanRepository.findById(planId).get();
                PlanPrice planPrice = planPriceService.create(plan, planPriceRequest, currentUser);

                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{planPriceId}")
                                .buildAndExpand(planPrice.getPlanId()).toUri();

                return ResponseEntity.created(location)
                                .body(new ApiResponse(true, "The plan Price created successfully."));
        }

        /**
         * geting a resourcePrices info by plan id
         * 
         * @param currentUser     the user id who currently logged in
         * @param resourcePriceId the unique resourcePrice number
         * @param page            the page number of the response (default value is 0)
         * @param size            the page size of each response (default value is 30)
         * @return resourcePrice response
         */
        @GetMapping("/plan/{planId}/planprices")
        @PreAuthorize("hasRole('MANAGER')")
        public PagedResponse<PlanPriceResponse> getPlanPriceByPlanId(@CurrentUser UserPrincipal currentUser,
                        @PathVariable Long planId,
                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

                // find plan by id
                Optional<VpsPlan> vpsPlanOptional = vpsPlanRepository.findById(planId);
                if (!vpsPlanOptional.isPresent()) {
                        logService.createLog("(IN)Get_VPS_PLAN_PLAN_PRICE_ALL", currentUser.getUsername(),
                                        NetmonStatus.LOG_STATUS.FAILED, "[planId=" + planId + "]", "",
                                        "The vps plan does not exists.");
                        throw new ResourceNotFoundException("VpsPlan", "vpsPlanId", planId);
                }
                logService.createLog("(IN)Get_VPS_PLAN_PLAN_PRICE_ALL", currentUser.getUsername(),
                                NetmonStatus.LOG_STATUS.SUCCESS, "[planId=" + planId + "]", "", "");

                VpsPlan vpsPlan = vpsPlanOptional.get();
                return planPriceService.getPlanPriceVpsByVpsPlan(vpsPlan, currentUser, page, size);
        }


}
