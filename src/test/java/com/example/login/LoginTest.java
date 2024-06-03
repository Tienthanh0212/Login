package com.example.login;


import com.example.login.DTO.User.UserDTO;
import com.example.login.DTO.UserException;
import com.example.login.controller.UserController;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@SpringBootTest
public class LoginTest {
    @Autowired
    UserController userController;
    UserException userException;

    @Autowired
    HttpServletRequest request;
    private void setLocale(HttpServletRequest request, Locale locale) {
        request.setAttribute(LocaleChangeInterceptor.DEFAULT_PARAM_NAME, locale.getLanguage());

        RequestContextUtils.getLocaleResolver(request).setLocale(request, null, locale);
    }
    @Test
    public void loginWithFail() {
        UserDTO userDTO = new UserDTO("", "123456789");
        BindingResult bindingResult = new BeanPropertyBindingResult(userDTO, "userDto");
        userException = (UserException) userController.login(userDTO,bindingResult,request).getBody();
        Assertions.assertThat(userException.getMessages()).isEqualTo("Wrong Username or Password");
    }

    @Test
    public void loginSuccessful() {
        UserDTO userDTO = new UserDTO("thanha", "123456789");
        BindingResult bindingResult = new BeanPropertyBindingResult(userDTO, "userDto");
        userException = (UserException) userController.login(userDTO,bindingResult,request).getBody();
        Assertions.assertThat(userException.getErrorCode()).isEqualTo("LOGIN_SUCCESS");
    }


}

