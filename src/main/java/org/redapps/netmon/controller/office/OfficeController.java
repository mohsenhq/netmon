package org.redapps.netmon.controller.office;

import org.redapps.netmon.payload.*;
import org.redapps.netmon.security.CurrentUser;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.service.*;
import org.redapps.netmon.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    private final CompanyService companyService;

    @Autowired
    public OfficeController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * getting list of all urls for the office role
     * @return an array of urls
     */
    @GetMapping("/urls")
    @PreAuthorize("hasRole('OFFICE')")
    public String[] getOfficeUrls() {

        return new String[]{"GET: /api/office/urls",
                "GET: /api/office/customers/all",
                "GET: /api/office/customers/{customerId}",
                "GET: /api/office/companies/all",
                "GET: /api/office/companies/{companyId}",
                "GET: /api/office/colocations/all",
                "GET: /api/office/colocations/{colocationId}",
                "GET: /api/office/customers/{customerId}/colocations/{colocationId}/billings/all",
                "GET: /api/office/customers/{customerId}/colocations/{colocationId}/billings/{billingId}",
                "PUT: /api/office/customers/{customerId}/colocations/{colocationId}/billings/{billingId}/update",
                "GET: /api/office/customers/{customerId}/colocations/{colocationId}/billing/calculate}",
                "GET: /api/office/customers/{customerId}/colocations/{colocationId}/billings/lastPaid",
                "GET: /api/office/vps/all",
                "GET: /api/office/vps/{vpsId}",
                "GET: /api/office/customers/{customerId}/vps/{vpsId}/billings/all",
                "GET: /api/office/customers/{customerId}/vps/{vpsId}/billings/{billingId}",
                "PUT: /api/office/customers/{customerId}/vps/{vpsId}/billings/{billingId}/update",
                "GET: /api/office/customers/{customerId}/vps/{vpsId}/billing/calculate}",
                "GET: /api/office/customers/{customerId}/vps/{vpsId}/billings/lastPaid",};
    }

    /**
     * getting a company info by id
     * @param currentUser the user id who currently logged in
     * @param companyId the unique company number
     * @return company response
     */
    @GetMapping("/companies/{companyId}")
    @PreAuthorize("hasRole('OFFICE')")
    public CompanyResponse getCompanyById(@CurrentUser UserPrincipal currentUser,
                                          @PathVariable Long companyId) {

        return companyService.getCompanyById(companyId, currentUser);
    }

    /**
     * getting list of all companies
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return company responses page by page
     */
    @GetMapping("/companies/all")
    @PreAuthorize("hasRole('OFFICE')")
    public PagedResponse<CompanyResponse> getAllCompanies(@CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page",
                                                                  defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size",
                                                                  defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return companyService.getAllCompanies(currentUser, page, size);
    }
}
