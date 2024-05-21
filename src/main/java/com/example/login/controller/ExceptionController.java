package com.example.login.controller;

import com.example.login.DTO.BaseResponse;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({NoResultException.class})
    public BaseResponse<String> notFound(NoResultException e) {
        log.info("INFO", e);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setCode(400);
        baseResponse.setMsg("no data");
        return baseResponse;
    }

    @ExceptionHandler({BindException.class})
    public BaseResponse<String> badRequest(BindException e) {
        log.info("bad request");
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String msg = "";
        for (ObjectError err : errors) {
            FieldError fieldError = (FieldError) err;
            msg += fieldError.getField() + ":" + err.getDefaultMessage() + ";";
        }
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setCode(400);
        baseResponse.setMsg(msg);
        return baseResponse;
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setCode(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMsg("Required request body is missing or not readable");
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public BaseResponse<String> duplicate(Exception e) {
        log.info("INFO", e);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setCode(402);
        baseResponse.setMsg("Duplicate Data");
        return baseResponse;

    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public BaseResponse<String> accessDined(Exception e) {
        log.info("INFO", e);
        return BaseResponse.<String>builder().code(403)
                .msg("Access Denied").build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> unauthorized(Exception e) {
        log.info("INFO", e);
        return BaseResponse.<String>builder().code(401)
                .msg(e.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> internalServerError(Exception e) {
        log.error("Internal Server Error", e);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setCode(500);
        baseResponse.setMsg("Lỗi server");
        return baseResponse;
    }



}