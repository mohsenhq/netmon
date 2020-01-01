package org.redapps.netmon.controller.admin;

import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.User;

import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.UserRepository;
import org.redapps.netmon.security.CurrentUser;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.service.LogService;
import org.redapps.netmon.service.UserManagementService;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserManagementService userManagementService;
    private final LogService logService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserManagementService userManagementService, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, LogService logService) {
        this.userManagementService = userManagementService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    /**
     * getting list of all urls for the admin role
     * @return an array of urls
     */
    @GetMapping("/urls")
    @PreAuthorize("hasRole('ADMIN')")
    public String[] getManagerUrls() {

        return new String[]{"GET: /api/admin/urls",
                "GET: /api/admin/users/all",
                "POST: /api/admin/users/new",
                "DELETE: /api/admin/users/delete/{userId}",
                "PUT: /api/admin/users/update/{userId}",
                "PUT: /api/admin/users/active/{userId}",
                "PUT: /api/admin/users/inactive/{userId}",
                "PUT: /api/admin/users/resetpassword",
                "GET: /api/admin/users/{username}",
                "GET: /api/admin/logs/all",
                "GET: /api/admin/log/{logId}"};
    }

    /**
     * getting list of all users
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return user responses page by page
     */
    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<UserResponse> getAllUsers(@CurrentUser UserPrincipal currentUser,
                                                   @RequestParam(value = "page",
                                                           defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                   @RequestParam(value = "size",
                                                           defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return userManagementService.getAllUsers(currentUser, page, size);
    }

    /**
     * creating a new user
     * @param currentUser the user id who currently logged in
     * @param createUserRequest an object of user information
     * @return OK response or report error
     */
    @PostMapping("/users/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@CurrentUser UserPrincipal currentUser,
                                     @Valid @RequestBody CreateUserRequest createUserRequest) {

        return userManagementService.createUser(createUserRequest);
    }

    /**
     * deleting an user
     * @param currentUser the user id who currently logged in
     * @param userId the unique number of an user
     * @return OK response or report error
     */
    @DeleteMapping("/users/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId) {

        return userManagementService.deleteUser(currentUser, userId);
    }

    /**
     * updating info (name, mobile, ...) of user
     * @param updateUserRequest an object of user information
     * @param currentUser the user id who currently logged in
     * @param userId the unique number of an user
     * @return OK response or report error
     */
    @PutMapping("/users/update/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest,
                                        @CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId) {

        return userManagementService.updateUser(currentUser, userId, updateUserRequest);
    }

    /**
     *
     * @param currentUser the user id who currently logged in
     * @param userId the unique number of an user
     * @return OK response or report error
     */
    @PutMapping("/users/active/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activeUser(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId) {

        return userManagementService.activeUser(currentUser, userId, true);
    }

    /**
     *
     * @param currentUser the user id who currently logged in
     * @param userId the unique number of an user
     * @return OK response or report error
     */
    @PutMapping("/users/inactive/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> inactiveUser(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long userId) {

        return userManagementService.activeUser(currentUser, userId, false);
    }

    /**
     *
     * @param resetPasswordRequest an object of new password
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PutMapping("/users/resetpassword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AdminResetPasswordRequest resetPasswordRequest,
                                           @CurrentUser UserPrincipal currentUser) {


        Optional<User> userOptional = userRepository.findByUsername(resetPasswordRequest.getUsername());
        if(!userOptional.isPresent()){
            logger.error("User not found [username: " + resetPasswordRequest.getUsername() + "].");

            logService.createLog("RESET_PASSWORD", resetPasswordRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", resetPasswordRequest.toString(), "This user not found.");
            throw new UsernameNotFoundException("This user not found.");
        }

        User user = userOptional.get();
        logService.createLog("RESET_PASSWORD", resetPasswordRequest.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "", resetPasswordRequest.toString(), "");

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/resetpassword")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Password has been reset successfully"));
    }

    /**
     *
     * @param currentUser the user id who currently logged in
     * @param username the username of one user
     * @return user profile
     */
    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfile getUserProfile(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable(value = "username") String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(!userOptional.isPresent()){
            logger.error("User not found [username: " + username + "].");

            logService.createLog("GET_USER_PROFILE", username, NetmonStatus.LOG_STATUS.FAILED, "[username=" + username + "]", "",
                    "This user not found.");
            throw new ResourceNotFoundException("User", "username", username);
        }

        User user = userOptional.get();

        logService.createLog("GET_USER_PROFILE", username, NetmonStatus.LOG_STATUS.SUCCESS,
                "[username=" + username + "]", "", "");

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
    }

    /**
     * getting list of all logs
     * @param currentUser the user id who currently logged in
     * @param startDate the start date to get logs
     * @param endDate the end date to get logs
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 100)
     * @return log responses page by page
     */
    @GetMapping("/logs/all")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<LogResponse> getLogs(@CurrentUser UserPrincipal currentUser,
                                              @RequestParam(required = false, value = "startDate")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(required = false, value = "endDate")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                              @RequestParam(value = "page",
                                                      defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                              @RequestParam(value = "size",
                                                      defaultValue = AppConstants.DEFAULT_LOGS_PAGE_SIZE) int size) {

        logService.createLog("GET_LOGS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[startDate=" + startDate + ",endDate=" + endDate + "]", "", "");

        LocalDateTime sDate = null;
        LocalDateTime eDate = null;
        if(startDate != null)
            sDate = startDate.atStartOfDay();
        if(endDate != null)
            eDate = endDate.atTime(LocalTime.MAX);
        return logService.getLogs(sDate, eDate, page, size);

    }

    /**
     * getting log info by id
     * @param currentUser the user id who currently logged in
     * @param logId the unique log number
     * @return log response
     */
    @GetMapping("/log/{logId}")
    @PreAuthorize("hasRole('ADMIN')")
    public LogResponse getLogById(@CurrentUser UserPrincipal currentUser,
                                  @PathVariable Long logId) {

        logService.createLog("GET_LOG_BY_ID", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[logId=" + logId + "]", "", "");

        return logService.getLogById(logId);
    }
}
