package com.example.login.DTO.Syslog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MonthlyRecordCountDTO {
    private String month;
    private Number count;
}
