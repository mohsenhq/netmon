package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.model.PlanPrice;
import org.redapps.netmon.model.VpsPlan;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.payload.PlanPriceRequest;
import org.redapps.netmon.payload.PlanPriceResponse;
import org.redapps.netmon.repository.PlanPriceRepository;
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
import java.util.Vector;

@Service
public class PlanPriceService {

    private final PlanPriceRepository PlanPriceRepository;
    private final LogService logService;

    @Autowired
    public PlanPriceService(LogService logService, PlanPriceRepository PlanPriceRepository) {
        this.logService = logService;
        this.PlanPriceRepository = PlanPriceRepository;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return planPrice responses page by page
     */
    public PagedResponse<PlanPriceResponse> getPlanPrice(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // find all planprice
        Page<PlanPrice> servicePlanPrice = PlanPriceRepository.findAll(pageable);

        if (servicePlanPrice.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), servicePlanPrice.getNumber(),
                    servicePlanPrice.getSize(), servicePlanPrice.getTotalElements(),
                    servicePlanPrice.getTotalPages(), servicePlanPrice.isLast());
        }

        // initial a list to response
        Vector<PlanPriceResponse> servicePlanPriceResponses = new Vector<>(10);
        PlanPriceResponse PlanPriceResponse;
        // store all planprice into the list
        for (PlanPrice PlanPrice : servicePlanPrice) {
            PlanPriceResponse = new PlanPriceResponse(PlanPrice.getPlanId(), PlanPrice.getDate(),
                    PlanPrice.getPrice());

            servicePlanPriceResponses.add(PlanPriceResponse);
        }

        logService.createLog("GET_VPS_PLANPRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                "", "");

        return new PagedResponse<>(servicePlanPriceResponses, servicePlanPrice.getNumber(),
                servicePlanPrice.getSize(), servicePlanPrice.getTotalElements(),
                servicePlanPrice.getTotalPages(), servicePlanPrice.isLast());
    }

    // /**
    //  * @param currentUser the user id who currently logged in
    //  * @param page        the page number of the response (default value is 0)
    //  * @param size        the page size of each response (default value is 30)
    //  * @return planPrice responses page by page
    //  */
    // public PlanPriceResponse getPlanPriceLast(UserPrincipal currentUser) {

    //     PlanPrice planPrice = PlanPriceRepository.findTopByOrderByDateDesc();
       
    //     logService.createLog("GET_VPS_PLANPRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
    //             "", "");

    //     return new PlanPriceResponse(planPrice.getId(), planPrice.getCpuPrice(),
    //     planPrice.getRamPrice(), planPrice.getDiskPrice(), planPrice.getTrafficUploadPrice(),
    //     planPrice.getTrafficDownloadPrice(), planPrice.getUnitSilverPrice(),
    //     planPrice.getUnitGoldPrice(), planPrice.getIpPrice(), planPrice.getDate());
    // }

    /**
     * @param planPriceRequest the planprice information object
     * @param currentUser          the user id who currently logged in
     * @return planprice response
     */
    public PlanPrice create(VpsPlan plan, PlanPriceRequest planPriceRequest, UserPrincipal currentUser) {

        // create a new planprice object
        PlanPrice PlanPrice = new PlanPrice(plan, planPriceRequest.getDate(),planPriceRequest.getPrice());

        logService.createLog("CREATE_VPS_PLANPRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                planPriceRequest.toString(), "");
        // save the planprice object
        return PlanPriceRepository.save(PlanPrice);
    }

    /**
     * @param planId the unique planprice number
     * @param currentUser the user id who currently logged in
     * @return planprice response
     */
    public PagedResponse<PlanPriceResponse> getPlanPriceVpsByVpsPlan(VpsPlan planId, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // find planprice by id
        Page<PlanPrice> servicePlanPrice = PlanPriceRepository.findByPlanId(planId, pageable);

        if (servicePlanPrice.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), servicePlanPrice.getNumber(),
                    servicePlanPrice.getSize(), servicePlanPrice.getTotalElements(),
                    servicePlanPrice.getTotalPages(), servicePlanPrice.isLast());
        }

        // initial a list to response
        Vector<PlanPriceResponse> servicePlanPriceResponses = new Vector<>(10);
        PlanPriceResponse PlanPriceResponse;
        // store all Planprice into the list
        for (PlanPrice planPrice : servicePlanPrice) {
            PlanPriceResponse = new PlanPriceResponse(planPrice.getPlanId(), planPrice.getDate(), planPrice.getPrice());

            servicePlanPriceResponses.add(PlanPriceResponse);
        }

        logService.createLog("GET_VPS_Plan_PRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                "", "");

        return new PagedResponse<>(servicePlanPriceResponses, servicePlanPrice.getNumber(),
                servicePlanPrice.getSize(), servicePlanPrice.getTotalElements(),
                servicePlanPrice.getTotalPages(), servicePlanPrice.isLast());


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
