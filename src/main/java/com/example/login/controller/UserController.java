package com.example.login.controller;

import com.example.login.DTO.SignUpDTO;
import com.example.login.DTO.User.UserDTO;
import com.example.login.DTO.UserException;
import com.example.login.internationalize.MyLocaleResolver;
import com.example.login.repository.UserRepository;
import com.example.login.service.JwtTokenService;
import com.example.login.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
        if (bindingResult.hasErrors()) {
                StringBuilder errorMessageBuilder = new StringBuilder();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errorMessageBuilder.append(messageSource.getMessage(error, userLocale)).append("; ");
                }
                String errorMessage = errorMessageBuilder.toString();
                return ResponseEntity.status(400)
                        .body(new UserException(400, "REGISTER_ERROR", errorMessage));
            }
            return userService.register(userDTO,request);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessageBuilder = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessageBuilder.append(error.getDefaultMessage()).append("; ");
            }
            String errorMessage = errorMessageBuilder.toString();

            return ResponseEntity.status(400).body(new UserException(400, "LOGIN_ERROR", errorMessage));
        }
            return userService.login(userDto, request);
    }


}
