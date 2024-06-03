package com.example.login.controller;

import com.example.login.DTO.Syslog.DeletedCountDTO;
import com.example.login.DTO.Syslog.MonthlyRecordCountDTO;
import com.example.login.DTO.Syslog.SysLogDTO;
import com.example.login.service.SyslogService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Date;
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

    @PostMapping ("/delete")
    public ResponseEntity<DeletedCountDTO> deleteRecordsByDateRange(@RequestBody SysLogDTO sysLogDTO) {
        DeletedCountDTO deletedCountDTO = syslogService.deleteRecordsByDateRangeAndMethod(sysLogDTO);
        return ResponseEntity.ok(deletedCountDTO);
    }
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportMonthlyRecordCounts(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam String method,
            @RequestParam String fileType) {
        try {
            SysLogDTO sysLogDTO = new SysLogDTO();
            sysLogDTO.setStartDate(startDate);
            sysLogDTO.setEndDate(endDate);
            sysLogDTO.setMethod(method);

            File file;
            InputStreamResource resource;
            HttpHeaders headers = new HttpHeaders();

            if ("pdf".equalsIgnoreCase(fileType)) {
                syslogService.exportMonthlyRecordCountsToPDF(sysLogDTO);
                file = new File("monthly_record_counts.pdf");
                resource = new InputStreamResource(new FileInputStream(file));
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_record_counts.pdf");
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else if ("csv".equalsIgnoreCase(fileType)) {
                syslogService.exportMonthlyRecordCountsToCSV(sysLogDTO);
                file = new File("monthly_record_counts.csv");
                resource = new InputStreamResource(new FileInputStream(file));
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_record_counts.csv");
                headers.setContentType(MediaType.parseMediaType("text/csv"));
            } else {
                return ResponseEntity.status(400).body(null);
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);

        } catch (DocumentException | IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

}
