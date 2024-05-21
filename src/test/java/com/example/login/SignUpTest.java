package com.example.login;


import com.example.login.DTO.SignUpDTO;
import com.example.login.DTO.UserException;
import com.example.login.controller.UserController;
import com.example.login.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Set;

@SpringBootTest
public class SignUpTest {
    BCryptPasswordEncoder encoder;
    UserException userException;

    public SignUpTest() {
        encoder = new BCryptPasswordEncoder();
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserController userController;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private Validator validator;

    @Test
    public void registerWithExistUsername() {
        SignUpDTO userDto = new SignUpDTO("thanh", "123456789", "123456789");
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userDto");
        userException = (UserException) userController.register(userDto,bindingResult,request).getBody();
        Assertions.assertThat(userException.getMessages()).isEqualTo("Username was already taken!");
    }

    @Test
    public void registerWithEmptyUsername() {
        SignUpDTO userDto = new SignUpDTO("", "12345678", "12345678");
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userDto");
        Set<ConstraintViolation<SignUpDTO>> violations = validator.validate(userDto);
        userException = (UserException) userController.register(userDto,bindingResult,request).getBody();
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("Username cannot be blank");
    }

    @Test
    public void registerWithEmptyPassword() {
        SignUpDTO userDto = new SignUpDTO("thanh123", "", "123");
        Set<ConstraintViolation<SignUpDTO>> violations = validator.validate(userDto);
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userDto");
        userException = (UserException) userController.register(userDto,bindingResult,request).getBody();
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("Password must be at least 8 characters long");
    }

    @Test
    public void registerWithEmptyConfirmPassword() {
        SignUpDTO userDto = new SignUpDTO("thanh567", "12346789", "");
        Set<ConstraintViolation<SignUpDTO>> violations = validator.validate(userDto);
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userDto");
        userException = (UserException) userController.register(userDto,bindingResult,request).getBody();
        Assertions.assertThat(violations.iterator().next().getMessage()).isEqualTo("Confirm password not blank");
    }

    @Test
    public void registerWithInvalidConfirmPassword() {
        SignUpDTO userDto = new SignUpDTO("thanhabc", "123456789abc", "12345");
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userDto");
        userException = (UserException) userController.register(userDto,bindingResult,request).getBody();
        Assertions.assertThat(userException.getMessages())
                .isEqualTo("Passwords do not match");
    }

    @Test
    public void registerSucces() {
        SignUpDTO userDto = new SignUpDTO("thanhkkkkk", "12345678910", "12345678910");
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userDto");
        userException = (UserException) userController.register(userDto,bindingResult,request).getBody();
        Assertions.assertThat(userException.getMessages()).isEqualTo("Register Successfully!");
    }
}