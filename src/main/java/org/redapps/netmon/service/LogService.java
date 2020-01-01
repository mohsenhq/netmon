package org.redapps.netmon.service;

import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.Log;
import org.redapps.netmon.payload.LogResponse;
import org.redapps.netmon.payload.PagedResponse;
import org.redapps.netmon.repository.LogRepository;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Vector;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * @param action SUCCESS / FAILED
     * @param user username
     * @param status failed or success
     * @param requestParameters all request parameters (query and header)
     * @param bodyParameters the parameters within json
     * @param description an explanation of the action
     * @return log
     */
    public Log createLog(String action, String user, NetmonStatus.LOG_STATUS status, String requestParameters, String bodyParameters,
                         String description) {

        logger.info("Action: " + action + ", Date: " + LocalDateTime.now() + ", user: " + user + ", status: "
                + status + ", requestParameters: " + requestParameters + ", bodyParameters: " +
                bodyParameters +", description: " + description);

        Log log = new Log(action, LocalDateTime.now(), user, status, requestParameters,
                bodyParameters, description);
        return logRepository.save(log);
    }

    /**
     * @param startDate the start date to get logs
     * @param endDate the end date to get logs
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 100)
     * @return log responses page by page
     */
    public PagedResponse<LogResponse> getLogs(LocalDateTime startDate,
                                              LocalDateTime endDate, int page, int size) {
        validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Vector<LogResponse> logResponses = new Vector<>(10);
        LogResponse logResponse;
        Page<Log> logs;

        // the start and end date is null: find all logs
        if (startDate == null && endDate == null) {
            logs = logRepository.findAll(pageable);
            if (logs.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), logs.getNumber(),
                        logs.getSize(), logs.getTotalElements(), logs.getTotalPages(),
                        logs.isLast());
            }

            for (Log log : logs) {
                logResponse = new LogResponse(log.getId(), log.getAction(), log.getDate(),
                        log.getUser(), log.getStatus(), log.getDetails());

                logResponses.add(logResponse);
            }
            // the start date is null: find all logs before the end date
        } else if (startDate == null) {
            logs = logRepository.findAllByDateBefore(endDate.plusDays(1), pageable);
            if (logs.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), logs.getNumber(),
                        logs.getSize(), logs.getTotalElements(), logs.getTotalPages(),
                        logs.isLast());
            }

            // store logs into a list
            for (Log log : logs) {
                logResponse = new LogResponse(log.getId(), log.getAction(), log.getDate(),
                        log.getUser(), log.getStatus(), log.getDetails());
                logResponses.add(logResponse);
            }
            // the end date is null: find all logs after the start date
        } else if (endDate == null) {
            logs = logRepository.findAllByDateAfter(startDate.minusDays(1), pageable);
            if (logs.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), logs.getNumber(),
                        logs.getSize(), logs.getTotalElements(), logs.getTotalPages(),
                        logs.isLast());
            }

            // store logs into a list
            for (Log log : logs) {
                logResponse = new LogResponse(log.getId(), log.getAction(), log.getDate(),
                        log.getUser(), log.getStatus(), log.getDetails());
                logResponses.add(logResponse);
            }
        } else {

            logs = logRepository.findAllByDateBetween(startDate, endDate, pageable);
            if (logs.getNumberOfElements() == 0) {
                return new PagedResponse<>(Collections.emptyList(), logs.getNumber(),
                        logs.getSize(), logs.getTotalElements(), logs.getTotalPages(),
                        logs.isLast());
            }

            // store logs into a list
            for (Log log : logs) {
                logResponse = new LogResponse(log.getId(), log.getAction(), log.getDate(),
                        log.getUser(), log.getStatus(), log.getDetails());
                logResponses.add(logResponse);
            }
        }

        return new PagedResponse<>(logResponses, logs.getNumber(),
                logs.getSize(), logs.getTotalElements(), logs.getTotalPages(),
                logs.isLast());
    }

    /**
     * @param logId the unique log number
     * @return log response
     */
    public LogResponse getLogById(Long logId) {

        // find the log by id
        Log log = logRepository.findById(logId).orElseThrow(
                () -> new ResourceNotFoundException("logId", "id", logId));

        return new LogResponse(log.getId(), log.getAction(), log.getDate(),
                log.getUser(), log.getStatus(), log.getDetails());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_LOGS_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_LOGS_PAGE_SIZE);
        }
    }
}
