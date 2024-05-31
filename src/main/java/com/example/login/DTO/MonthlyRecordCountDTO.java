package com.example.login.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRecordCountDTO {
    private String month;
    private Number count;
}
