package org.redapps.netmon.controller.auth;

import org.redapps.netmon.exception.AppException;
import org.redapps.netmon.exception.ResourceNotFoundException;
import org.redapps.netmon.model.Role;
import org.redapps.netmon.model.RoleName;
import org.redapps.netmon.model.User;

import org.redapps.netmon.payload.*;
import org.redapps.netmon.repository.RoleRepository;
import org.redapps.netmon.repository.UserRepository;
import org.redapps.netmon.security.CurrentUser;
import org.redapps.netmon.security.JwtTokenProvider;
import org.redapps.netmon.security.UserPrincipal;
import org.redapps.netmon.service.LogService;
import org.redapps.netmon.service.LoginedUsersService;
import org.redapps.netmon.util.AppConstants;
import org.redapps.netmon.util.NetmonStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import org.redapps.netmon.util.SMSSender;
import org.redapps.netmon.util.RandomCodeGenerator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static boolean emailIsEnabled;
    private static boolean smsIsEnabled;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final LoginedUsersService loginedUsersService;
    private final LogService logService;

    @Autowired
    public AuthController(RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider, UserRepository userRepository,
                          AuthenticationManager authenticationManager, LoginedUsersService loginedUsersService,
                          LogService logService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.loginedUsersService = loginedUsersService;
        this.logService = logService;
    }

    @Value("${email.enabled}")
    public void setEnabledEmail(boolean value) {
        emailIsEnabled = value;
    }

    @Value("${sms.enabled}")
    public void setEnabledSMS(boolean value) {
        smsIsEnabled = value;
    }

    /**
     * authenticate the user
     * @param loginRequest the login information object
     * @return OK response or report error
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (!userOptional.isPresent()) {
            logService.createLog("SIGNIN_USER", loginRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                    "[username=" + loginRequest.getUsername() + "]", "The user not found.");
            throw new UsernameNotFoundException("The user not found.");
        }

        User user = userOptional.get();
        if (!user.isActive()) {
            logService.createLog("SIGNIN_USER", loginRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                    "[username=" + loginRequest.getUsername() + "]", "The user is not active.");
            return new ResponseEntity<>(new ApiResponse(false, "The user is not active."), HttpStatus.BAD_REQUEST);
        }

        logService.createLog("SIGNIN_USER", loginRequest.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,  "",
                "[username=" + loginRequest.getUsername() + "]", "");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        loginedUsersService.loginUser(user.getUsername(), jwt);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.getRoles()));
    }

    /**
     * logout user and disable his token
     * @param currentUser the user id who currently logged in
     * @return OK response or report error
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutPage(@CurrentUser UserPrincipal currentUser) {

        logService.createLog("LOGOUT_USER", currentUser.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS, "", "", "");

        loginedUsersService.logoutUser(currentUser.getUsername());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/signin")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Logout successfully."));
    }

    /**
     *
     * @param signUpRequest the signup information object
     * @return OK response or report error
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            logService.createLog("REGISTER_USER", signUpRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", "[username=" + signUpRequest.getUsername() + "]", "The username is already taken.");
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken."),
                    HttpStatus.BAD_REQUEST);
        }

        RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();
        String activationCode = randomCodeGenerator.generate(AppConstants.ACTIVATION_CODE_LEN);

        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getNationalID(), signUpRequest.getEmail(), signUpRequest.getMobile(),
                signUpRequest.getTelNumber(), signUpRequest.getPassword(), activationCode);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> roleOptional = roleRepository.findByName(RoleName.ROLE_CUSTOMER);
        if (!roleOptional.isPresent()) {
            logService.createLog("REGISTER_USER", signUpRequest.getUsername(),
                    NetmonStatus.LOG_STATUS.FAILED, "", "[username=" + signUpRequest.getUsername() + "]", "The user-role not set.");
            throw new AppException("The user-role not set.");
        }

        Role role = roleOptional.get();
        user.setRoles(Collections.singleton(role));
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        String response = SMSSender.sendMessage(signUpRequest.getMobile(), signUpRequest.getEmail(),
                activationCode, smsIsEnabled, emailIsEnabled);

        logService.createLog("REGISTER_USER", signUpRequest.getUsername(), NetmonStatus.LOG_STATUS.SUCCESS,
                "[username=" + signUpRequest.getUsername() + "]", "", "Send message response: " + response);

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    /**
     * sending the activation code
     * @param activationCodeRequest the user information object
     * @return OK response or report error
     */
    @PostMapping("/getactivationcode")
    public ResponseEntity<?> getActivationCode(@Valid @RequestBody ActivationCodeRequest activationCodeRequest) {

        Optional<User> userOptional = userRepository.findByUsername(activationCodeRequest.getUsername());
        if (!userOptional.isPresent()) {
            logService.createLog("GET_ACTIVATION_CODE", activationCodeRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                    "[username=" + activationCodeRequest.getUsername() + "]",
                    "The user not found.");
            throw new UsernameNotFoundException("The user not found.");
        }

        User user = userOptional.get();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        activationCodeRequest.getUsername(),
                        activationCodeRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();
        String reActivationCode = randomCodeGenerator.generate(AppConstants.ACTIVATION_CODE_LEN);
        user.setActivationCode(reActivationCode);

        userRepository.save(user);

        String response = SMSSender.sendMessage(user.getMobile(), user.getEmail(),
                reActivationCode, smsIsEnabled, emailIsEnabled);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/getactivationcode")
                .buildAndExpand().toUri();

        logService.createLog("GET_ACTIVATION_CODE", activationCodeRequest.getUsername(),
                NetmonStatus.LOG_STATUS.SUCCESS, "",
                "[username=" + activationCodeRequest.getUsername() + "]",
                "Send message response: " + response);

        return ResponseEntity.created(location).body(new ApiResponse(true, "Activation code has been sent"));
    }

    /**
     *
     * @param activateRequest the active user object
     * @return OK response or report error
     */
    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@Valid @RequestBody ActivateRequest activateRequest) {

        Optional<User> userOptional = userRepository.findByUsername(activateRequest.getUsername());
        if (!userOptional.isPresent()) {
            logService.createLog("ACTIVATE_USER", activateRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                    "[username=" + activateRequest.getUsername() + "]", "The user not found.");
            throw new ResourceNotFoundException("User", "username", activateRequest.getUsername());
        }

        User user = userOptional.get();

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/activate")
                .buildAndExpand().toUri();

        logger.info("activation code is: " + user.getActivationCode());

        if (user.getActivationCode().equals(activateRequest.getActivationCode())) {
            logService.createLog("ACTIVATE_USER", activateRequest.getUsername(),
                    NetmonStatus.LOG_STATUS.SUCCESS, "", "[username=" +
                            activateRequest.getUsername() + "]", "");

            user.setActive(true);
            userRepository.save(user);
            return ResponseEntity.created(location).body(new ApiResponse(true, "The user activated successfully."));
        }

        logService.createLog("ACTIVATE_USER", activateRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED, "",
                "[username=" + activateRequest.getUsername() + "]", "The activation code is not correct.");
        return ResponseEntity.created(location).body(new ApiResponse(false, "The activation code is not correct"));
    }

    /**
     * sending user password
     * @param getForgotPasswordRequest an object of username
     * @return OK response or report error
     */
    @PostMapping("/forgotpassword")
    public ResponseEntity<?> getResetPassword(@Valid @RequestBody GetForgottenCodeRequest getForgotPasswordRequest) {

        Optional<User> userOptional = userRepository.findByUsername(getForgotPasswordRequest.getUsername());
        if(!userOptional.isPresent()){
            logger.error("User not found [username: " + getForgotPasswordRequest.getUsername() + "].");

            logService.createLog("FORGOT_PASSWORD", getForgotPasswordRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", "[username=" + getForgotPasswordRequest.getUsername() + "]", "The user not found.");
            throw new ResourceNotFoundException("User", "username", getForgotPasswordRequest.getUsername());
        }

        RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();
        String resetCode = randomCodeGenerator.generate(AppConstants.FORGOTTEN_CODE_LEN);
        User user = userOptional.get();
        user.setForgottenCode(resetCode);

        logger.info("resetCode: " + resetCode);

        userRepository.save(user);

        String response = SMSSender.sendMessage(user.getMobile(), user.getEmail(),
                resetCode, smsIsEnabled, emailIsEnabled);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/forgotpassword")
                .buildAndExpand().toUri();

        logService.createLog("FORGOT_PASSWORD", getForgotPasswordRequest.getUsername(),
                NetmonStatus.LOG_STATUS.SUCCESS, "", "[username=" + getForgotPasswordRequest.getUsername() + "]",
                "Send message response: " + response);

        return ResponseEntity.created(location).body(new ApiResponse(true, "Forgotten code has been sent."));
    }

    /**
     *
     * @param resetPasswordRequest an object of new password
     * @return OK response or report error
     */
    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        Optional<User> userOptional = userRepository.findByUsername(resetPasswordRequest.getUsername());
        if(!userOptional.isPresent()){
            logger.error("User not found [username: " + resetPasswordRequest.getUsername() + "].");

            logService.createLog("RESET_PASSWORD", resetPasswordRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                    "", "[username=" + resetPasswordRequest.getUsername() + "]", "The user not found.");
            throw new ResourceNotFoundException("User", "username", resetPasswordRequest.getUsername());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/resetpassword")
                .buildAndExpand().toUri();

        User user = userOptional.get();
        logger.info("forgotten-code: " + user.getForgottenCode());

        //FIXME: exception
        if (user.getForgottenCode().equals(resetPasswordRequest.getForgottenCode())) {

            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            userRepository.save(user);

            String response = SMSSender.sendMessage(user.getMobile(), user.getEmail(),
                    "Your password has been successfully changed.", smsIsEnabled, emailIsEnabled);

            logService.createLog("RESET_PASSWORD", resetPasswordRequest.getUsername(),
                    NetmonStatus.LOG_STATUS.SUCCESS, "", "[username=" +
                            resetPasswordRequest.getUsername() + "]", "Send message response: " + response);

            return ResponseEntity.created(location).body(new ApiResponse(true, "Password has been reset successfully."));
        }

        logService.createLog("RESET_PASSWORD", resetPasswordRequest.getUsername(), NetmonStatus.LOG_STATUS.FAILED,
                "", "[username=" + resetPasswordRequest.getUsername() + "]", "The forgotten code is not correct.");
        return ResponseEntity.created(location).body(new ApiResponse(false, "The forgotten code is not correct."));
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.isActive());
    }
}
