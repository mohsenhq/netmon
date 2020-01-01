package org.redapps.netmon.controller.customer;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.*;
import org.redapps.netmon.repository.*;
import org.redapps.netmon.security.CurrentUser;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.service.*;
import org.redapps.netmon.util.*;
import org.redapps.netmon.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.redapps.netmon.exception.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Vector;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final NSService nsService;
    private final DeviceService colocationDeviceService;
    private final IPService ipService;
    private final PortService portService;
    private final TechnicalPersonService technicalPersonService;
    private final TicketService ticketService;
    private final BillingService billingService;
    private final DocumentService documentService;
    private final CompanyService companyService;
    private final LogService logService;
    private final CompanyRepository companyRepository;
    private final TechnicalPersonRepository technicalPersonRepository;
    private final OsTypeRepository osTypeRepository;
    private final ServiceIPRepository serviceIPRepository;
    private final ServicePortRepository servicePortRepository;
    private final NetmonServiceRepository netmonServiceRepository;
    private final VpsPlanRepository vpsPlanRepository;
    private final OSTypeService osTypeService;
    private final ServiceBillingRepository serviceBillingRepository;
    private final VpsPlanService vpsPlanService;

    @Autowired
    public CustomerController(TechnicalPersonRepository technicalPersonRepository,
                              DeviceService colocationDeviceService, OSTypeService osTypeService,
                              TechnicalPersonService technicalPersonService,
                              OsTypeRepository osTypeRepository, DocumentService documentService,
                              PortService portService, ServiceIPRepository serviceIPRepository,
                              NSService nsService, NetmonServiceRepository netmonServiceRepository,
                              ServicePortRepository servicePortRepository, IPService ipService,
                              BillingService billingService, TicketService ticketService,
                              CompanyRepository companyRepository, CompanyService companyService,
                              LogService logService, VpsPlanRepository vpsPlanRepository,
                              ServiceBillingRepository serviceBillingRepository,
                              VpsPlanService vpsPlanService) {
        this.technicalPersonRepository = technicalPersonRepository;
        this.colocationDeviceService = colocationDeviceService;
        this.technicalPersonService = technicalPersonService;
        this.osTypeRepository = osTypeRepository;
        this.documentService = documentService;
        this.portService = portService;
        this.serviceIPRepository = serviceIPRepository;
        this.nsService = nsService;
        this.netmonServiceRepository = netmonServiceRepository;
        this.servicePortRepository = servicePortRepository;
        this.ipService = ipService;
        this.billingService = billingService;
        this.ticketService = ticketService;
        this.companyRepository = companyRepository;
        this.companyService = companyService;
        this.logService = logService;
        this.vpsPlanRepository = vpsPlanRepository;
        this.osTypeService = osTypeService;
        this.serviceBillingRepository = serviceBillingRepository;
        this.vpsPlanService = vpsPlanService;
    }

    /**
     * getting list of all urls for the customer role
     * @return an arrays of urls
     */
    @GetMapping("/urls")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String[] getCustomerUrls() {

        return new String[]{"GET: /api/customer/urls",
                "POST: /api/customer/companies/new",
                "GET: /api/customer/companies/all",
                "POST: /api/customer/documents/new",
                "GET: /api/customer/documents/all",
                "GET: /api/customer/documents/{documentId}",
                "POST: /api/customer/documents/{documentId}/upload",
                "POST: /api/customer/technicalPerson/new",
                "GET: /api/customer/technicalPerson/all",
                "GET: /api/customer/technicalPerson/{technicalPersonId}",
                "PUT: /api/customer/technicalPerson/{technicalPersonId}/update",
                "DELETE: /api/customer/technicalPerson/{technicalPersonId}/delete",
                "POST: /api/customer/colocations/{colocationId}/devices/new",
                "GET: /api/customer/colocations/{colocationId}/devices/{deviceId}",
                "GET: /api/customer/colocations/{colocationId}/devices/all",
                "GET: /api/customer/colocations/{colocationId}/ips/all",
                "GET: /api/customer/colocations/{colocationId}/ips/{ipId}",
                "GET: /api/customer/colocations/{colocationId}/ip/{ipId}/usage",
                "POST: /api/customer/colocations/{colocationId}/ports/new",
                "GET: /api/customer/colocations/{colocationId}/ports/all",
                "GET: /api/customer/colocations/{colocationId}/ports/{portId}",
                "POST: /api/customer/colocations/{colocationId}/tickets/new",
                "GET: /api/customer/colocations/{colocationId}/tickets/all",
                "GET: /api/customer/colocations/{colocationId}/tickets/{ticketId}",
                "GET: /api/customer/colocations/{colocationId}/billings/all",
                "GET: /api/customer/colocations/{colocationId}/billings/{billingId}",
                "GET: /api/customer/colocations/{colocationId}/billings/calculate",
                "PUT: /api/customer/colocations/{colocationId}/billings/{billingId}/updateReferenceId",
                "PUT: /api/customer/colocations/{colocationId}/billings/{billingId}/updateOrderId",
                "GET: /api/customer/colocations/{colocationId}/billings/{billingId}/lastPaid",
                "POST: /api/customer/colocations/new",
                "GET: /api/customer/colocations/all",
                "GET: /api/customer/colocations/{colocationId}",
                "PUT: /api/customer/colocations/{colocationId}/confirm",
                "GET: /api/customer/vps/{vpsId}/ips/all",
                "GET: /api/customer/vps/{vpsId}/ips/{ipId}",
                "GET: /api/customer/vps/{vpsId}/ip/{ipId}/usage",
                "POST: /api/customer/vps/{vpsId}/ports/new",
                "GET: /api/customer/vps/{vpsId}/ports/all",
                "GET: /api/customer/vps/{vpsId}/ports/{portId}",
                "POST: /api/customer/vps/{vpsId}/tickets/new",
                "GET: /api/customer/vps/{vpsId}/tickets/all",
                "GET: /api/customer/vps/{vpsId}/tickets/{ticketId}",
                "GET /api/customer/vps/{vpsId}/billings/{billingId}",
                "GET /api/customer/vps/{vpsId}/billings/all",
                "GET: /api/customer/vps/{vpsId}/billings/calculate",
                "PUT: /api/customer/vps/{vpsId}/billings/{billingId}/updateReferenceId",
                "PUT: /api/customer/vps/{vpsId}/billings/{billingId}/updateOrderId",
                "GET: /api/customer/vps/{vpsId}/billings/{billingId}/lastPaid",
                "POST: /api/customer/vps/new",
                "GET: /api/customer/vps/all",
                "GET: /api/customer/vps/{vpsId}",
                "PUT: /api/customer/vps/{vpsId}/confirm",
                "GET: /api/customer/plans/all",
                "GET: /api/customer/osType/all"};
    }

    /**
     * creating a new company
     * @param companyRequest the company information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/companies/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequest companyRequest,
                                           @CurrentUser UserPrincipal currentUser) {

        if (companyRepository.existsByName(companyRequest.getName())) {
            logService.createLog("CREATE_COMPANY", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED, "", companyRequest.toString(),
                    "The company is already exist.");
            return new ResponseEntity<>(new ApiResponse(false, "The company is already exist."),
                    HttpStatus.BAD_REQUEST);
        }

        if (companyRepository.existsByUserId(currentUser.getId())) {
            logService.createLog("CREATE_COMPANY", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED, "", companyRequest.toString(),
                    "The user could not has more than one company.");
            return new ResponseEntity<>(new ApiResponse(false, "The user could not has more than one company."),
                    HttpStatus.BAD_REQUEST);
        }

        Company company = companyService.createCompany(companyRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{companyId}")
                .buildAndExpand(company.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The company created successfully."));
    }

    /**
     * getting list of all user companies
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return company responses page by page
     */
    @GetMapping("/companies/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<CompanyResponse> getCompanies(@CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page",
                                                               defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size",
                                                               defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return companyService.getAllCustomerCompanies(currentUser, page, size);
    }

//    @GetMapping("/companies/{companyId}")
//    @PreAuthorize("hasRole('CUSTOMER')")
//    public CompanyResponse getCompanyById(@CurrentUser UserPrincipal currentUser,
//                                          @PathVariable Long companyId) {
//
//        if (!companyRepository.existsByIdAndUserId(companyId, currentUser.getId())) {
//            logService.createLog("GET_COMPANY_INFO", currentUser.getUsername(), "FAILED",
//                    "[companyId=" + companyId + "]", "", "This company does not belong to the customer.");
//            throw new AccessDeniedException("This company does not belong to the customer.");
//        }
//
//        return companyService.getCompanyById(companyId, currentUser);
//    }

    /**
     * create a new document
     * @param documentRequest the document information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/documents/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createDocument(@Valid @RequestBody DocumentRequest documentRequest,
                                            @CurrentUser UserPrincipal currentUser) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CREATE_DOCUMENT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", documentRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }

        Long companyId = companyOptional.get().getId();
        Document document = documentService.createDocument(currentUser, documentRequest, companyId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{documentId}")
                .buildAndExpand(document.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The document created successfully."));
    }

    /**
     * getting list of user documents
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return document responses page by page
     */
    @GetMapping("/documents/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<DocumentResponse> getDocuments(@CurrentUser UserPrincipal currentUser,
                                                        @RequestParam(value = "page",
                                                                defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size",
                                                                defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_DOCUMENTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }

        Long companyId = companyOptional.get().getId();

        return documentService.getAllCustomerDocuments(currentUser, companyId, page, size);
    }

    /**
     * getting document info by id
     * @param currentUser the user id who currently logged in
     * @param documentId the unique document number
     * @return document response
     */
    @GetMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public DocumentResponse getDocumentById(@CurrentUser UserPrincipal currentUser,
                                            @PathVariable Long documentId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_DOCUMENT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[documentId=" + documentId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }

        Long companyId = companyOptional.get().getId();

        return documentService.getDocumentById(documentId, companyId, currentUser);
    }

    /**
     *
     * uploading a document file
     * @param currentUser the user id who currently logged in
     * @param documentId the unique document number
     * @param uploadfile the path of a file to upload
     * @return OK response or report error
     */
    @PostMapping("/documents/{documentId}/upload")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> uploadFile(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long documentId,
                                        @RequestParam("file") MultipartFile uploadfile) {

        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }

        try {

            String filename = UploadFiles.saveUploadedFiles(Collections.singletonList(uploadfile));
//            String filename = UploadFiles.saveUploadedFiles(Arrays.asList(uploadfile));

            //FIXME: set path and save
            documentService.setPath(currentUser, documentId, filename);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    /**
     * create a new colocation
     * @param colocationRequest the document information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/colocations/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createColocation(@Valid @RequestBody ColocationRequest colocationRequest,
                                               @CurrentUser UserPrincipal currentUser) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CREATE_COLOCATION", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED,
                    "", colocationRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }

        Optional<TechnicalPerson> technicalPersonOptional = technicalPersonRepository.findByIdAndUserId(
                colocationRequest.getTechnicalPersonId(), currentUser.getId());
        if (!technicalPersonOptional.isPresent()) {
            logService.createLog("CREATE_COLOCATION", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", colocationRequest.toString(),
                    "The technical person does not exists.");
            throw new BadRequestException("The technical person does not exists.");
        }

        Optional<OSType> osTypeOptional = osTypeRepository.findById(colocationRequest.getOsTypeId());
        if (!osTypeOptional.isPresent()) {
            logService.createLog("CREATE_COLOCATION", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED, "", colocationRequest.toString(),
                    "The os type does not exists.");
            throw new BadRequestException("The os type does not exists.");
        }

        NetmonService netmonService = nsService.createColocation(currentUser, colocationRequest,
                companyOptional.get(), technicalPersonOptional.get(), osTypeOptional.get());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{colocationId}")
                .buildAndExpand(netmonService.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The colocation created successfully."));
    }

    /**
     * getting list of all user colocations
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return colocation responses page by page
     */
    @GetMapping("/colocations/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<ColocationResponse> getCompanyColocations(@CurrentUser UserPrincipal currentUser,
                                                                     @RequestParam(value = "page",
                                                                             defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                     @RequestParam(value = "size",
                                                                             defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_COLOCATIONS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        return nsService.getCompanyColocations(currentUser, companyId, page, size);
    }

    /**
     * getting a colocation info by id
     * @param currentUser the user id who currently logged in
     * @param colocationId the unique colocation number
     * @return colocation response
     */
    @GetMapping("/colocations/{colocationId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ColocationResponse getColocationById(@CurrentUser UserPrincipal currentUser,
                                                  @PathVariable Long colocationId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(colocationId, companyId,
                NetmonTypes.SERVICE_TYPES.COLOCATION)) {
            logService.createLog("GET_COLOCATION_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return nsService.getColocationById(colocationId, currentUser);
    }

    /**
     * create a new device
     * @param colocationDeviceRequest the device information object
     * @param currentUser the user id who currently logged in
     * @param colocationId the unique colocation number
     * @return OK response or report error
     */
    @PostMapping("/colocations/{colocationId}/devices/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createColocationDevice(@Valid @RequestBody DeviceRequest colocationDeviceRequest,
                                                     @CurrentUser UserPrincipal currentUser,
                                                     @PathVariable Long colocationId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CREATE_COLOCATION_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", colocationDeviceRequest.toString(),
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(colocationId, companyId,
                NetmonTypes.SERVICE_TYPES.COLOCATION)) {
            logService.createLog("CREATE_COLOCATION_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", colocationDeviceRequest.toString(),
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        Device colocationDevice = colocationDeviceService.create(colocationDeviceRequest, currentUser,
                colocationId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{deviceId}")
                .buildAndExpand(colocationDevice.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The device created successfully."));
    }

    /**
     * getting all devices bu colocation id
     * @param currentUser the user id who currently logged in
     * @param colocationId the unique colocation number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return device responses page by page
     */
    @GetMapping("/colocations/{colocationId}/devices/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<ColocationDeviceResponse> getColocationDevices(@CurrentUser UserPrincipal currentUser,
                                                                          @PathVariable Long colocationId,
                                                                          @RequestParam(value = "page",
                                                                                  defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                          @RequestParam(value = "size",
                                                                                  defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_COLOCATION_DEVICES", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(colocationId, companyId,
                NetmonTypes.SERVICE_TYPES.COLOCATION)) {
            logService.createLog("GET_ALL_COLOCATION_DEVICES", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return colocationDeviceService.getColocationDevices(currentUser, colocationId, page, size);
    }

    /**
     * getting device info by id
     * @param currentUser the user id who currently logged in
     * @param colocationId the unique colocation number
     * @param deviceId the unique device number
     * @return device response
     */
    @GetMapping("/colocations/{colocationId}/devices/{deviceId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ColocationDeviceResponse getColocationDeviceById(@CurrentUser UserPrincipal currentUser,
                                                              @PathVariable Long colocationId,
                                                              @PathVariable Long deviceId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED, "[colocationId=" + colocationId +
                            ",deviceId=" + deviceId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(colocationId, companyId,
                NetmonTypes.SERVICE_TYPES.COLOCATION)) {
            logService.createLog("GET_COLOCATION_DEVICE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[colocationId=" + colocationId + ",deviceId=" + deviceId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return colocationDeviceService.getColocationDeviceById(deviceId, currentUser);
    }

    /**
     * getting list of ips by service id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return ip responses page by page
     */
    @GetMapping("/{serviceType}/{serviceId}/ips/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<ServiceIPResponse> getServiceIPs(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable String serviceType,
                                                          @PathVariable Long serviceId,
                                                          @RequestParam(value = "page",
                                                                  defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size",
                                                                  defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_SERVICE_IPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "",
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_SERVICE_IPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ipService.getServiceIps(serviceId, currentUser, page, size);
    }

    /**
     * getting ip info by id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param ipId the unique ip number
     * @return ip response
     */
    @GetMapping("/{serviceType}/{serviceId}/ips/{ipId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ServiceIPResponse getServiceIPById(@CurrentUser UserPrincipal currentUser,
                                              @PathVariable String serviceType,
                                              @PathVariable Long serviceId,
                                              @PathVariable Long ipId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + "]", "",
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_SERVICE_IP_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ipService.getServiceIpById(ipId, currentUser);
    }

    /**
     * creating a billing
     * @param currentUser the user id who currently logged in
     * @param startDate the start date to calculate a billing
     * @param endDate the end date to calculate a billing
     * @param description an explanation of the action
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @GetMapping("/{serviceType}/{serviceId}/billings/calculate")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> calculateBilling(@CurrentUser UserPrincipal currentUser,
                                              @RequestParam("startDate")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam("endDate")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                              @RequestParam("description") String description,
                                              @PathVariable String serviceType,
                                              @PathVariable Long serviceId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CALCULATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",startDate=" + startDate + ",endDate=" + endDate +"]", "",
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("CALCULATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",startDate=" + startDate + ",endDate=" + endDate +"]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        // checking start and end date
        if (startDate.isAfter(endDate)) {
            logService.createLog("CALCULATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",startDate=" + startDate + ",endDate=" + endDate +"]", "",
                    "The endDate should be greater then the startDate.");
            throw new BadRequestException("The endDate should be greater then the startDate.");
        }

        Billing billing = billingService.calculateBilling(currentUser, serviceId, startDate, endDate,
                description);

        serviceBillingRepository.save(billing);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{billingId}")
                .buildAndExpand(billing.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The billing created successfully."));
    }

    /**
     * updating order id for a billing
     * @param callAPIBillingRequest the api information object to pay
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param billingId the unique billing number
     * @return OK response or report error
     */
    @PutMapping("/{serviceType}/{serviceId}/billings/{billingId}/updateOrderId")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateBillingOrderId(@Valid @RequestBody CallAPIBillingRequest callAPIBillingRequest,
                                                      @CurrentUser UserPrincipal currentUser,
                                                      @PathVariable String serviceType,
                                                      @PathVariable Long serviceId,
                                                      @PathVariable Long billingId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("UPDATE_BILLING_ORDERID", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",billingId=" + billingId + "]",
                    callAPIBillingRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("UPDATE_BILLING_ORDERID", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",billingId=" + billingId + "]",
                    callAPIBillingRequest.toString(), "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        billingService.updateOrderId(currentUser, billingId, callAPIBillingRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{billingId}")
                .buildAndExpand(billingId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The billing updated successfully."));
    }

    /**
     * updating reference id for a billing
     * @param callAPIBillingRequest the api information object to pay
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param billingId the unique billing number
     * @return OK response or report error
     */
    @PutMapping("/{serviceType}/{serviceId}/billings/{billingId}/updateReferenceId")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateBillingReferenceId(@Valid @RequestBody CallAPIBillingRequest callAPIBillingRequest,
                                                      @CurrentUser UserPrincipal currentUser,
                                                      @PathVariable String serviceType,
                                                      @PathVariable Long serviceId,
                                                      @PathVariable Long billingId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("UPDATE_BILLING_REFERENCEID", currentUser.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",billingId=" + billingId + "]",
                    callAPIBillingRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("UPDATE_BILLING_REFERENCEID", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",billingId=" + billingId + "]",
                    callAPIBillingRequest.toString(), "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        billingService.updateReferenceId(currentUser, billingId, callAPIBillingRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{billingId}")
                .buildAndExpand(billingId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The billing updated successfully."));
    }

    /**
     * getting last paid billing info
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @return billing response
     */
    @GetMapping("/{serviceType}/{serviceId}/billings/lastPaid")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Billing getLastPaidBilling(@CurrentUser UserPrincipal currentUser,
                                                @PathVariable String serviceType,
                                                @PathVariable Long serviceId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_LAST_PAID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_LAST_PAID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return billingService.getLastPaidBilling(serviceId);
    }

    /**
     * getting value of ip usage
     * @param startDate the start date to calculate ip usage
     * @param endDate the end date to calculate ip usage
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param ipId the unique ip number
     * @param currentUser the user id who currently logged in
     * @return ip usage response
     */
    @GetMapping("/{serviceType}/{serviceId}/ip/{ipId}/usage")
    @PreAuthorize("hasRole('CUSTOMER')")
    public IpUsageResponse getServiceIpUsage(@RequestParam("startDate")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                             @RequestParam("endDate")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                             @PathVariable String serviceType,
                                             @PathVariable Long serviceId,
                                             @PathVariable Long ipId,
                                             @CurrentUser UserPrincipal currentUser) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + ",startDate=" + startDate + ",endDate=" + endDate +"]",
                    "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        if (!companyRepository.existsByIdAndUserId(companyId, currentUser.getId())) {
            logService.createLog("IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + ",startDate=" + startDate + ",endDate=" + endDate +"]",
                    "", "This company does not belong to the customer.");
            throw new AccessDeniedException("This company does not belong to the customer.");
        }

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + ",startDate=" + startDate + ",endDate=" + endDate +"]",
                    "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        if (!serviceIPRepository.existsByIdAndNetmonServiceId(ipId, serviceId)) {
            logService.createLog("IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + ",startDate=" + startDate + ",endDate=" + endDate +"]",
                    "", "The ip does not belong to service.");
            throw new AccessDeniedException("The ip does not belong to service.");
        }

        if (endDate.isBefore(startDate)) {
            logService.createLog("IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ipId=" + ipId + ",startDate=" + startDate + ",endDate=" + endDate +"]",
                    "", "The endDate should be greater then the startDate.");
            throw new BadRequestException("The endDate should be greater then the startDate.");
        }

        IP ip = serviceIPRepository.getOne(ipId);

        Vector<IpUsage> ipUpUsages = ProcessIpUsageFiles.processFiles(startDate,
                endDate, ip.getIp(), "up.csv");

        Vector<IpUsage> ipDownUsages = ProcessIpUsageFiles.processFiles(startDate,
                endDate, ip.getIp(), "dl.csv");

        logService.createLog("IP_USAGE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + ",ipId=" + ipId + ",startDate=" + startDate + ",endDate=" + endDate +"]",
                "", "");

        return new IpUsageResponse(startDate, endDate, ip.getIp(), ipUpUsages, ipDownUsages);
    }

    /**
     * create a new port
     * @param servicePortRequest the port information object
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @PostMapping("/{serviceType}/{serviceId}/ports/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createColocationPort(@Valid @RequestBody ServicePortRequest servicePortRequest,
                                                   @CurrentUser UserPrincipal currentUser,
                                                   @PathVariable String serviceType,
                                                   @PathVariable Long serviceId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CREATE_COLOCATION_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", servicePortRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("CREATE_COLOCATION_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", servicePortRequest.toString(),
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        if (servicePortRepository.existsByPortAndNetmonServiceId(servicePortRequest.getPort(), serviceId)) {
            logService.createLog("CREATE_COLOCATION_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", servicePortRequest.toString(), "This port is already assigned.");

            return new ResponseEntity<>(new ApiResponse(false, "This port is already assigned."),
                    HttpStatus.BAD_REQUEST);
        }

        Port servicePort = portService.create(servicePortRequest, currentUser, serviceId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{portId}")
                .buildAndExpand(servicePort.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The port created successfully."));
    }

    /**
     * getting list of ports by service id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return port responses page by page
     */
    @GetMapping("/{serviceType}/{serviceId}/ports/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<ServicePortResponse> getColocationPorts(@CurrentUser UserPrincipal currentUser,
                                                                  @PathVariable String serviceType,
                                                                  @PathVariable Long serviceId,
                                                                  @RequestParam(value = "page",
                                                                          defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                  @RequestParam(value = "size",
                                                                          defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_SERVICE_PORTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_ALL_SERVICE_PORTS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return portService.getServicePorts(currentUser, serviceId, page, size);
    }

    /**
     * getting port info by id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param portId the unique port number
     * @return port response
     */
    @GetMapping("/{serviceType}/{serviceId}/ports/{portId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ServicePortResponse getColocationPortById(@CurrentUser UserPrincipal currentUser,
                                                      @PathVariable String serviceType,
                                                      @PathVariable Long serviceId,
                                                      @PathVariable Long portId) {
        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_SERVICE_PORT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",portId=" + portId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_SERVICE_PORT_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",portId=" + portId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return portService.getServicePortById(portId, currentUser);
    }

    /**
     * create a new ticket
     * @param serviceTicketRequest the ticket information object
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @PostMapping("/{serviceType}/{serviceId}/tickets/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createServiceTicket(@Valid @RequestBody ServiceTicketRequest serviceTicketRequest,
                                                 @CurrentUser UserPrincipal currentUser,
                                                 @PathVariable String serviceType,
                                                 @PathVariable Long serviceId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CREATE_SERVICE_TICKET", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("CREATE_SERVICE_TICKET", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", serviceTicketRequest.toString(),
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        Ticket serviceTicket = ticketService.create(serviceTicketRequest, currentUser, serviceId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ticketId}")
                .buildAndExpand(serviceTicket.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The ticket created successfully."));
    }

    /**
     * getting list of tickets by service id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return ticket responses page by page
     */
    @GetMapping("/{serviceType}/{serviceId}/tickets/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<ServiceTicketResponse> getServiceTickets(@CurrentUser UserPrincipal currentUser,
                                                                  @PathVariable String serviceType,
                                                                  @PathVariable Long serviceId,
                                                                  @RequestParam(value = "page",
                                                                          defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                  @RequestParam(value = "size",
                                                                          defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_SERVICE_TICKETS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_ALL_SERVICE_TICKETS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ticketService.getServiceTickets(currentUser, serviceId, page, size);
    }

    /**
     * getting ticket info by id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param ticketId the unique ticket number
     * @return ticket response
     */
    @GetMapping("/{serviceType}/{serviceId}/tickets/{ticketId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ServiceTicketResponse getServiceTicketById(@CurrentUser UserPrincipal currentUser,
                                                      @PathVariable String serviceType,
                                                      @PathVariable Long serviceId,
                                                      @PathVariable Long ticketId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_SERVICE_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ticketId=" + ticketId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_SERVICE_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",ticketId=" + ticketId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return ticketService.getServiceTicketById(ticketId, currentUser);
    }

    /**
     * getting billing info by id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param billingId the unique billing number
     * @return billing response
     */
    @GetMapping("/{serviceType}/{serviceId}/billings/{billingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ServiceBillingResponse getServiceBillingById(@CurrentUser UserPrincipal currentUser,
                                                        @PathVariable String serviceType,
                                                        @PathVariable Long serviceId,
                                                        @PathVariable Long billingId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",billingId=" + billingId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + ",billingId=" + billingId + "]", "",
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return billingService.getServiceBillingById(billingId, currentUser);
    }

    /**
     * getting list of billings by service id
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return billing responses page by page
     */
    @GetMapping("/{serviceType}/{serviceId}/billings/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<ServiceBillingResponse> getServiceBillings(@CurrentUser UserPrincipal currentUser,
                                                                    @PathVariable String serviceType,
                                                                    @PathVariable Long serviceId,
                                                                    @RequestParam(value = "page",
                                                                            defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(value = "size",
                                                                            defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return billingService.getServiceBillings(currentUser, serviceId, page, size);
    }

    /**
     * create a new technical person
     * @param technicalPersonRequest the technicalPerson information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/technicalPerson/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createTechnicalPerson(@Valid @RequestBody TechnicalPersonRequest technicalPersonRequest,
                                                   @CurrentUser UserPrincipal currentUser) {

        if (technicalPersonRepository.existsByNationalIdAndUserId(technicalPersonRequest.getNationalId(),
                currentUser.getId())) {
            logService.createLog("CREATE_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", technicalPersonRequest.toString(), "This technical person is exists for the customer.");
            throw new BadRequestException("This technical person is exists for the customer.");
        }

        TechnicalPerson technicalPerson = technicalPersonService.create(currentUser,
                technicalPersonRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{technicalPersonId}")
                .buildAndExpand(technicalPerson.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The technical person created successfully."));
    }

    /**
     * getting list of all technical person
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return technical person responses page by page
     */
    @GetMapping("/technicalPerson/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<TechnicalPersonResponse> getTechnicalPersons(@CurrentUser UserPrincipal currentUser,
                                                                      @RequestParam(value = "page",
                                                                              defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                      @RequestParam(value = "size",
                                                                              defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return technicalPersonService.getTechnicalPersons(currentUser, page, size);

    }

    /**
     * getting technical person info by id
     * @param currentUser the user id who currently logged in
     * @param technicalPersonId the unique technicalPerson number
     * @return technical person response
     */
    @GetMapping("/technicalPerson/{technicalPersonId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public TechnicalPersonResponse getTechnicalPersonById(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long technicalPersonId) {

        return technicalPersonService.getTechnicalPersonById(technicalPersonId, currentUser);
    }

    /**
     * updating info (name, email, mobile, ...) of technical person
     * @param technicalPersonRequest the technicalPerson information object
     * @param currentUser the user id who currently logged in
     * @param technicalPersonId the unique technicalPerson number
     * @return OK response or report error
     */
    @PutMapping("/technicalPerson/{technicalPersonId}/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateTechnicalPerson(@Valid @RequestBody TechnicalPersonRequest technicalPersonRequest,
                                                   @CurrentUser UserPrincipal currentUser,
                                                   @PathVariable Long technicalPersonId) {

        if (!technicalPersonRepository.existsByIdAndUserId(technicalPersonId, currentUser.getId())) {
            logService.createLog("UPDATE_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[technicalPersonId=" + technicalPersonId + "]", technicalPersonRequest.toString(),
                    "This technical person does not belong to the customer.");
            throw new BadRequestException("This technical person does not belong to the customer.");
        }

        technicalPersonService.update(currentUser, technicalPersonId, technicalPersonRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{technicalPersonId}")
                .buildAndExpand(technicalPersonId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "TechnicalPerson updated successfully"));
    }

    /**
     * removing a technical person
     * @param currentUser the user id who currently logged in
     * @param technicalPersonId the unique technicalPerson number
     * @return OK response or report error
     */
    @DeleteMapping("/technicalPerson/{technicalPersonId}/delete")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> deleteTechnicalPerson(@CurrentUser UserPrincipal currentUser,
                                                   @PathVariable Long technicalPersonId) {

        if (!technicalPersonRepository.existsByIdAndUserId(technicalPersonId, currentUser.getId())) {
            logService.createLog("DELETE_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[technicalPersonId=" + technicalPersonId + "]", "",
                    "This technical person does not belong to the customer.");
            throw new BadRequestException("This technical person does not belong to the customer.");
        }

        if (netmonServiceRepository.existsByTechnicalPersonId(technicalPersonId)) {

            logService.createLog("DELETE_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[technicalPersonId=" + technicalPersonId + "]", "",
                    "This technical person is assigned to another company.");
            throw new BadRequestException("This technical person is assigned to another company.");
        }

        technicalPersonService.delete(currentUser, technicalPersonId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{technicalPersonId}")
                .buildAndExpand(technicalPersonId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The technical person deleted successfully."));
    }

    /**
     * creating a new vps
     * @param vpsRequest the vps information object
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/vps/new")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createVPS(@Valid @RequestBody VpsRequest vpsRequest,
                                       @CurrentUser UserPrincipal currentUser) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", vpsRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }

        Optional<TechnicalPerson> technicalPersonOptional = technicalPersonRepository.findByIdAndUserId(
                vpsRequest.getTechnicalPersonId(), currentUser.getId());
        if (!technicalPersonOptional.isPresent()) {
            logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", vpsRequest.toString(), "This technical person does not exists.");
            throw new BadRequestException("This technical person does not exists.");
        }

        Optional<OSType> osTypeOptional = osTypeRepository.findById(vpsRequest.getOsTypeId());
        if (!osTypeOptional.isPresent()) {
            logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", vpsRequest.toString(), "This os type does not exists.");
            throw new BadRequestException("This os type does not exists.");
        }
        OSType osType = osTypeOptional.get();
        if(!osType.isActive()){
            logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", vpsRequest.toString(), "The os type is not active.");
            throw new AccessDeniedException("The os type is not active.");
        }

        Optional<VpsPlan> vpsPlanOptional = vpsPlanRepository.findById(vpsRequest.getVpsPlan());
        if (!vpsPlanOptional.isPresent()) {
            logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", vpsRequest.toString(), "This vps plan does not exists.");
            throw new BadRequestException("This vps plan does not exists.");
        }

        VpsPlan vpsPlan = vpsPlanOptional.get();
        if(!vpsPlan.isActive()){
            logService.createLog("CREATE_VPS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", vpsRequest.toString(), "The vps plan is not active.");
            throw new AccessDeniedException("The vps plan is not active.");
        }

        NetmonService netmonService = nsService.createVPS(currentUser, vpsRequest, companyOptional.get(),
                technicalPersonOptional.get(), osType, vpsPlan);

        int[] ports = vpsRequest.getPorts();
        for (int port : ports) {

            if (!servicePortRepository.existsByPortAndNetmonServiceId(port, netmonService.getId())) {
                ServicePortRequest servicePortRequest = new ServicePortRequest(port, "");
                portService.create(servicePortRequest, currentUser, netmonService.getId());
            }
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{vpsId}")
                .buildAndExpand(netmonService.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The VPS created successfully."));
    }

    /**
     * getting list of all vpses
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return vps responses page by page
     */
    @GetMapping("/vps/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<VpsResponse> getVPS(@CurrentUser UserPrincipal currentUser,
                                             @RequestParam(value = "page",
                                                     defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                             @RequestParam(value = "size",
                                                     defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_ALL_CUSTOMER_VPSES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        return nsService.getCustomerVPSes(currentUser, companyId, page, size);
    }

    /**
     * getting vps info by id
     * @param currentUser the user id who currently logged in
     * @param vpsId the unique vps number
     * @return vps response
     */
    @GetMapping("/vps/{vpsId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public VpsResponse getVPSById(@CurrentUser UserPrincipal currentUser, @PathVariable Long vpsId) {
        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[vpsId=" + vpsId + "]", "", "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(vpsId, companyId,
                NetmonTypes.SERVICE_TYPES.VPS)) {
            logService.createLog("GET_VPS_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[vpsId=" + vpsId + "]", "", "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        return nsService.getVPSById(vpsId, currentUser);
    }

    /**
     * getting list of all os types
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return os type responses page by page
     */
    @GetMapping("/osType/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<OSTypeResponse> getOSTypes(@CurrentUser UserPrincipal currentUser,
                                                    @RequestParam(value = "page",
                                                            defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size",
                                                            defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return osTypeService.getOSTypes(currentUser, page, size);

    }

    /**
     * changing service status by customer
     * @param serviceConfirmRequest the service information object to confirm
     * @param currentUser the user id who currently logged in
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @PutMapping("/{serviceType}/{serviceId}/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> confirmService(@Valid @RequestBody ServiceConfirmRequest serviceConfirmRequest,
                                            @CurrentUser UserPrincipal currentUser,
                                            @PathVariable String serviceType,
                                            @PathVariable Long serviceId) {

        Optional<Company> companyOptional = companyRepository.findByUserId(currentUser.getId());
        if(!companyOptional.isPresent()){
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(), "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", currentUser.getId());
        }
        Long companyId = companyOptional.get().getId();

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog("CONFIRM_SERVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[serviceId=" + serviceId + "]", serviceConfirmRequest.toString(),
                    "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }

        nsService.customerConfirmService(currentUser, serviceId, serviceConfirmRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{serviceId}")
                .buildAndExpand(serviceId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The service updated successfully."));
    }

    /**
     * getiing list of all plans
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return plan responses page by page
     */
    @GetMapping("/plans/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResponse<VpsPlanResponse> getVpsPlans(@CurrentUser UserPrincipal currentUser,
                                                      @RequestParam(value = "page",
                                                              defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size",
                                                              defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {


        return vpsPlanService.getPlans(currentUser, page, size);
    }
}
