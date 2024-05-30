package com.example.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "SysLogs")

public class Syslog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sysLogId;

    private LocalDateTime createdTime;

    private String method;
}