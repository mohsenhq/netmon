package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.OSType;
import org.redapps.netmon.payload.OSTypeRequest;
import org.redapps.netmon.payload.OSTypeResponse;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.repository.OsTypeRepository;
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
public class OSTypeService {

    private final OsTypeRepository osTypeRepository;
    private final LogService logService;

    @Autowired
    public OSTypeService(LogService logService, OsTypeRepository osTypeRepository) {
        this.logService = logService;
        this.osTypeRepository = osTypeRepository;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param osTypeRequest the osType information object
     * @return os type response
     */
    public OSType create(UserPrincipal currentUser, OSTypeRequest osTypeRequest) {

        // create a new os type object
        OSType osType = new OSType(osTypeRequest.getName(), osTypeRequest.getDescription());

        logService.createLog("CREATE_OS_TYPE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                osTypeRequest.toString(), "");

        // save the os type object
        return osTypeRepository.save(osType);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return os type responses page by page
     */
    public PagedResponse<OSTypeResponse> getOSTypes(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all os type and sorted by id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Page<OSType> osTypes = osTypeRepository.findAll(pageable);

        if(osTypes.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), osTypes.getNumber(),
                    osTypes.getSize(), osTypes.getTotalElements(), osTypes.getTotalPages(),
                    osTypes.isLast());
        }

        // store all os type into a list
        Vector<OSTypeResponse> osTypeResponses = new Vector<>(10);
        OSTypeResponse osTypeResponse;
        for (OSType osType : osTypes) {
                osTypeResponse = new OSTypeResponse(osType.getId(),
                        osType.getName(), osType.getDescription(), osType.isActive());
                osTypeResponses.add(osTypeResponse);
        }

        logService.createLog("GET_ALL_OS_TYPES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(osTypeResponses, osTypes.getNumber(),
                osTypes.getSize(), osTypes.getTotalElements(), osTypes.getTotalPages(),
                osTypes.isLast());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param osTypeId the unique osType number
     * @return os type response
     */
    public OSTypeResponse getOSTypeById(UserPrincipal currentUser, Long osTypeId) {

        // find os type by id
        Optional<OSType> osTypeOptional = osTypeRepository.findById(osTypeId);
        if(!osTypeOptional.isPresent()) {
            logService.createLog("GET_OS_TYPE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[osTypeId=" + osTypeId + "]", "", "The os-type not found.");
            throw new ResourceNotFoundException("OSType", "id", osTypeId);
        }
        OSType osType = osTypeOptional.get();

        logService.createLog("GET_OS_TYPE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[osTypeId=" + osTypeId + "]", "", "");

        // create a new os type response
        return new OSTypeResponse(osType.getId(),
                osType.getName(), osType.getDescription(), osType.isActive());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param osTypeId the unique osType number
     * @param osTypeRequest the osType information object
     * @return a new osType object
     */
    public OSType update(UserPrincipal currentUser, Long osTypeId, OSTypeRequest osTypeRequest) {

        // find the os type by id
        Optional<OSType> osTypeOptional = osTypeRepository.findById(osTypeId);
        if(!osTypeOptional.isPresent()) {
            logService.createLog("GET_OS_TYPE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[osTypeId=" + osTypeId + "]", osTypeRequest.toString(), "The os-type not found.");
            throw new ResourceNotFoundException("OSType", "id", osTypeId);
        }
        OSType osType = osTypeOptional.get();

        // update name and description
        osType.setName(osTypeRequest.getName());
        osType.setDescription(osTypeRequest.getDescription());

        logService.createLog("GET_OS_TYPE_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[osTypeId=" + osTypeId + "]", osTypeRequest.toString(), "");

        // store os type changes
        return osTypeRepository.save(osType);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    /**
     * @param osTypeId the unique osType number
     * @param currentUser the user id who currently logged in
     * @param active true / false
     * @return os type
     */
    public OSType active(Long osTypeId, UserPrincipal currentUser, boolean active) {

        // find os type by id
        Optional<OSType> vpsPlanOptional = osTypeRepository.findById(osTypeId);
        if (!vpsPlanOptional.isPresent()) {
            logService.createLog("(IN)ACTIVE_OS_TYPE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[osTypeId=" + osTypeId + "]", "", "The os type does not exists.");
            throw new ResourceNotFoundException("OSType", "osTypeId", osTypeId);
        }
        logService.createLog("(IN)ACTIVE_OS_TYPE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[osTypeId=" + osTypeId + "]", "", "");

        OSType osType = vpsPlanOptional.get();
        // set active parameter to true or false
        osType.setActive(active);

        // store os type changes
        return osTypeRepository.save(osType);
    }
}
