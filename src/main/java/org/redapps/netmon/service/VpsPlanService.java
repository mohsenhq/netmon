package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.VpsPlan;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.payload.VpsPlanRequest;
import org.redapps.netmon.payload.VpsPlanResponse;
import org.redapps.netmon.repository.VpsPlanRepository;
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
public class VpsPlanService {

    private final VpsPlanRepository vpsPlanRepository;
    private final LogService logService;

    @Autowired
    public VpsPlanService(LogService logService, VpsPlanRepository vpsPlanRepository) {
        this.logService = logService;
        this.vpsPlanRepository = vpsPlanRepository;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return plan responses page by page
     */
    public PagedResponse<VpsPlanResponse> getPlans(UserPrincipal currentUser,
                                                   int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // find all plans
        Page<VpsPlan> servicePlans = vpsPlanRepository.findAll(pageable);

        if (servicePlans.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), servicePlans.getNumber(),
                    servicePlans.getSize(), servicePlans.getTotalElements(), servicePlans.getTotalPages(), servicePlans.isLast());
        }

        // initial a list to response
        Vector<VpsPlanResponse> servicePlanResponses = new Vector<>(10);
        VpsPlanResponse vpsPlanResponse;
        // store all plans into the list
        for (VpsPlan vpsPlan : servicePlans) {
            vpsPlanResponse = new VpsPlanResponse(vpsPlan.getId(), vpsPlan.getName(), vpsPlan.getRam(),
                    vpsPlan.getCpu(), vpsPlan.getMaxTraffic(), vpsPlan.getDisk(), vpsPlan.getMonthlyPrice(),
                    vpsPlan.getFreeTraffic(), vpsPlan.isActive());

            servicePlanResponses.add(vpsPlanResponse);
        }

        logService.createLog("GET_VPS_PLANS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(servicePlanResponses, servicePlans.getNumber(),
                servicePlans.getSize(), servicePlans.getTotalElements(),
                servicePlans.getTotalPages(), servicePlans.isLast());
    }

    /**
     * @param vpsPlanRequest the plan information object
     * @param currentUser the user id who currently logged in
     * @return plan response
     */
    public VpsPlan create(VpsPlanRequest vpsPlanRequest, UserPrincipal currentUser) {

        // create a new plan object
        VpsPlan vpsPlan = new VpsPlan(vpsPlanRequest.getName(), vpsPlanRequest.getRam(),
                vpsPlanRequest.getCpu(), vpsPlanRequest.getMaxTraffic(), vpsPlanRequest.getDisk(),
                vpsPlanRequest.getMonthlyPrice(), vpsPlanRequest.getFreeTraffic());

        logService.createLog("CREATE_VPS_PLAN", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "", vpsPlanRequest.toString(), "");
        // save the plan object
        return vpsPlanRepository.save(vpsPlan);
    }

    /**
     * @param planId the unique plan number
     * @param currentUser the user id who currently logged in
     * @param active true / false
     * @return plan response
     */
    public VpsPlan active(Long planId, UserPrincipal currentUser, boolean active) {

        // find plan by id
       Optional<VpsPlan> vpsPlanOptional = vpsPlanRepository.findById(planId);
        if (!vpsPlanOptional.isPresent()) {
            logService.createLog("(IN)ACTIVE_VPS_PLAN", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[planId=" + planId + "]", "", "The vps plan does not exists.");
            throw new ResourceNotFoundException("VpsPlan", "vpsPlanId", planId);
        }
        logService.createLog("(IN)ACTIVE_VPS_PLAN", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[planId=" + planId + "]", "", "");

        VpsPlan vpsPlan = vpsPlanOptional.get();
        // set active parameter to true or false
        vpsPlan.setActive(active);
        // store plan changes
        return vpsPlanRepository.save(vpsPlan);
    }

    /**
     * @param planId the unique plan number
     * @param currentUser the user id who currently logged in
     * @return plan response
     */
    public VpsPlanResponse getPlanById(Long planId, UserPrincipal currentUser) {

        // find plan by id
        Optional<VpsPlan> vpsPlanOptional = vpsPlanRepository.findById(planId);
        if (!vpsPlanOptional.isPresent()) {
            logService.createLog("GET_PLAN_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[planId=" + planId + "]", "", "The vps plan does not exists.");
            throw new ResourceNotFoundException("VpsPlan", "vpsPlanId", planId);
        }

        VpsPlan vpsPlan = vpsPlanOptional.get();
        logService.createLog("GET_PLAN_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[planId=" + planId + "]", "", "");

        // create a plan response
        return new VpsPlanResponse(vpsPlan.getId(), vpsPlan.getName(),
                vpsPlan.getRam(), vpsPlan.getCpu(), vpsPlan.getMaxTraffic(), vpsPlan.getDisk(),
                vpsPlan.getMonthlyPrice(), vpsPlan.getFreeTraffic(), vpsPlan.isActive());
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
