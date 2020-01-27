// package org.redapps.netmon.service;

// import org.redapps.netmon.exception.AccessDeniedException;
// import org.redapps.netmon.exception.BadRequestException;
// import org.redapps.netmon.exception.ResourceNotFoundException;
// import org.redapps.netmon.model.*;
// import org.redapps.netmon.payload.*;
// import org.redapps.netmon.repository.NetmonServiceRepository;
// import org.redapps.netmon.repository.ServiceBillingRepository;
// import org.redapps.netmon.repository.ServiceIPRepository;
// import org.redapps.netmon.security.UserPrincipal;
// import org.redapps.netmon.util.AppConstants;
// import org.redapps.netmon.util.PaymentStatusService;
// import org.redapps.netmon.util.ProcessIpUsageFiles;
// import org.redapps.netmon.util.NetmonStatus;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.*;

// import static java.time.temporal.ChronoUnit.DAYS;

// @Service
// public class BillingService {

//     private final NetmonServiceRepository netmonServiceRepository;
//     private final ServiceBillingRepository serviceBillingRepository;
//     private final LogService logService;
//     private final ServiceIPRepository serviceIPRepository;

//     @Autowired
//     public BillingService(NetmonServiceRepository netmonServiceRepository, LogService logService,
//                           ServiceBillingRepository serviceBillingRepository,
//                           ServiceIPRepository serviceIPRepository) {
//         this.netmonServiceRepository = netmonServiceRepository;
//         this.logService = logService;
//         this.serviceBillingRepository = serviceBillingRepository;
//         this.serviceIPRepository = serviceIPRepository;
//     }

//     /**
//      * Get a billing info by billing id.
//      * @param billingId the unique billing number
//      * @param currentUser the user id who currently logged in
//      * @return billing response
//      */
//     public ServiceBillingResponse getServiceBillingById(Long billingId,
//                                                         UserPrincipal currentUser) {

//         // find the billing by id
//         Optional<Billing> billingOptional = serviceBillingRepository.findById(billingId);
//         if (!billingOptional.isPresent()) {
//             logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", "", "The billing not found.");
//             throw new ResourceNotFoundException("serviceBilling", "id", billingId);
//         }
//         Billing billing = billingOptional.get();

//         logService.createLog("GET_SERVICE_BILLING_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
//                 "[billingId=" + billingId + "]", "", "");

//         // create a new billing response
//         return new ServiceBillingResponse(billing.getId(),
//                 billing.getStatus(), billing.getValue(), billing.getStartDate(),
//                 billing.getEndDate(), billing.getPaymentDate(),
//                 billing.getDescription(), billing.getOrderId());
//     }

//     /**
//      * Get list of service billings.
//      * @param currentUser the user id who currently logged in
//      * @param serviceId the unique service number
//      * @param page the page number of the response (default value is 0) page number
//      * @param size the page size of each response (default value is 30) page size
//      * @return list of billing responses
//      */
//     public PagedResponse<ServiceBillingResponse> getServiceBillings(UserPrincipal currentUser,
//                                                                     ServiceIdentity serviceId,
//                                                                     int page, int size) {
//         validatePageNumberAndSize(page, size);

//         // find all billings by service id
//         Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
//         Page<Billing> serviceBillings = serviceBillingRepository.findByNetmonServiceId(serviceId, pageable);

//         if(serviceBillings.getNumberOfElements() == 0) {
//             return new PagedResponse<>(Collections.emptyList(), serviceBillings.getNumber(),
//                     serviceBillings.getSize(), serviceBillings.getTotalElements(),
//                     serviceBillings.getTotalPages(), serviceBillings.isLast());
//         }

//         // store the billings into a list
//         Vector<ServiceBillingResponse> serviceBillingResponses = new Vector<>(10);
//         ServiceBillingResponse serviceBillingResponse;

//         for (Billing billing : serviceBillings) {
//                 serviceBillingResponse = new ServiceBillingResponse(billing.getId(),
//                         billing.getStatus(), billing.getValue(), billing.getStartDate(),
//                         billing.getEndDate(), billing.getPaymentDate(), billing.getDescription(),
//                         billing.getOrderId());

//                 serviceBillingResponses.add(serviceBillingResponse);
//         }

//         logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
//                 "[serviceId=" + serviceId + "]", "", "");

//         return new PagedResponse<>(serviceBillingResponses, serviceBillings.getNumber(),
//                 serviceBillings.getSize(), serviceBillings.getTotalElements(), serviceBillings.getTotalPages(),
//                 serviceBillings.isLast());
//     }

//     /**
//      * Get last billing that status is PAID.
//      * @param serviceId the unique service number
//      * @return billing response
//      */
//     public Billing getLastPaidBilling(ServiceIdentity serviceId) {

//         // find list of paid billings
//         List<Billing> billings = serviceBillingRepository.findByNetmonServiceIdAndStatus(serviceId,
//                 NetmonStatus.BillingStatus.PAID);
//         billings.sort(Comparator.comparing(Billing::getPaymentDate));

//         // return number of billings
//         return billings.get(billings.size() - 1);
//     }

// //    public PagedResponse<ServiceBillingResponse> getAllBillings(UserPrincipal currentUser,
// //                                                                    int page, int size) {
// //        validatePageNumberAndSize(page, size);
// //
// //        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
// //
// //        Page<Billing> serviceBillings = serviceBillingRepository.findAll(pageable);
// //
// //        if(serviceBillings.getNumberOfElements() == 0) {
// //            return new PagedResponse<>(Collections.emptyList(), serviceBillings.getNumber(),
// //                    serviceBillings.getSize(), serviceBillings.getTotalElements(),
// //                    serviceBillings.getTotalPages(), serviceBillings.isLast());
// //        }
// //
// //        Vector<ServiceBillingResponse> serviceBillingResponses = new Vector<>(10);
// //
// //        ServiceBillingResponse serviceBillingResponse;
// //
// //        for (Billing billing : serviceBillings) {
// //
// //                serviceBillingResponse = new ServiceBillingResponse(billing.getId(),
// //                        billing.getStatus(), billing.getValue(), billing.getStartDate(),
// //                        billing.getEndDate(), billing.getPaymentDate(),
// //                        billing.getPrice(), billing.getDescription());
// //
// //                serviceBillingResponses.add(serviceBillingResponse);
// //        }
// //
// //        logService.createLog("GET_ALL_SERVICE_BILLING", currentUser.getUsername(), "SUCCESS", "");
// //
// //        return new PagedResponse<>(serviceBillingResponses, serviceBillings.getNumber(),
// //                serviceBillings.getSize(), serviceBillings.getTotalElements(), serviceBillings.getTotalPages(),
// //                serviceBillings.isLast());
// //    }

// //    public void calculateAllBillings(ServiceBillingRequest serviceBillingRequest) {
// //
// //        List<NetmonService> netmonServices = netmonServiceRepository.findAll();
// //
// //        for (NetmonService netmonService : netmonServices) {
// //
// //            LocalDate nowDate = LocalDate.now();
// //            LocalDate startDate = serviceBillingRequest.getStartDate();
// //
// //            if(netmonService.getStartDate().isAfter(serviceBillingRequest.getStartDate()))
// //                startDate = netmonService.getStartDate();
// //
// //            Billing billing = new Billing(nowDate, 1000, startDate, netmonService.getEndDate(),
// //                    netmonService);
// //
// //            serviceBillingRepository.save(billing);
// //        }
// //    }

//     /**
//      * Create a service billing
//      * @param currentUser the user id who currently logged in
//      * @param serviceId the unique service number
//      * @param startDate the start date to calculate a billing
//      * @param endDate the end date to calculate a billing
//      * @param description an explanation of the action
//      * @return billing response
//      */
//     public Billing calculateBilling(UserPrincipal currentUser, ServiceIdentity serviceId,
//                                  LocalDate startDate, LocalDate endDate, String description) {

//         // find service by id
//         Optional<NetmonService> netmonServiceOptional = netmonServiceRepository.findById(serviceId);
//         if (!netmonServiceOptional.isPresent()) {
//             logService.createLog("CALCULATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[serviceId=" + serviceId + ",startDate=" + startDate + ",endDate=" + endDate +"]", "",
//                     "This service does not exists.");
//             throw new ResourceNotFoundException("Service", "serviceId", serviceId);
//         }
//         NetmonService netmonService = netmonServiceOptional.get();

//         // the status should be RUNNING to create a billing
//         if(netmonService.getStatus() != NetmonStatus.ServiceStatus.RUNNING){
//             logService.createLog("CALCULATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[serviceId=" + serviceId + ",startDate=" + startDate + ",endDate=" + endDate +"]", "",
//                     "This service is not running.");
//             throw new AccessDeniedException("This service is not running.");
//         }

//         LocalDate serviceEndDate = netmonService.getStartDate().plusMonths(netmonService.getDuration());

//         if (endDate.isBefore(netmonService.getStartDate())){
//             logService.createLog("CALCULATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[serviceId=" + serviceId + ",startDate=" + startDate + ",endDate=" + endDate +"]", "",
//                     "Bad parameters");
//             throw new BadRequestException("Bad parameters");
//         }

//         if (netmonService.getStartDate().isAfter(startDate))
//             startDate = netmonService.getStartDate();

//         if (serviceEndDate.isBefore(endDate))
//             endDate = serviceEndDate;

//         long daysBetween = DAYS.between(startDate, endDate);

//         double price = (netmonService.getPrice() + netmonService.getExtraPrice()) * daysBetween / 30;

//         double ipsUsageValue = 0;

//         // Calculate ip usage
//         List<IP> ips = serviceIPRepository.findByNetmonServiceId(serviceId);
//         for (IP ip : ips) {
//             Vector<IpUsage> ipUpUsages = ProcessIpUsageFiles.processFiles(startDate,
//                     endDate, ip.getIp(), "up.csv");
//             for (IpUsage ipUsage: ipUpUsages){
//                 ipsUsageValue += ipUsage.getIpUsage();
//             }

//             Vector<IpUsage> ipDownUsages = ProcessIpUsageFiles.processFiles(startDate,
//                     endDate, ip.getIp(), "dl.csv");
//             for (IpUsage ipUsage: ipDownUsages){
//                 ipsUsageValue += ipUsage.getIpUsage();
//             }
//         }

//         double freeTrafficRate = netmonService.getVpsPlan().getFreeTraffic();

//         if(freeTrafficRate != 0 && ipsUsageValue > freeTrafficRate)
//             ipsUsageValue -= freeTrafficRate;

//         price += ipsUsageValue;
//         return new Billing(price, startDate, endDate, description, netmonService);
//     }

//     /**
//      * Pay a billing
//      * @param currentUser the user id who currently logged in
//      * @param billingId the unique billing number
//      * @param updateBillingRequest the billing new information object
//      * @return updated billing
//      */
//     public Billing update(UserPrincipal currentUser, Long billingId, ServiceBillingRequest updateBillingRequest) {

//         // find the billing by id
//         Optional<Billing> billingOptional = serviceBillingRepository.findById(billingId);
//         if (!billingOptional.isPresent()) {
//             logService.createLog("UPDATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", updateBillingRequest.toString(), "The billing not found.");
//             throw new ResourceNotFoundException("billingId", "id", billingId);
//         }

//         Billing billing = billingOptional.get();

//         // update the description, payment date and change status to paid
//         billing.setDescription(updateBillingRequest.getDescription());
//         billing.setPaymentDate(updateBillingRequest.getPaymentDate());
//         billing.setStatus(NetmonStatus.BillingStatus.PAID);

//         logService.createLog("UPDATE_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
//                 "[billingId=" + billingId + "]", updateBillingRequest.toString(), "");

//         // store the billing changes
//         return serviceBillingRepository.save(billing);
//     }

//     /**
//      * Set billing status to VOID.
//      * @param currentUser the user id who currently logged in
//      * @param billingId the unique billing number
//      * @param updateBillingRequest the billing new information object
//      * @return updated billing
//      */
//     public Billing voidBilling(UserPrincipal currentUser, Long billingId, ServiceBillingRequest updateBillingRequest) {

//         // find the billing by id
//         Optional<Billing> billingOptional = serviceBillingRepository.findById(billingId);
//         if (!billingOptional.isPresent()) {
//             logService.createLog("VOID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", updateBillingRequest.toString(), "The billing not found.");
//             throw new ResourceNotFoundException("billingId", "id", billingId);
//         }

//         Billing billing = billingOptional.get();

//         // update the description and change the status yo void
//         billing.setDescription(updateBillingRequest.getDescription());
//         billing.setStatus(NetmonStatus.BillingStatus.VOID);

//         logService.createLog("VOID_BILLING", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
//                 "[billingId=" + billingId + "]", updateBillingRequest.toString(), "");

//         return serviceBillingRepository.save(billing);
//     }

//     /**
//      * Call payment API, get order id and update billing info.
//      * @param currentUser the user id who currently logged in
//      * @param billingId the unique billing number
//      * @param callAPIBillingRequest the api information object to pay
//      * @return updated billing
//      */
//     public Billing updateOrderId(UserPrincipal currentUser, Long billingId,
//                                  CallAPIBillingRequest callAPIBillingRequest) {

//         // find the billing by id
//         Optional<Billing> billingOptional = serviceBillingRepository.findById(billingId);
//         if (!billingOptional.isPresent()) {
//             logService.createLog("UPDATE_BILLING_ORDERID", currentUser.getUsername(),
//                     NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", callAPIBillingRequest.toString(), "The billing not found.");
//             throw new ResourceNotFoundException("Billing", "id", billingId);
//         }

//         // get order id by calling api
//         UpdateBillingOrderIdResponse response =
//                 PaymentStatusService.callBillingAPIGetOrderID(callAPIBillingRequest);

//         if(response.getResult() != 0) {
//             logService.createLog("UPDATE_BILLING_ORDERID", currentUser.getUsername(),
//                     NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", callAPIBillingRequest.toString(),
//                     "BillingStatusService error: " + response.getMessage());
//             throw new BadRequestException(response.getMessage());
//         }

//         Billing billing = billingOptional.get();

//         // update order id and message
//         billing.setOrderId(response.getOrderID());
//         billing.setDescription(response.getMessage());

//         logService.createLog("UPDATE_BILLING_ORDERID", currentUser.getUsername(),
//                 NetmonStatus.LOG_STATUS.SUCCESS,
//                 "[billingId=" + billingId + "]", callAPIBillingRequest.toString(), "");

//         // store the billing changes
//         return serviceBillingRepository.save(billing);
//     }

//     /**
//      * Call payment API, get reference id and update billing info.
//      * @param currentUser the user id who currently logged in
//      * @param billingId the unique billing number
//      * @param callAPIBillingRequest the api information object to pay
//      * @return updated billing
//      */
//     public Billing updateReferenceId(UserPrincipal currentUser, Long billingId,
//                                  CallAPIBillingRequest callAPIBillingRequest) {

//         // find the billing by id
//         Optional<Billing> billingOptional = serviceBillingRepository.findById(billingId);
//         if (!billingOptional.isPresent()) {
//             logService.createLog("UPDATE_BILLING_REFERENCEID", currentUser.getUsername(),
//                     NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", callAPIBillingRequest.toString(), "The billing not found.");
//             throw new ResourceNotFoundException("Billing", "id", billingId);
//         }

//         // get reference id by calling an api
//         UpdateBillingReferenceIdResponse response =
//                 PaymentStatusService.callBillingAPIGetReferenceID(callAPIBillingRequest);

//         if(response.getResult() != 0) {
//             logService.createLog("UPDATE_BILLING_REFERENCEID", currentUser.getUsername(),
//                     NetmonStatus.LOG_STATUS.FAILED,
//                     "[billingId=" + billingId + "]", callAPIBillingRequest.toString(),
//                     "BillingStatusService error: " + response.getMessage());
//             throw new BadRequestException(response.getMessage());
//         }

//         Billing billing = billingOptional.get();

//         // update reference id and message and change the status to paid
//         billing.setReferenceId(response.getReferenceId());
//         billing.setDescription(response.getMessage());
//         billing.setStatus(NetmonStatus.BillingStatus.PAID);

//         logService.createLog("UPDATE_BILLING_REFERENCEID", currentUser.getUsername(),
//                 NetmonStatus.LOG_STATUS.SUCCESS,
//                 "[billingId=" + billingId + "]", callAPIBillingRequest.toString(), "");

//         // save the billing changes
//         return serviceBillingRepository.save(billing);
//     }


//     private void validatePageNumberAndSize(int page, int size) {
//         if(page < 0) {
//             throw new BadRequestException("Page number cannot be less than zero.");
//         }

//         if(size > AppConstants.MAX_PAGE_SIZE) {
//             throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
//         }
//     }

// }
