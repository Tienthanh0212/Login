package com.example.login.controller;

import com.example.login.DTO.MonthlyRecordCountDTO;
import com.example.login.DTO.SysLogDTO;
import com.example.login.service.SyslogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/syslog")
public class SyslogController {
    @Autowired
    SyslogService syslogService;

    @PostMapping("/monthly-records")
    public ResponseEntity<List<MonthlyRecordCountDTO>> getMonthlyRecordCounts(@RequestBody SysLogDTO sysLogDTO) {
        List<MonthlyRecordCountDTO> monthlyRecordCounts = syslogService.getMonthlyRecordCountsBetweenDates(sysLogDTO);
        return ResponseEntity.ok(monthlyRecordCounts);
    }
}
