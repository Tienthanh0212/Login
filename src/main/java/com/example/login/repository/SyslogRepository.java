package com.example.login.repository;

import com.example.login.entity.Syslog;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SyslogRepository extends JpaRepository<Syslog, Long> {

    @Cacheable("findCountByMonth")
    @Query(value = "WITH DateRange AS (" +
            "    SELECT CAST(:startDate AS DATE) AS month_start " +
            "    UNION ALL " +
            "    SELECT DATEADD(MONTH, 1, month_start) " +
            "    FROM DateRange " +
            "    WHERE month_start < CAST(:endDate AS DATE) " +
            ") " +
            "SELECT d.month_start AS month, " +
            "       CASE WHEN COUNT(s.createdTime) = 0 THEN NULL ELSE COUNT(s.createdTime) END AS count " +
            "FROM DateRange d " +
            "LEFT JOIN SysLogs s " +
            "ON YEAR(s.createdTime) = YEAR(d.month_start) " +
            "   AND MONTH(s.createdTime) = MONTH(d.month_start) " +
            "   AND s.createdTime BETWEEN :startDate AND :endDate " +
            "   AND s.method = :method " +
            "GROUP BY d.month_start " +
            "ORDER BY d.month_start " +
            "OPTION (MAXRECURSION 0)",
            nativeQuery = true)
    List<Object[]> findCountByMonth(@Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate,
                                    @Param("method") String method);



    @Transactional
    @Modifying
    @Query("DELETE FROM Syslog s WHERE s.createdTime BETWEEN :startDate AND :endDate AND s.method = :method")
    int deleteByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate,@Param("method") String method);
}
