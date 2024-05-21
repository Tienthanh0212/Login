package com.example.login.exception;
import com.example.login.DTO.BaseResponse;
import com.example.login.DTO.ErrorDetails;
import com.example.login.entity.ErrorLog;
import com.example.login.repository.ErrorLogRepository;
import com.example.login.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    EmailService emailService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex) {

        ErrorDetails errorDetail = new ErrorDetails(ex.getMessage(), Arrays.toString(ex.getStackTrace()), new Date());

        emailService.sendMailAdmin("User registration failed " );
        saveErrorToDatabase(ex);
        return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private void saveErrorToDatabase(Exception errorDetails) {
        ErrorLog errorLog = ErrorLog.builder()
                .errMessage(errorDetails.getMessage())
                .date(LocalDateTime.now())
                .errStackTrace(Arrays.toString(errorDetails.getStackTrace()))
                .build();
        errorLogRepository.save(errorLog);
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setCode(HttpStatus.BAD_REQUEST.value());
        baseResponse.setTimestamp(LocalDateTime.now());
        emailService.sendMailAdmin("Failed in the server " );
        baseResponse.setMsg("Required request body is missing or not readable");
        saveErrorToDatabase(ex);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

}
