package com.example.login.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String errMessage;

    @Column(columnDefinition="TEXT")
    private String errStackTrace;

    private LocalDateTime date;
}