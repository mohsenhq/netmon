package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.User;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.payload.CompanyRequest;
import org.redapps.netmon.payload.CompanyResponse;
import org.redapps.netmon.repository.CompanyRepository;
import org.redapps.netmon.repository.UserRepository;

import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.model.Company;
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
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final LogService logService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, LogService logService) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.logService = logService;
    }

    /**
     * Find list of user company
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0) page number
     * @param size the page size of each response (default value is 30) page size
     * @return list of companies
     */
    public PagedResponse<CompanyResponse> getAllCustomerCompanies(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all companies by customer id
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Company> companies = companyRepository.findAllByUserId(currentUser.getId(), pageable);

        if(companies.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), companies.getNumber(),
                    companies.getSize(), companies.getTotalElements(), companies.getTotalPages(), companies.isLast());
        }

        // store companies into a list
        Vector<CompanyResponse> companyResponses = new Vector<>(size);
        for (Company company : companies) {
            companyResponses.add(new CompanyResponse(company.getId(), company.getName(), company.getType(),
                    company.getNationalID(), company.getFinancialID(),
                    company.getRegistrationID(), company.getTelNumber()));
        }

        logService.createLog("GET_ALL_COMPANIES", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(companyResponses, companies.getNumber(),
                companies.getSize(), companies.getTotalElements(), companies.getTotalPages(), companies.isLast());
    }

    /**
     * Create a new company.
     * @param companyRequest the company information object
     * @param currentUser the user id who currently logged in
     * @return a new company object
     */
    public Company createCompany(CompanyRequest companyRequest, UserPrincipal currentUser) {

        // find the user by id
        User user = userRepository.getOne(currentUser.getId());

        // create a new company object
        Company company = new Company(companyRequest.getName(), companyRequest.getNationalID(),
                companyRequest.getFinancialID(), companyRequest.getRegistrationID(),
                companyRequest.getTelNumber(), companyRequest.getType(), user);

        logService.createLog("CREATE_COMPANY", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "", companyRequest.toString(), "");

        // store the new object
        return companyRepository.save(company);
    }

    /**
     * Get company info by id.
     * @param companyId the unique company number
     * @param currentUser the user id who currently logged in
     * @return a company response
     */
    public CompanyResponse getCompanyById(Long companyId, UserPrincipal currentUser) {

        // find company by id
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (!optionalCompany.isPresent()) {
            logService.createLog("GET_COMPANY_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[companyId=" + companyId + "]", "", "The company does not exists.");
            throw new ResourceNotFoundException("Company", "companyId", companyId);
        }

        logService.createLog("GET_COMPANY_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[companyId=" + companyId + "]", "", "");

        Company company = optionalCompany.get();

        // create a new company response
        return new CompanyResponse(company.getId(), company.getName(), company.getType(),
                company.getFinancialID(), company.getNationalID(), company.getRegistrationID(),
                company.getTelNumber());
    }

    /**
     * Find all companies.
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0) page number
     * @param size the page size of each response (default value is 30) page size
     * @return list all companies.
     */
    public PagedResponse<CompanyResponse> getAllCompanies(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all companies
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Company> companies = companyRepository.findAll(pageable);

        if(companies.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), companies.getNumber(),
                    companies.getSize(), companies.getTotalElements(), companies.getTotalPages(), companies.isLast());
        }

        // store the companies into a list
        Vector<CompanyResponse> companyResponses = new Vector<>(10);
        for (Company company : companies) {
            companyResponses.add(new CompanyResponse(company.getId(), company.getName(), company.getType(),
                        company.getNationalID(), company.getFinancialID(),
                        company.getRegistrationID(), company.getTelNumber()));

        }

        logService.createLog("GET_ALL_COMPANY", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");
        return new PagedResponse<>(companyResponses, companies.getNumber(),
                companies.getSize(), companies.getTotalElements(), companies.getTotalPages(), companies.isLast());
    }


    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
