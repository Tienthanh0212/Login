package com.example.login.controller;

import com.example.login.DTO.ResponseDTO;
import com.example.login.DTO.UserDTO;
import com.example.login.service.JwtTokenService;
import com.example.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenService jwtTokenService;

    @PostMapping("/")
    public ResponseDTO<UserDTO> create(@RequestBody UserDTO userDTO) {
        userService.signup(userDTO);
        return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(userDTO).build();
    }

    @PutMapping("/")
    public ResponseDTO<Void> update(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") Integer id) {
        userService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PostMapping("/login")
    public ResponseDTO<String> login(@RequestParam("username") String username,
                                     @RequestParam("password") String password){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            System.out.println(authentication);
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //if login success, jwt-gen token(string)
            return new ResponseDTO<>("LOGINSUCCESS", "User registered successfully", jwtTokenService.createToken(username));
        } catch (Exception e) {
            // Authentication failed, return error response
            return new ResponseDTO<>("LOGINFAIL", "Username or password is incorrect", null);
        }
    }
    @PostMapping("/registerWithoutSecurity")
    public ResponseDTO<String> signUp(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        try {
            userService.signUp1(username, password);
            return new ResponseDTO<>("RegisterSuccess", "User registered successfully", null);
        } catch (Exception e) {
            return new ResponseDTO<>("RegisterFail", e.getMessage(), null);
        }
    }

    @PostMapping("/loginWithoutSecurity")
    public ResponseDTO<String> login1(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        try {
            boolean isSuccess = userService.login(username, password);
            if (isSuccess) {
                return new ResponseDTO<>("LoginSuccess", "User Login successfully", null);
            } else {
                return new ResponseDTO<>("LoginFailed", "Username or password is incorrect", null);
            }
        } catch (Exception e) {
            return new ResponseDTO<>("LoginFailed", "An error occurred", null);
        }
    }



}
