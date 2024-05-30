package com.example.login.repository;

import com.example.login.entity.Syslog;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface SyslogRepository extends JpaRepository<Syslog, Long> {


    @Cacheable("findCountByMonth")
    @Query(value = "SELECT YEAR(s.createdTime) AS year, MONTH(s.createdTime) AS month, COUNT(*) AS count " +
            "FROM SysLogs s " +
            "WHERE s.createdTime BETWEEN :startDate AND :endDate " +
            "AND s.method = :method " +
            "GROUP BY YEAR(s.createdTime), MONTH(s.createdTime) " +
            "ORDER BY YEAR(s.createdTime), MONTH(s.createdTime)",
            nativeQuery = true)
    List<Object[]> findCountByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("method") String method);

}
