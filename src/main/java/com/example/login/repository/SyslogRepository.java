package com.example.login.repository;

import com.example.login.entity.Syslog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface SyslogRepository extends JpaRepository<Syslog, Long> {

    @Query(value = "SELECT CONCAT(YEAR(s.createdTime), '/', RIGHT('0' + CAST(MONTH(s.createdTime) AS VARCHAR(2)), 2)) AS month, " +
            "COUNT(*) AS count " +
            "FROM SysLogs s " +
            "WHERE s.createdTime BETWEEN :startDate AND :endDate " +
            "AND s.method = :method " +
            "GROUP BY YEAR(s.createdTime), MONTH(s.createdTime) " +
            "ORDER BY CONCAT(YEAR(s.createdTime), '/', RIGHT('0' + CAST(MONTH(s.createdTime) AS VARCHAR(2)), 2))",
            nativeQuery = true)
    List<Object[]> findSysLogs(@Param("startDate") Date startDate,
                               @Param("endDate") Date endDate,
                               @Param("method") String method);


}
