package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.TechnicalPerson;
import org.redapps.netmon.model.User;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.payload.TechnicalPersonRequest;
import org.redapps.netmon.payload.TechnicalPersonResponse;
import org.redapps.netmon.repository.TechnicalPersonRepository;
import org.redapps.netmon.repository.UserRepository;
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
public class TechnicalPersonService {

    private final TechnicalPersonRepository technicalPersonRepository;
    private final UserRepository userRepository;
    private final LogService logService;

    @Autowired
    public TechnicalPersonService(UserRepository userRepository, LogService logService, TechnicalPersonRepository technicalPersonRepository) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.technicalPersonRepository = technicalPersonRepository;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param technicalPersonRequest the technicalPerson information object
     * @return technical person response
     */
    public TechnicalPerson create(UserPrincipal currentUser, TechnicalPersonRequest technicalPersonRequest) {

        // find user by id
        User user = userRepository.getOne(currentUser.getId());

        // create a new technical person object
        TechnicalPerson technicalPerson = new TechnicalPerson(technicalPersonRequest.getName(),
                technicalPersonRequest.getEmail(), technicalPersonRequest.getTelNumber(),
                technicalPersonRequest.getMobile(), technicalPersonRequest.getNationalId(), user);

        logService.createLog("CREATE_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "", technicalPersonRequest.toString(), "");

        // save the technical person
        return technicalPersonRepository.save(technicalPerson);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return technical person responses page by page
     */
    public PagedResponse<TechnicalPersonResponse> getTechnicalPersons(UserPrincipal currentUser,
                                                                      int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all technical person for a user and sorted by createdAt
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<TechnicalPerson> technicalPersons = technicalPersonRepository.findAllByUserId(currentUser.getId(), pageable);

        if (technicalPersons.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), technicalPersons.getNumber(),
                    technicalPersons.getSize(), technicalPersons.getTotalElements(), technicalPersons.getTotalPages(),
                    technicalPersons.isLast());
        }

        // store technical persons into a list
        Vector<TechnicalPersonResponse> technicalPersonResponses = new Vector<>(10);
        TechnicalPersonResponse technicalPersonResponse;
        for (TechnicalPerson technicalPerson : technicalPersons) {
            technicalPersonResponse = new TechnicalPersonResponse(technicalPerson.getId(),
                    technicalPerson.getName(), technicalPerson.getEmail(), technicalPerson.getTelNumber(),
                    technicalPerson.getMobile(), technicalPerson.getNationalId());

            technicalPersonResponses.add(technicalPersonResponse);
        }

        logService.createLog("GET_ALL_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(technicalPersonResponses, technicalPersons.getNumber(),
                technicalPersons.getSize(), technicalPersons.getTotalElements(), technicalPersons.getTotalPages(),
                technicalPersons.isLast());
    }

    /**
     * @param technicalPersonId the unique technicalPerson number
     * @param currentUser the user id who currently logged in
     * @return technical person response
     */
    public TechnicalPersonResponse getTechnicalPersonById(Long technicalPersonId, UserPrincipal currentUser) {

        // find a technical person by id
        Optional<TechnicalPerson> technicalPersonOptional = technicalPersonRepository.findByIdAndUserId(technicalPersonId, currentUser.getId());
        if (!technicalPersonOptional.isPresent()) {
            logService.createLog("GET_TECHNICAL_PERSON_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[technicalPersonId=" + technicalPersonId + "]", "", "This technical person does not exists.");
            throw new ResourceNotFoundException("technicalPersonId", "id", technicalPersonId);
        }

        logService.createLog("GET_TECHNICAL_PERSON_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[technicalPersonId=" + technicalPersonId + "]", "", "");

        TechnicalPerson technicalPerson = technicalPersonOptional.get();

        // create a new technical person response object
        return new TechnicalPersonResponse(technicalPerson.getId(),
                technicalPerson.getName(), technicalPerson.getEmail(), technicalPerson.getTelNumber(),
                technicalPerson.getMobile(), technicalPerson.getNationalId());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param technicalPersonId the unique technicalPerson number
     * @param technicalPersonRequest the technicalPerson information object
     * @return technical person
     */
    public TechnicalPerson update(UserPrincipal currentUser, Long technicalPersonId,
                                  TechnicalPersonRequest technicalPersonRequest) {

        // find the technical person by id
        Optional<TechnicalPerson> technicalPersonOptional = technicalPersonRepository.findById(technicalPersonId);
        if (!technicalPersonOptional.isPresent()) {
            logService.createLog("GET_TECHNICAL_PERSON_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[technicalPersonId=" + technicalPersonId + "]", technicalPersonRequest.toString(),
                    "This technical person does not exists.");
            throw new ResourceNotFoundException("technicalPersonId", "id", technicalPersonId);
        }
        TechnicalPerson technicalPerson = technicalPersonOptional.get();

        // update name, email, mobile and tel number
        technicalPerson.setName(technicalPersonRequest.getName());
        technicalPerson.setEmail(technicalPersonRequest.getEmail());
        technicalPerson.setMobile(technicalPersonRequest.getMobile());
        technicalPerson.setTelNumber(technicalPersonRequest.getTelNumber());

        // store technical person changes
        return technicalPersonRepository.save(technicalPerson);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param technicalPersonId the unique technicalPerson number
     */
    public void delete(UserPrincipal currentUser, Long technicalPersonId) {

        // find the technical person by user
        if (!technicalPersonRepository.existsById(technicalPersonId)) {
            logService.createLog("DELETE_TECHNICAL_PERSON", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[technicalPersonId=" + technicalPersonId + "]", "", "This technical person does not exists.");
            throw new ResourceNotFoundException("TechnicalPerson", "technicalPersonId", technicalPersonId);
        }

        // delete the technical person
        technicalPersonRepository.deleteById(technicalPersonId);
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
