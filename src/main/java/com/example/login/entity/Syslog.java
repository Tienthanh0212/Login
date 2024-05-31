package com.example.login.entity;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "SysLogs")

public class Syslog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sysLogId;

    private Date createdTime;

    private String method;
}