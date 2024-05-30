package com.example.login.DTO;

import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SysLogDTO {
    private Date startDate;
    private Date endDate;
    private String method;
}
