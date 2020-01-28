package org.redapps.netmon.controller.technical;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

@RestController
@RequestMapping("/api/technical")
public class TechnicalController {

    private final IPService ipService;
    private final ServiceIPRepository serviceIPRepository;
    private final NetmonServiceRepository netmonServiceRepository;
//     private final DeviceService colocationDeviceService;
//     private final ColocationDeviceRepository colocationDeviceRepository;
    private final ServicePortRepository servicePortRepository;
    private final PortService portService;
    private final TicketService ticketService;
    private final ServiceTicketRepository serviceTicketRepository;
    private final LogService logService;
    private final UserRepository userRepository;
    private final OSTypeService osTypeService;
    private final CompanyRepository companyRepository;
    private final NSService nsService;

    @Autowired
    public TechnicalController(ServiceIPRepository serviceIPRepository, NetmonServiceRepository netmonServiceRepository,
                        //        DeviceService colocationDeviceService,  
                               OSTypeService osTypeService,
                        //        ColocationDeviceRepository colocationDeviceRepository, 
                               LogService logService,
                               ServicePortRepository servicePortRepository,
                               ServiceTicketRepository serviceTicketRepository, IPService ipService,
                               PortService portService, TicketService ticketService,
                               UserRepository userRepository, CompanyRepository companyRepository,
                               NSService nsService) {
        this.serviceIPRepository = serviceIPRepository;
        this.netmonServiceRepository = netmonServiceRepository;
        // this.colocationDeviceService = colocationDeviceService;
        // this.colocationDeviceRepository = colocationDeviceRepository;
        this.osTypeService = osTypeService;
        this.logService = logService;
        this.servicePortRepository = servicePortRepository;
        this.serviceTicketRepository = serviceTicketRepository;
        this.ipService = ipService;
        this.portService = portService;
        this.ticketService = ticketService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.nsService = nsService;
    }

    /**
     * Get all urls for technical role.
     * @return list of urls
     */
    @GetMapping("/urls")
    @PreAuthorize("hasRole('TECHNICAL')")
    public String[] getTechnicalUrls() {

        return new String[] {"GET: /api/technical/urls",
                "GET: /api/technical/customers/all",
                "GET: /api/technical/customers/{customerId}",
                "POST: /api/technical/customers/{customerId}/colocations/{colocationId}/ips/new",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/ips/all",
                "DELETE: /api/technical/customers/{customerId}/colocations/{colocationId}/ips/{ipId}",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/ips/{ipId}/delete/",
                "PUT: /api/technical/customers/{customerId}/colocations/{colocationId}/ips/{ipId}/update/",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/ip/{ipId}/usage",
                "POST: /api/technical/customers/{customerId}/colocations/{colocationId}/ports/new",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/ports/all",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/ports/{portId}",
                "PUT: /api/technical/customers/{customerId}/colocations/{colocationId}/ports/{portId}/update",
                "DELETE: /api/technical/customers/{customerId}/colocations/{colocationId}/ports/{portId}/delete",
                "POST: /api/technical/customers/{customerId}/colocations/{colocationId}/devices/new",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/devices/all",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}",
                "DELETE: /api/technical/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}/delete",
                "PUT: /api/technical/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}/update",
                "PUT: /api/technical/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}/status",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/tickets/all",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}/tickets/{ticketId}",
                "POST: /api/technical/customers/{customerId}/colocations/{colocationId}/tickets/{ticketId}/response",
                "GET: /api/technical/customers/{customerId}/colocations/all",
                "GET: /api/technical/customers/{customerId}/colocations/{colocationId}",
                "POST: /api/technical/customers/{customerId}/colocations/{colocationId}/rename",
                "POST: /api/technical/customers/{customerId}/vps/{vpsId}/ips/new",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/ips/all",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/ips/{ipId}",
                "DELETE: /api/technical/customers/{customerId}/vps/{vpsId}/ips/{ipId}/delete/",
                "PUT: /api/technical/customers/{customerId}/vps/{vpsId}/ips/{ipId}/update/",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/ip/{ipId}/usage",
                "POST: /api/technical/customers/{customerId}/vps/{vpsId}/ports/new",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/ports/all",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/ports/{portId}",
                "PUT: /api/technical/customers/{customerId}/vps/{vpsId}/ports/{portId}/update",
                "DELETE: /api/technical/customers/{customerId}/vps/{vpsId}/ports/{portId}/delete",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/tickets/all",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}/tickets/{ticketId}",
                "POST: /api/technical/customers/{customerId}/vps/{vpsId}/tickets/{ticketId}/response",
                "GET: /api/technical/plans/all",
                "POST: /api/technical/osType/new",
                "PUT: /api/technical/osType/{osTypeId}/active",
                "PUT: /api/technical/osType/{osTypeId}/inactive",
                "PUT: /api/technical/osType/{osTypeId}/update",
                "GET: /api/technical/osType/all",
                "GET: /api/technical/osType/{osTypeId}",
                "GET: /api/technical/customers/{customerId}/vps/all",
                "GET: /api/technical/customers/{customerId}/vps/{vpsId}",
                "POST: /api/technical/customers/{customerId}/vps/{vpsId}/rename",
                "GET: /api/technical/colocations/all",
                "GET: /api/technical/colocations/{colocationId}",
                "GET: /api/technical/vps/all",
                "GET: /api/technical/vps/{vpsId}"};
    }

    /**
     * Assign an ip address to a service.
     * @param serviceIPRequest the ip information object
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @PostMapping("/customers/{customerId}/{serviceType}/{serviceId}/ips/new")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> createServiceIP(@Valid @RequestBody ServiceIPRequest serviceIPRequest,
                                             @CurrentUser UserPrincipal currentUser,
                                             @PathVariable Long customerId,
                                             @PathVariable String serviceType,
                                             @PathVariable Long serviceId) {

        initialChecking("CREATE_COLOCATION_IP", currentUser.getUsername(), customerId,
                serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId + "]",
                serviceIPRequest.toString());

        // Check if ip is already assigned to this service or not
        if (serviceIPRepository.existsByIpAndNetmonServiceId(serviceIPRequest.getIp(), serviceId)) {
            logService.createLog("CREATE_COLOCATION_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + "]", serviceIPRequest.toString(),
                    "This ip is already assigned.");
            return new ResponseEntity<>(new ApiResponse(false, "This ip is already assigned."),
                    HttpStatus.BAD_REQUEST);
        }

        LocalDate createDate = LocalDate.now();                                
        IP serviceIp = ipService.create(serviceIPRequest, currentUser, serviceId, createDate);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ipId}")
                .buildAndExpand(serviceIp.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The IP created successfully."));
    }

    /**
     * Delete an ip from service ip list.
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param ipId the unique ip number
     * @return OK response or report error
     */
    @DeleteMapping("/customers/{customerId}/{serviceType}/{serviceId}/ips/{ipId}/delete")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> deleteServiceIP(@CurrentUser UserPrincipal currentUser,
                                             @PathVariable Long customerId,
                                             @PathVariable String serviceType,
                                             @PathVariable Long serviceId,
                                             @PathVariable Long ipId) {

        initialChecking("DELETE_SERVICE_IP", currentUser.getUsername(), customerId,
                serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId
                        + ",ipId=" + ipId + "]", "");

        if (!serviceIPRepository.existsByIdAndNetmonServiceId(ipId, serviceId)) {
            logService.createLog("DELETE_SERVICE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",ipId=" + ipId + "]", "",
                    "This ip does not exists.");
            return new ResponseEntity<>(new ApiResponse(false, "This ip does not exists."),
                    HttpStatus.BAD_REQUEST);
        }

        ipService.delete(currentUser, ipId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ipId}")
                .buildAndExpand(ipId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The IP deleted successfully."));

    }

    /**
     * Changing an ip
     * @param serviceIPRequest the ip information object
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param ipId the unique ip number
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/ips/{ipId}/update")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> updateServiceIP(@Valid @RequestBody ServiceIPRequest serviceIPRequest,
                                             @CurrentUser UserPrincipal currentUser,
                                             @PathVariable Long customerId,
                                             @PathVariable String serviceType,
                                             @PathVariable Long ipId,
                                             @PathVariable Long serviceId) {

        initialChecking("UPDATE_SERVICE_IP", currentUser.getUsername(), customerId,
                serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId + ",ipId=" + ipId + "]",
                serviceIPRequest.toString());

        if (!serviceIPRepository.existsByIdAndNetmonServiceId(ipId, serviceId)) {
            logService.createLog("DELETE_SERVICE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",ipId=" + ipId + "]",
                    serviceIPRequest.toString(), "This ip does not belong to the service.");
            return new ResponseEntity<>(new ApiResponse(false, "This ip does not belong to the service."),
                    HttpStatus.BAD_REQUEST);
        }

        if (serviceIPRepository.existsByIpAndNetmonServiceId(serviceIPRequest.getIp(), serviceId)) {
            logService.createLog("DELETE_SERVICE_IP", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",ipId=" + ipId + "]",
                    serviceIPRequest.toString(), "This ip is already assigned." );
            return new ResponseEntity<>(new ApiResponse(false, "This ip is already assigned."),
                    HttpStatus.BAD_REQUEST);
        }

        ipService.update(ipId, serviceIPRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ipId}")
                .buildAndExpand(ipId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The IP updated successfully."));
    }

//     /**
//      * Create a new colocation device
//      * @param colocationDeviceRequest the device information object the device information object
//      * @param currentUser the user id who currently logged in
//      * @param customerId the customer unique number who requested the service
//      * @param colocationId the unique colocation number
//      * @return OK response or report error
//      */
//     @PostMapping("/customers/{customerId}/colocations/{colocationId}/devices/new")
//     @PreAuthorize("hasRole('TECHNICAL')")
//     public ResponseEntity<?> createColocationDevice(@Valid @RequestBody DeviceRequest colocationDeviceRequest,
//                                                      @CurrentUser UserPrincipal currentUser,
//                                                      @PathVariable Long customerId,
//                                                      @PathVariable Long colocationId) {

//         initialChecking("UPDATE_SERVICE_IP", currentUser.getUsername(), customerId,
//                 "colocations", colocationId, "[customerId=" + customerId + ",colocationId=" + colocationId + "]",
//                 colocationDeviceRequest.toString());

//         Device colocationDevice = colocationDeviceService.create(colocationDeviceRequest, currentUser,
//                 colocationId);

//         URI location = ServletUriComponentsBuilder
//                 .fromCurrentRequest().path("/{deviceId}")
//                 .buildAndExpand(colocationDevice.getId()).toUri();

//         return ResponseEntity.created(location)
//                 .body(new ApiResponse(true, "The device created successfully."));
//     }

//     /**
//      * Delete a device
//      * @param currentUser the user id who currently logged in
//      * @param customerId the customer unique number who requested the service
//      * @param deviceId the unique device number
//      * @param colocationId the unique colocation number
//      * @return OK response or report error
//      */
//     @DeleteMapping("/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}/delete")
//     @PreAuthorize("hasRole('TECHNICAL')")
//     public ResponseEntity<?> deleteColocationDevice(@CurrentUser UserPrincipal currentUser,
//                                                      @PathVariable Long customerId,
//                                                      @PathVariable Long deviceId,
//                                                      @PathVariable Long colocationId) {

//         initialChecking("UPDATE_SERVICE_IP", currentUser.getUsername(), customerId,
//                 "colocations", colocationId, "[customerId=" + customerId + ",colocationId=" + colocationId
//                         + ",deviceId=" + deviceId + "]", "");

//         if (!colocationDeviceRepository.existsByIdAndNetmonServiceId(deviceId, colocationId)) {
//             logService.createLog("DELETE_COLOCATION_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[customerId=" + customerId + ",colocationId=" + colocationId + ",deviceId=" + deviceId + "]", "",
//                     "The device does not exists.");
//             return new ResponseEntity<>(new ApiResponse(false, "The device does not exists."),
//                     HttpStatus.BAD_REQUEST);
//         }

//         colocationDeviceService.delete(currentUser, deviceId);

//         URI location = ServletUriComponentsBuilder
//                 .fromCurrentRequest().path("/{deviceId}")
//                 .buildAndExpand(deviceId).toUri();

//         return ResponseEntity.created(location)
//                 .body(new ApiResponse(true, "The device deleted successfully."));
//     }

//     /**
//      * Update the name and description.
//      * @param deviceRequest
//      * @param currentUser the user id who currently logged in
//      * @param customerId the customer unique number who requested the service
//      * @param deviceId the unique device number
//      * @param colocationId the unique colocation number
//      * @return OK response or report error
//      */
//     @PutMapping("/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}/update")
//     @PreAuthorize("hasRole('TECHNICAL')")
//     public ResponseEntity<?> updateColocationDevice(@Valid @RequestBody DeviceRequest deviceRequest,
//                                                      @CurrentUser UserPrincipal currentUser,
//                                                      @PathVariable Long customerId,
//                                                      @PathVariable Long deviceId,
//                                                      @PathVariable Long colocationId) {

//         initialChecking("UPDATE_SERVICE_IP", currentUser.getUsername(), customerId,
//                 "colocations", colocationId, "[customerId=" + customerId + ",colocationId=" + colocationId
//                         + ",deviceId=" + deviceId + "]", deviceRequest.toString());

//         if (!colocationDeviceRepository.existsByIdAndNetmonServiceId(deviceId, colocationId)) {
//             logService.createLog("UPDATE_COLOCATION_DEVICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[customerId=" + customerId + ",colocationId=" + colocationId + ",deviceId=" + deviceId + "]",
//                     deviceRequest.toString(), "This device does not belong to the service.");
//             return new ResponseEntity<>(new ApiResponse(false, "This device does not belong to the service."),
//                     HttpStatus.BAD_REQUEST);
//         }

//         colocationDeviceService.update(deviceId, deviceRequest, currentUser);

//         URI location = ServletUriComponentsBuilder
//                 .fromCurrentRequest().path("/{deviceId}")
//                 .buildAndExpand(deviceId).toUri();

//         return ResponseEntity.created(location)
//                 .body(new ApiResponse(true, "The device updated successfully."));
//     }

//     /**
//      * Change status
//      * @param deviceStatusRequest
//      * @param currentUser the user id who currently logged in
//      * @param customerId the customer unique number who requested the service
//      * @param deviceId the unique device number
//      * @param colocationId the unique colocation number
//      * @return OK response or report error
//      */
//     @PutMapping("/customers/{customerId}/colocations/{colocationId}/devices/{deviceId}/status")
//     @PreAuthorize("hasRole('TECHNICAL')")
//     public ResponseEntity<?> changingColocationDeviceStatus(@Valid @RequestBody DeviceStatusRequest deviceStatusRequest,
//                                                              @CurrentUser UserPrincipal currentUser,
//                                                              @PathVariable Long customerId,
//                                                              @PathVariable Long deviceId,
//                                                              @PathVariable Long colocationId) {

//         initialChecking("CHANGE_COLOCATION_DEVICE_STATUS", currentUser.getUsername(), customerId,
//                 "colocations", colocationId, "[customerId=" + customerId + ",colocationId=" + colocationId
//                         + ",deviceId=" + deviceId + "]", deviceStatusRequest.toString());

//         if (!colocationDeviceRepository.existsByIdAndNetmonServiceId(deviceId, colocationId)) {
//             logService.createLog("CHANGE_COLOCATION_DEVICE_STATUS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[customerId=" + customerId + ",colocationId=" + colocationId + ",deviceId=" + deviceId + "]",
//                     deviceStatusRequest.toString(), "This device does not belong to the service.");
//             return new ResponseEntity<>(new ApiResponse(false, "This device does not belong to the service."),
//                     HttpStatus.BAD_REQUEST);
//         }

//         colocationDeviceService.changeStatus(deviceId, deviceStatusRequest, currentUser);

//         URI location = ServletUriComponentsBuilder
//                 .fromCurrentRequest().path("/{deviceId}")
//                 .buildAndExpand(deviceId).toUri();

//         return ResponseEntity.created(location)
//                 .body(new ApiResponse(true, "The device updated successfully"));
//     }

//     /**
//      * Create a new service port
//      * @param servicePortRequest the port information object
//      * @param currentUser the user id who currently logged in
//      * @param customerId the customer unique number who requested the service
//      * @param serviceType vps / collocation
//      * @param serviceId the unique service number
//      * @return OK response or report error
//      */
//     @PostMapping("/customers/{customerId}/{serviceType}/{serviceId}/ports/new")
//     @PreAuthorize("hasRole('TECHNICAL')")
//     public ResponseEntity<?> createServicePort(@Valid @RequestBody ServicePortRequest servicePortRequest,
//                                                @CurrentUser UserPrincipal currentUser,
//                                                @PathVariable Long customerId,
//                                                @PathVariable String serviceType,
//                                                @PathVariable Long serviceId) {

//         initialChecking("CREATE_SERVICE_PORT", currentUser.getUsername(), customerId,
//                 serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId + "]",
//                 servicePortRequest.toString());

//         LocalDate createDate = LocalDate.now();                                
//         if (servicePortRepository.existsByPortAndNetmonServiceId(servicePortRequest.getPort(), serviceId)) {
//             logService.createLog("CREATE_SERVICE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[customerId=" + customerId + ",serviceId=" + serviceId + "]", servicePortRequest.toString(),
//                     "This port is already assigned to the service.");
//             return new ResponseEntity<>(new ApiResponse(false, "This port is already assigned to the service."),
//                     HttpStatus.BAD_REQUEST);
//         }

//         Port servicePort = portService.create(servicePortRequest, currentUser, serviceId, createDate);

//         URI location = ServletUriComponentsBuilder
//                 .fromCurrentRequest().path("/{portId}")
//                 .buildAndExpand(servicePort.getId()).toUri();

//         return ResponseEntity.created(location)
//                 .body(new ApiResponse(true, "The port created successfully."));
//     }

    /**
     * Delete the service port
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param portId the unique port number
     * @return OK response or report error
     */
    @DeleteMapping("/customers/{customerId}/{serviceType}/{serviceId}/ports/{portId}/delete")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> deleteServicePort(@CurrentUser UserPrincipal currentUser,
                                               @PathVariable Long customerId,
                                               @PathVariable String serviceType,
                                               @PathVariable Long serviceId,
                                               @PathVariable Long portId) {
        initialChecking("DELETE_SERVICE_PORT", currentUser.getUsername(), customerId,
                serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" +
                        serviceId + ",portId=" + portId + "]", "");

        if (!servicePortRepository.existsByIdAndNetmonServiceId(portId, serviceId)) {
            logService.createLog("DELETE_SERVICE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",portId=" + portId + "]", "",
                    "This port does not exists.");
            return new ResponseEntity<>(new ApiResponse(false, "This port does not exists."),
                    HttpStatus.BAD_REQUEST);
        }

        portService.delete(currentUser, portId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{portId}")
                .buildAndExpand(portId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The port deleted successfully."));

    }

//     /**
//      * Change the service port
//      * @param servicePortRequest the port information object
//      * @param currentUser the user id who currently logged in
//      * @param customerId the customer unique number who requested the service
//      * @param serviceType vps / collocation
//      * @param portId the unique port number
//      * @param serviceId the unique service number
//      * @return OK response or report error
//      */
//     @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/ports/{portId}/update")
//     @PreAuthorize("hasRole('TECHNICAL')")
//     public ResponseEntity<?> updateServicePort(@Valid @RequestBody ServicePortRequest servicePortRequest,
//                                                @CurrentUser UserPrincipal currentUser,
//                                                @PathVariable Long customerId,
//                                                @PathVariable String serviceType,
//                                                @PathVariable Long portId,
//                                                @PathVariable Long serviceId) {
//         initialChecking("UPDATE_SERVICE_PORT", currentUser.getUsername(), customerId,
//                 serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId +
//                         ",portId=" + portId + "]", servicePortRequest.toString());
//         LocalDate createDate = LocalDate.now();
//         if (!servicePortRepository.existsByIdAndNetmonServiceId(portId, serviceId)) {
//             logService.createLog("UPDATE_SERVICE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[customerId=" + customerId + ",serviceId=" + serviceId + ",portId=" + portId + "]",
//                     servicePortRequest.toString(), "This port does not belong to the service.");
//             return new ResponseEntity<>(new ApiResponse(false, "This port does not belong to the service."),
//                     HttpStatus.BAD_REQUEST);
//         }

//         if (servicePortRepository.existsByPortAndNetmonServiceId(servicePortRequest.getPort(), serviceId)) {
//             logService.createLog("UPDATE_SERVICE_PORT", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[customerId=" + customerId + ",serviceId=" + serviceId + ",portId=" + portId + "]",
//                     servicePortRequest.toString(), "This port is already assigned.");
//             return new ResponseEntity<>(new ApiResponse(false, "This port is already assigned."),
//                     HttpStatus.BAD_REQUEST);
//         }

//         portService.update(currentUser, portId, servicePortRequest);

//         URI location = ServletUriComponentsBuilder
//                 .fromCurrentRequest().path("/{portId}")
//                 .buildAndExpand(portId).toUri();

//         return ResponseEntity.created(location)
//                 .body(new ApiResponse(true, "The port updated successfully."));
//     }

    /**
     * Setting the ticket response
     * @param response
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / collocation
     * @param serviceId the unique service number
     * @param ticketId the unique ticket number
     * @return OK response or report error
     */
    @PostMapping("/customers/{customerId}/{serviceType}/{serviceId}/tickets/{ticketId}/response")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> setTicketResponse(@Valid @RequestBody String response,
                                               @CurrentUser UserPrincipal currentUser,
                                               @PathVariable Long customerId,
                                               @PathVariable String serviceType,
                                               @PathVariable Long serviceId,
                                               @PathVariable Long ticketId) {
        initialChecking("SET_TICKET_RESPONSE", currentUser.getUsername(), customerId,
                serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId + ",ticketId="
                        + ticketId + "]", response);

        if(!serviceTicketRepository.existsByIdAndNetmonServiceId(ticketId, serviceId)) {
            logService.createLog("SET_TICKET_RESPONSE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + ",serviceId=" + serviceId + ",ticketId=" + ticketId + "]",
                    response, "This ticket does not belong to the service.");
            throw new AccessDeniedException("This ticket does not belong to the service.");
        }

        ticketService.setTicketResponse(currentUser, ticketId, response);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ticketId}")
                .buildAndExpand(ticketId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Setting response for ticket."));
    }

    /**
     * Create a new os type
     * @param osTypeRequest the osType information object
     * @param currentUser the user id who currently logged in get username to log
     * @return OK response or report error
     */
    @PostMapping("/osType/new")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> createOSType(@Valid @RequestBody OSTypeRequest osTypeRequest,
                                          @CurrentUser UserPrincipal currentUser) {

        OSType osType = osTypeService.create(currentUser, osTypeRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{osTypeId}")
                .buildAndExpand(osType.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The OS-type created successfully."));
    }

    /**
     * Enable the os type object
     * @param osTypeId the unique osType number
     * @param currentUser the user id who currently logged in get username to log
     * @return OK response or report error
     */
    @PutMapping("/osType/{osTypeId}/active")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> activeOsType(@PathVariable Long osTypeId,
                                           @CurrentUser UserPrincipal currentUser) {

        osTypeService.active(osTypeId, currentUser, true);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{planId}")
                .buildAndExpand(osTypeId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The os-type active successfully."));
    }

    /**
     * Disable the os type object
     * @param osTypeId the unique osType number
     * @param currentUser the user id who currently logged in get username to log
     * @return OK response or report error
     */
    @PutMapping("/osType/{osTypeId}/inactive")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> inactiveOsType(@PathVariable Long osTypeId,
                                             @CurrentUser UserPrincipal currentUser) {


        osTypeService.active(osTypeId, currentUser, false);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{osTypeId}")
                .buildAndExpand(osTypeId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The os-type inactive successfully."));
    }

    /**
     * Get list of os types
     * @param currentUser the user id who currently logged in get username to log
     * @param page the page number of the response (default value is 0) page number
     * @param size the page size of each response (default value is 30) page size
     * @return list of os types
     */
    @GetMapping("/osType/all")
    @PreAuthorize("hasRole('TECHNICAL')")
    public PagedResponse<OSTypeResponse> getOSTypes(@CurrentUser UserPrincipal currentUser,
                                                    @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return osTypeService.getOSTypes(currentUser, page, size);

    }

    /**
     * Get the os type object by id
     * @param currentUser the user id who currently logged in get username to log
     * @param osTypeId the unique osType number
     * @return os type response
     */
    @GetMapping("/osType/{osTypeId}")
    @PreAuthorize("hasRole('TECHNICAL')")
    public OSTypeResponse getOSTypeById(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long osTypeId) {

        return osTypeService.getOSTypeById(currentUser, osTypeId);
    }

    @PutMapping("/osType/{osTypeId}/update")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> updateOSTypeId(@Valid @RequestBody OSTypeRequest osTypeRequest,
                                            @CurrentUser UserPrincipal currentUser,
                                            @PathVariable Long osTypeId) {

        osTypeService.update(currentUser, osTypeId, osTypeRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{osTypeId}")
                .buildAndExpand(osTypeId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The OS-Type updated successfully."));
    }

    /**
     * Change service name and status to RUNNING.
     * @param renameServiceRequest
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / colocation
     * @param serviceId the unique service number
     * @return OK response or report error
     */
    @PutMapping("/customers/{customerId}/{serviceType}/{serviceId}/rename")
    @PreAuthorize("hasRole('TECHNICAL')")
    public ResponseEntity<?> renameService(@Valid @RequestBody RenameServiceRequest renameServiceRequest,
                                                          @CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long customerId,
                                                          @PathVariable String serviceType,
                                                          @PathVariable Long serviceId) {

        initialChecking("RENAME_SERVICE", currentUser.getUsername(), customerId,
                serviceType, serviceId, "[customerId=" + customerId + ",serviceId=" + serviceId + "]",
                renameServiceRequest.toString());

        nsService.renameService(currentUser, serviceId, renameServiceRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{serviceId}")
                .buildAndExpand(serviceId).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The service renamed successfully."));
    }

    /**
     * Checking customer, company and service (vps / colocation)
     * @param action to log
     * @param username to log
     * @param customerId the customer unique number who requested the service
     * @param serviceType vps / colocation
     * @param serviceId the unique service number
     * @param requestParams query parameters of request
     * @param bodyParams body parameters of request
     */
    private void initialChecking(String action, String username, Long customerId, String serviceType, Long serviceId,
                                 String requestParams, String bodyParams){

        if (!userRepository.existsById(customerId)) {
            logService.createLog(action, username, NetmonStatus.LOG_STATUS.FAILED, requestParams, bodyParams,
                    "The customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        Optional<Company> companyOptional = companyRepository.findByUserId(customerId);
        if(!companyOptional.isPresent()){
            logService.createLog(action, username, NetmonStatus.LOG_STATUS.FAILED, requestParams, bodyParams,
                    "The customer has no company.");
            throw new ResourceNotFoundException("Company", "userId", customerId);
        }

        Long companyId = companyOptional.get().getId();

        if (!companyRepository.existsByIdAndUserId(companyId, customerId)) {
            logService.createLog(action, username, NetmonStatus.LOG_STATUS.FAILED, requestParams, bodyParams,
                    "This company does not belong to the customer.");
            throw new AccessDeniedException("This company does not belong to the customer.");
        }

        NetmonTypes.SERVICE_TYPES srvType = NetmonTypes.SERVICE_TYPES.COLOCATION;
        if(serviceType.compareToIgnoreCase("vps") == 0)
            srvType = NetmonTypes.SERVICE_TYPES.VPS;

        if (!netmonServiceRepository.existsByIdAndCompanyIdAndServiceType(serviceId, companyId, srvType)) {
            logService.createLog(action, username, NetmonStatus.LOG_STATUS.FAILED,
                    requestParams, bodyParams, "This service does not belong to the company.");
            throw new AccessDeniedException("This service does not belong to the company.");
        }
    }
}
