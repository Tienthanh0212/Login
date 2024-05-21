package com.example.login.controller;

import com.example.login.DTO.SignUpDTO;
import com.example.login.DTO.UserDTO;
import com.example.login.DTO.UserException;
import com.example.login.internationalize.MyLocaleResolver;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.JwtTokenService;
import com.example.login.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MyLocaleResolver myLocaleResolver;

    @Autowired
    BCryptPasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SignUpDTO userDTO, BindingResult bindingResult, HttpServletRequest request) {
        Locale userLocale = myLocaleResolver.resolveLocale(request);

        try {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessageBuilder = new StringBuilder();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errorMessageBuilder.append(messageSource.getMessage(error, userLocale)).append("; ");
                }
                String errorMessage = errorMessageBuilder.toString();
                return ResponseEntity.status(400)
                        .body(new UserException(400, "REGISTER_ERROR", errorMessage));
            }
            String customErrorMessage = validateRegistration(userDTO, userLocale);
            if (customErrorMessage != null) {
                return ResponseEntity.status(400)
                        .body(new UserException(400, "REGISTER_ERROR", customErrorMessage));
            }
            User user = new User();
            user.setPassword(encoder.encode(userDTO.getUsername() + userDTO.getPassword()));
            user.setUsername(userDTO.getUsername());
            userRepository.save(user);

            String successMessage = messageSource.getMessage("register.success", null, userLocale);
            return ResponseEntity.status(200)
                    .body(new UserException(200, "REGISTER_SUCCESSFULLY", successMessage));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDto, BindingResult bindingResult, HttpServletRequest request) {
        Locale userLocale = myLocaleResolver.resolveLocale(request);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessageBuilder = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessageBuilder.append(error.getDefaultMessage()).append("; ");
            }
            String errorMessage = errorMessageBuilder.toString();

            return ResponseEntity.status(400).body(new UserException(400, "LOGIN_ERROR", errorMessage));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(),
                            userDto.getUsername() + userDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.status(200)
                    .body(new UserException(200, "LOGIN_SUCCESS", jwtTokenService.createToken(userDto.getUsername())));
        } catch (AuthenticationException ex) {
            String message = messageSource.getMessage("register.error.login", null, userLocale);
            return ResponseEntity.status(400).body(new UserException(400, "LOGIN_ERROR", message));
        }
    }


}
