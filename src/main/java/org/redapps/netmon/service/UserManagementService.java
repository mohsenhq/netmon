package org.redapps.netmon.service;

import org.redapps.netmon.exception.AppException;
import org.redapps.netmon.exception.BadRequestException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.Company;
import org.redapps.netmon.model.Role;
import org.redapps.netmon.model.RoleName;
import org.redapps.netmon.model.User;
import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.CompanyRepository;
import org.redapps.netmon.repository.RoleRepository;
import org.redapps.netmon.repository.UserRepository;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.redapps.netmon.util.SMSSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
public class UserManagementService {

    private static boolean emailIsEnabled;
    private static boolean smsIsEnabled;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;
    private final CompanyRepository companyRepository;

    @Autowired
    public UserManagementService(LogService logService, RoleRepository roleRepository,
                                 UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 CompanyRepository companyRepository) {
        this.logService = logService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    // read values from configuration file
    @Value("${email.enabled}")
    public void setEnabledEmail(boolean value) {
        emailIsEnabled = value;
    }

    @Value("${sms.enabled}")
    public void setEnabledSMS(boolean value) {
        smsIsEnabled = value;
    }

    /**
     * @param createUserRequest an object of user information
     * @return OK response or report error
     */
    public ResponseEntity<?> createUser(CreateUserRequest createUserRequest){

        // checking the username is unique
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // create a new user object
        User user = new User(createUserRequest.getName(), createUserRequest.getUsername(),
                createUserRequest.getNationalID(), createUserRequest.getEmail(),
                createUserRequest.getMobile(),createUserRequest.getTelNumber(),
                createUserRequest.getPassword(), "00000");

        // user is activate by default
        user.setActive(true);
        // encode password and add to object
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set user roles
        Role userRole = roleRepository.findByName(RoleName.fromValue(createUserRequest.getUser_role()))
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        // save user
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        String response = SMSSender.sendMessage(user.getMobile(), user.getEmail() , "Your User has been created.",
                smsIsEnabled, emailIsEnabled);

        logService.createLog("CREATE_USER", createUserRequest.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "",
                createUserRequest.toString(), "Send message response: " + response);

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return user responses page by page
     */
    public PagedResponse<UserResponse> getAllUsers(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // retrieve users
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<User> users = userRepository.findAll(pageable);

        if(users.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }

        // store all users into a list
        Vector<UserResponse> userResponses = createUserResponse(users);

        logService.createLog("GET_ALL_USERS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(userResponses, users.getNumber(),
                users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }

    /**
     * @param users list of users
     * @return list of user responses
     */
    private Vector<UserResponse> createUserResponse(Page<User> users){
        Vector<UserResponse> userResponses = new Vector<>(10);

        UserResponse userResponse;
        for (User user : users) {
            userResponse = new UserResponse(user.getId(), user.getName(), user.getUsername(),
                    user.getNationalID(), user.getEmail(), user.getMobile(), user.getTelNumber(),
                    user.isActive(), user.getCreatedAt(), user.getLastAccess(), user.getRoles());

            Optional<Company> companyOptional = companyRepository.findByUserId(user.getId());
            if(companyOptional.isPresent()){
                userResponse.setCompany(companyOptional.get());
            }
            userResponses.add(userResponse);
        }

        return userResponses;
    }

    /**
     * @param customerId the customer unique number who requested the service
     * @param currentUser the user id who currently logged in
     * @return user response
     */
    public UserResponse getCustomerById(Long customerId, UserPrincipal currentUser) {

        Optional<User> userOptional = userRepository.findById(customerId);
        // user should be available
        if(!userOptional.isPresent()){
            logService.createLog("GET_USER_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + "]", "", "This customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        User user = userOptional.get();
        // user role should be customer
        if(!user.isCustomer()){
            logService.createLog("GET_USER_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[customerId=" + customerId + "]", "", "This customer does not exists.");
            throw new ResourceNotFoundException("Customer", "customerId", customerId);
        }

        logService.createLog("GET_USER_INFO", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[customerId=" + customerId + "]", "", "");

        // create the response
        UserResponse userResponse =  new UserResponse(user.getId(), user.getName(), user.getUsername(),
                user.getNationalID(), user.getEmail(), user.getMobile(), user.getTelNumber(),
                user.isActive(), user.getCreatedAt(), user.getLastAccess(), user.getRoles());

        Optional<Company> companyOptional = companyRepository.findByUserId(user.getId());
        if(companyOptional.isPresent()){
            userResponse.setCompany(companyOptional.get());
        }

        return userResponse;
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param customerId the customer unique number who requested the service
     * @return OK response or report error
     */
    public ResponseEntity<?> deleteUser(UserPrincipal currentUser, Long customerId){

        // user should be available
        if (!userRepository.existsById(customerId)) {
            logService.createLog("DELETE_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[userId=" + customerId + "]", "", "This user does not exists.");
            return new ResponseEntity<>(new ApiResponse(false, "This user does not exists."),
                    HttpStatus.BAD_REQUEST);
        }

        logService.createLog("DELETE_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[userId=" + customerId + "]", "", "");

        // delete user by id
        userRepository.deleteById(customerId);
        return new ResponseEntity<>("The user deleted successfully."
                , new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param userId the unique number of an user
     * @param updateUserRequest the user information object
     * @return OK response or report error
     */
    public ResponseEntity<?> updateUser(UserPrincipal currentUser, Long userId,
                                        UpdateUserRequest updateUserRequest){

        // find user
        Optional<User> userOptional = userRepository.findById(userId);
        // user should be available
        if (!userOptional.isPresent()) {
            logService.createLog("UPDATE_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[userId=" + userId + "]",  updateUserRequest.toString(), "The user does not exists.");
            return new ResponseEntity<>(new ApiResponse(false, "The user does not exists."),
                    HttpStatus.BAD_REQUEST);
        }

        // update the tel, name, email and mobile of user
        User user = userOptional.get();
        user.setTelNumber(updateUserRequest.getTelNumber());
        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());
        user.setMobile(updateUserRequest.getMobile());

        // store user changes
        userRepository.save(user);

        logService.createLog("UPDATE_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[userId=" + userId + "]",  updateUserRequest.toString(), "");
        return new ResponseEntity<>("The user updated successfully."
                , new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * @param currentUser the user id who currently logged in
     * @param userId the unique number of an user
     * @param active true / false
     * @return OK response or report error
     */
    public ResponseEntity<?> activeUser(UserPrincipal currentUser, Long userId, boolean active){

        // find user
        Optional<User> userOptional = userRepository.findById(userId);

        // user should be available
        if (!userOptional.isPresent()) {
            logService.createLog("ACTIVE_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "[userId=" + userId + "]", "", "The user does not exists.");
            return new ResponseEntity<>(new ApiResponse(false, "The user does not exists."),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        // actice user
        user.setActive(active);

        // save user changes
        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{userId}")
                .buildAndExpand(userId).toUri();

        logService.createLog("ACTIVE_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[userId=" + userId + "]", "", "");

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "The user active/inactive successfully."));

    }

    /**
     * @param currentUser the user id who currently logged in
     * @param page the page number of the response (default value is 0)
     * @param size the page size of each response (default value is 30)
     * @return user responses page by page
     */
    public PagedResponse<UserResponse> getAllCustomers(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Set<Role> roles = new HashSet<>();
        Role customer = new Role(RoleName.ROLE_CUSTOMER);
        customer.setId(2L);
        roles.add(customer);
        // find all user. the role should be customer
        Page<User> users = userRepository.findAllByRoles(roles, pageable);

        if(users.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }

        // store all users into the list
        Vector<UserResponse> userResponses = createUserResponse(users);

        logService.createLog("GET_ALL_USERS", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        return new PagedResponse<>(userResponses, users.getNumber(),
                users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
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
