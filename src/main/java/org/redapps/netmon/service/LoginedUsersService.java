package org.redapps.netmon.service;

import org.redapps.netmon.model.LoginedUser;
import org.redapps.netmon.model.User;
import org.redapps.netmon.repository.LoginedUsersRepository;
import org.redapps.netmon.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoginedUsersService {

    private final LoginedUsersRepository loginedUsersRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoginedUsersService.class);

    @Autowired
    public LoginedUsersService(LoginedUsersRepository loginedUsersRepository, UserRepository userRepository) {
        this.loginedUsersRepository = loginedUsersRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean loginUser(String username, String token) {

        logger.debug("loginUser " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastAccess(LocalDateTime.now());
            userRepository.save(user);
        }

        LoginedUser loginedUser = new LoginedUser(username, token);
        loginedUsersRepository.deleteAllByUsername(username);

        loginedUsersRepository.save(loginedUser);
        return true;

    }

    public void logoutUser(String username) {
        List<LoginedUser> loginedUsers= loginedUsersRepository.findByUsername(username);
        loginedUsersRepository.deleteAll(loginedUsers);
    }

    public boolean isLogined(String token){
        return loginedUsersRepository.existsByToken(token);
    }
}
