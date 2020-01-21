package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.model.Ticket;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.payload.ServiceTicketRequest;
import org.redapps.netmon.payload.ServiceTicketResponse;
import org.redapps.netmon.repository.NetmonServiceRepository;
import org.redapps.netmon.repository.ServiceTicketRepository;
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
public class TicketService {

    private final NetmonServiceRepository netmonServiceRepository;
    private final ServiceTicketRepository serviceTicketRepository;
    private final LogService logService;

    @Autowired
    public TicketService(LogService logService, ServiceTicketRepository serviceTicketRepository, NetmonServiceRepository netmonServiceRepository) {
        this.logService = logService;
        this.serviceTicketRepository = serviceTicketRepository;
        this.netmonServiceRepository = netmonServiceRepository;
    }


    /**
     * @param serviceTicketRequest the ticket information object
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @return ticket response
     */
    public Ticket create(ServiceTicketRequest serviceTicketRequest, UserPrincipal currentUser , ServiceIdentity serviceId) {

        // find service
        NetmonService netmonService = netmonServiceRepository.getOne(serviceId);

        // create a new ticket object
        Ticket serviceTicket = new Ticket(serviceTicketRequest.getTitle(), serviceTicketRequest.getDescription(),
                serviceTicketRequest.getStatus(), netmonService);

        // assign service to ticket
        serviceTicket.setNetmonService(netmonService);

        logService.createLog("CREATE_TICKET", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", serviceTicketRequest.toString(), "");

        // store the ticket
        return serviceTicketRepository.save(serviceTicket);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param serviceId the unique service number
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return ticket responses page by page
     */
    public PagedResponse<ServiceTicketResponse> getServiceTickets(UserPrincipal currentUser,
                                                                  ServiceIdentity serviceId, int page, int size) {
        validatePageNumberAndSize(page, size);

        // find all tickets sorted by createdAt
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Ticket> serviceTickets = serviceTicketRepository.findAllByNetmonServiceId(serviceId, pageable);

        if(serviceTickets.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), serviceTickets.getNumber(),
                    serviceTickets.getSize(), serviceTickets.getTotalElements(),
                    serviceTickets.getTotalPages(), serviceTickets.isLast());
        }

        // store tickets into a list
        Vector<ServiceTicketResponse> serviceTicketResponses = new Vector<>(10);
        ServiceTicketResponse serviceTicketResponse;
        for (Ticket ticket : serviceTickets) {
                serviceTicketResponse = new ServiceTicketResponse(ticket.getId(), ticket.getTitle(),
                        ticket.getDescription(), ticket.getStatus());

                serviceTicketResponses.add(serviceTicketResponse);
        }

        logService.createLog("GET_ALL_TICKETS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[serviceId=" + serviceId + "]", "", "");

        return new PagedResponse<>(serviceTicketResponses, serviceTickets.getNumber(),
                serviceTickets.getSize(), serviceTickets.getTotalElements(), serviceTickets.getTotalPages(),
                serviceTickets.isLast());
    }

    /**
     * @param ticketId the unique ticket number
     * @param currentUser the user id who currently logged in
     * @return ticket response
     */
    public ServiceTicketResponse getServiceTicketById(Long ticketId, UserPrincipal currentUser) {

        // find the ticket by id
        Optional<Ticket> ticketOptional = serviceTicketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            logService.createLog("GET_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[ticketId=" + ticketId + "]", "", "This ticket does not exists.");
            throw new ResourceNotFoundException("Ticket", "ticketId", ticketId);
        }

        Ticket ticket = ticketOptional.get();
        logService.createLog("GET_TICKET_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[ticketId=" + ticketId + "]", "", "");

        // create a ticket response
        return new ServiceTicketResponse(ticket.getId(),
                ticket.getTitle(), ticket.getDescription(), ticket.getStatus());
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param ticketId the unique ticket number
     * @param response a description
     * @return ticket response
     */
    public Ticket setTicketResponse(UserPrincipal currentUser, Long ticketId, String response) {

        // find the ticket by id
        Optional<Ticket> ticketOptional = serviceTicketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            logService.createLog("SET_TICKET_RESPONSE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[ticketId=" + ticketId + "]", response, "This ticket does not exists.");
            throw new ResourceNotFoundException("Ticket", "ticketId", ticketId);
        }

        Ticket ticket = ticketOptional.get();
        // set ticket response
        ticket.setResponse(response);

        logService.createLog("SET_TICKET_RESPONSE", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[ticketId=" + ticketId + "]", response, "");
        return serviceTicketRepository.save(ticket);
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


