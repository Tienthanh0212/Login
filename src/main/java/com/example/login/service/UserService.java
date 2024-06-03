package com.example.login.service;

import com.example.login.DTO.SignUpDTO;
import com.example.login.DTO.User.UserDTO;
import com.example.login.DTO.UserException;
import com.example.login.entity.User;
import com.example.login.internationalize.MyLocaleResolver;
import com.example.login.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

public interface UserService {

    ResponseEntity<?> register(SignUpDTO userDTO, HttpServletRequest request);

    ResponseEntity<?> login(UserDTO userDTO, HttpServletRequest request);

    List<User> getAdminUsers();
}
@Service
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MyLocaleResolver myLocaleResolver;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> register(SignUpDTO userDTO, HttpServletRequest request)  {
        Locale userLocale = myLocaleResolver.resolveLocale(request);

        String customErrorMessage = validateRegistration(userDTO, userLocale);
        if (customErrorMessage != null) {
            return ResponseEntity.status(400)
                    .body(new UserException(400, "REGISTER_ERROR", customErrorMessage));
        }
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(userDTO, User.class);
        user.setPassword(encoder.encode(userDTO.getUsername() + userDTO.getPassword()));

        userRepository.save(user);

        String successMessage = messageSource.getMessage("register.success", null, userLocale);
        return ResponseEntity.status(200)
                .body(new UserException(200, "REGISTER_SUCCESSFULLY", successMessage));
    }

    private String validateRegistration(SignUpDTO userDTO, Locale userLocale) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            return messageSource.getMessage("register.error.username.exists", null, userLocale);
        }
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            return messageSource.getMessage("register.error.password.mismatch", null, userLocale);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> login(UserDTO userDTO, HttpServletRequest request) {
        Locale userLocale = myLocaleResolver.resolveLocale(request);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
                            userDTO.getUsername() + userDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.status(200)
                    .body(new UserException(200, "LOGIN_SUCCESS", jwtTokenService.createToken(userDTO.getUsername())));
        } catch (Exception ex) {
            String message = messageSource.getMessage("register.error.login", null, userLocale);
            return ResponseEntity.status(400).body(new UserException(400, "LOGIN_ERROR", message));
        }
    }


    @Override
    public List<User> getAdminUsers() {
        return userRepository.findByRole("ADMIN");
    }
}
