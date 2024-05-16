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
            return ResponseDTO.<String>builder()
                    .code(String.valueOf(HttpStatus.OK.value()))
                    .message("LOGIN SUCCESS")
                    .data(jwtTokenService.createToken(username))
                    .build();
        } catch (Exception e) {
            // Authentication failed, return error response
            return ResponseDTO.<String>builder()
                    .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                    .message("LOGIN FAILED: Invalid username or password")
                    .build();
        }
    }
    @PostMapping("/login1")
    public ResponseDTO<String> signUp(@RequestBody UserDTO userDTO) {
        try {
            userService.signUp1(userDTO);
            return new ResponseDTO<>("LOGINSUCCESS", "User registered successfully", null);
        } catch (Exception e) {
            return new ResponseDTO<>("LOGINFAIL", e.getMessage(), null);
        }
    }


}
