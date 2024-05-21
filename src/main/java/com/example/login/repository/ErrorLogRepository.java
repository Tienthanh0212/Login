package com.example.login.repository;

import com.example.login.entity.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, String> {

}
