package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.ResourcePrice;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.payload.ResourcePriceRequest;
import org.redapps.netmon.payload.ResourcePriceResponse;
import org.redapps.netmon.repository.ResourcePriceRepository;
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
public class ResourcePriceService {

    private final ResourcePriceRepository ResourcePriceRepository;
    private final LogService logService;

    @Autowired
    public ResourcePriceService(LogService logService, ResourcePriceRepository ResourcePriceRepository) {
        this.logService = logService;
        this.ResourcePriceRepository = ResourcePriceRepository;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return resourcePrice responses page by page
     */
    public PagedResponse<ResourcePriceResponse> getResourcePrice(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // find all resourceprice
        Page<ResourcePrice> serviceResourcePrice = ResourcePriceRepository.findAll(pageable);

        if (serviceResourcePrice.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), serviceResourcePrice.getNumber(),
                    serviceResourcePrice.getSize(), serviceResourcePrice.getTotalElements(),
                    serviceResourcePrice.getTotalPages(), serviceResourcePrice.isLast());
        }

        // initial a list to response
        Vector<ResourcePriceResponse> serviceResourcePriceResponses = new Vector<>(10);
        ResourcePriceResponse ResourcePriceResponse;
        // store all resourceprice into the list
        for (ResourcePrice ResourcePrice : serviceResourcePrice) {
            ResourcePriceResponse = new ResourcePriceResponse(ResourcePrice.getId(), ResourcePrice.getCpuPrice(),
                    ResourcePrice.getRamPrice(), ResourcePrice.getDiskPrice(), ResourcePrice.getTrafficUploadPrice(),
                    ResourcePrice.getTrafficDownloadPrice(), ResourcePrice.getUnitSilverPrice(),
                    ResourcePrice.getUnitGoldPrice(), ResourcePrice.getIpPrice(), ResourcePrice.getDate());

            serviceResourcePriceResponses.add(ResourcePriceResponse);
        }

        logService.createLog("GET_VPS_RESOURCEPRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                "", "");

        return new PagedResponse<>(serviceResourcePriceResponses, serviceResourcePrice.getNumber(),
                serviceResourcePrice.getSize(), serviceResourcePrice.getTotalElements(),
                serviceResourcePrice.getTotalPages(), serviceResourcePrice.isLast());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page        the page number of the response (default value is 0)
     * @param size        the page size of each response (default value is 30)
     * @return resourcePrice responses page by page
     */
    public ResourcePriceResponse getResourcePriceLast(UserPrincipal currentUser) {

        ResourcePrice resourcePrice = ResourcePriceRepository.findTopByOrderByDateDesc();
       
        logService.createLog("GET_VPS_RESOURCEPRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                "", "");

        return new ResourcePriceResponse(resourcePrice.getId(), resourcePrice.getCpuPrice(),
        resourcePrice.getRamPrice(), resourcePrice.getDiskPrice(), resourcePrice.getTrafficUploadPrice(),
        resourcePrice.getTrafficDownloadPrice(), resourcePrice.getUnitSilverPrice(),
        resourcePrice.getUnitGoldPrice(), resourcePrice.getIpPrice(), resourcePrice.getDate());
    }

    /**
     * @param ResourcePriceRequest the resourceprice information object
     * @param currentUser          the user id who currently logged in
     * @return resourceprice response
     */
    public ResourcePrice create(ResourcePriceRequest ResourcePriceRequest, UserPrincipal currentUser) {

        // create a new resourceprice object
        ResourcePrice ResourcePrice = new ResourcePrice(ResourcePriceRequest.getCpuPrice(),
                ResourcePriceRequest.getRamPrice(), ResourcePriceRequest.getDiskPrice(),
                ResourcePriceRequest.getTrafficUploadPrice(), ResourcePriceRequest.getTrafficDownloadPrice(),
                ResourcePriceRequest.getUnitSilverPrice(), ResourcePriceRequest.getUnitGoldPrice(),
                ResourcePriceRequest.getIpPrice(), ResourcePriceRequest.getDate());

        logService.createLog("CREATE_VPS_RESOURCEPRICE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                ResourcePriceRequest.toString(), "");
        // save the resourceprice object
        return ResourcePriceRepository.save(ResourcePrice);
    }

    /**
     * @param resourcePriceId the unique resourceprice number
     * @param currentUser     the user id who currently logged in
     * @return resourceprice response
     */
    public ResourcePriceResponse getResourcePriceById(Long resourcePriceId, UserPrincipal currentUser) {

        // find resourceprice by id
        Optional<ResourcePrice> ResourcePriceOptional = ResourcePriceRepository.findById(resourcePriceId);
        if (!ResourcePriceOptional.isPresent()) {
            logService.createLog("GET_RESOURCEPRICE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[resourcePriceId=" + resourcePriceId + "]", "", "The vps resourceprice does not exists.");
            throw new ResourceNotFoundException("ResourcePrice", "ResourcePriceId", resourcePriceId);
        }

        ResourcePrice ResourcePrice = ResourcePriceOptional.get();
        logService.createLog("GET_RESOURCEPRICE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[resourcePriceId=" + resourcePriceId + "]", "", "");

        // create a resourceprice response
        return new ResourcePriceResponse(ResourcePrice.getId(), ResourcePrice.getCpuPrice(),
                ResourcePrice.getRamPrice(), ResourcePrice.getDiskPrice(), ResourcePrice.getTrafficUploadPrice(),
                ResourcePrice.getTrafficDownloadPrice(), ResourcePrice.getUnitSilverPrice(),
                ResourcePrice.getUnitGoldPrice(), ResourcePrice.getIpPrice(), ResourcePrice.getDate());
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
