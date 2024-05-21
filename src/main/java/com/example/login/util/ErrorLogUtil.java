//package com.example.login.util;
//import com.example.login.entity.ErrorLog;
//import com.example.login.repository.ErrorLogRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import java.util.Date;
//import java.util.Arrays;
//
//
//@Component
//@RequiredArgsConstructor
//public class ErrorLogUtil {
//
//    private final ErrorLogRepository errorLogRepository;
//
//    public void storeToDB(String errMessage, Exception exception) {
//        String errStackTraceStr = Arrays.toString(exception.getStackTrace());
//
//        ErrorLog errorLog = ErrorLog.builder()
//                .errMessage(errMessage)
//                .errStackTrace(errStackTraceStr)
//                .date(new Date())
//                .build();
//        errorLogRepository.save(errorLog);
//    }
//}