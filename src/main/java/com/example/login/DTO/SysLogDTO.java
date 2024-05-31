package com.example.login.DTO;

import lombok.*;

import java.time.YearMonth;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SysLogDTO {
    private YearMonth startDate;
    private YearMonth endDate;
    private String method;
}
