package com.example.login.controller;

import com.example.login.DTO.RoleDTO;
import com.example.login.DTO.UserException;
import com.example.login.internationalize.MyLocaleResolver;
import com.example.login.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MyLocaleResolver myLocaleResolver;
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody @Valid RoleDTO roleDTO, BindingResult bindingResult, HttpServletRequest request) {
        Locale userLocale = myLocaleResolver.resolveLocale(request);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessageBuilder = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessageBuilder.append(messageSource.getMessage(error, userLocale)).append("; ");
            }
            String errorMessage = errorMessageBuilder.toString();
            return ResponseEntity.status(400)
                    .body(new UserException(400, "CREATED_ERROR", errorMessage));
        }
        return roleService.create(roleDTO,request);

    }


}
