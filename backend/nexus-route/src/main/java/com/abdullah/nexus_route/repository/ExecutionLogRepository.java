package com.abdullah.nexus_route.repository;

import com.abdullah.nexus_route.entity.ExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {
//    @Query("SELECT (COUNT(e) * 100.0) / NULLIF((SELECT COUNT(allLogs) FROM ExecutionLog allLogs), 0) FROM ExecutionLog e WHERE e.isError = true")
    @Query("""
            SELECT (SUM(CASE e.isError WHEN true THEN 1 ELSE 0 END) * 100.0) / NULLIF(COUNT(e) , 0)
            FROM ExecutionLog e
            """)
    Double calculateOverallErrorRate();

    @Query("""
            SELECT (SUM(CASE e.isError WHEN true THEN 1 ELSE 0 END) * 100.0) / NULLIF(COUNT(e) , 0)
            FROM ExecutionLog e
            WHERE e.timestamp BETWEEN :start AND :end
            """)
    Double calculateOverallErrorRateBetweenStartAndEnd(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT e.executionTimeMs from ExecutionLog e where e.isError = false ORDER BY e.executionTimeMs ASC")
    List<Long> findAllExecutionTimeMsByIsErrorIsNull();
}
