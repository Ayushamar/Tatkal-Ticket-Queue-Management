package com.tokenbackend.repository;

import com.tokenbackend.model.Counter;
import com.tokenbackend.model.Counter.CounterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Integer> {
    
    Optional<Counter> findByCounterNumber(Integer counterNumber);
    
    List<Counter> findByStatus(CounterStatus status);
    
    List<Counter> findByAssignedStaffId(String staffId);
    
    @Query("SELECT c FROM Counter c WHERE c.status = 'ACTIVE' ORDER BY c.counterNumber")
    List<Counter> findActiveCounters();
    
    @Query("SELECT COUNT(c) FROM Counter c WHERE c.status = 'ACTIVE'")
    Long countActiveCounters();
    
    @Query("SELECT c FROM Counter c WHERE c.assignedStaffId = :staffId AND c.status = 'ACTIVE'")
    Optional<Counter> findActiveCounterByStaffId(@Param("staffId") String staffId);
    
    @Query("SELECT SUM(c.totalServedToday) FROM Counter c WHERE c.status = 'ACTIVE'")
    Integer getTotalServedToday();
    
    @Query("SELECT c.counterId, c.counterName, c.assignedStaffName, c.totalServedToday, c.currentQueuePosition, c.status FROM Counter c WHERE c.status = 'ACTIVE'")
    List<Object[]> getCounterStats(@Param("date") LocalDate date);
    
    @Query("SELECT c.counterNumber, c.currentQueuePosition, c.status, c.assignedStaffName FROM Counter c")
    List<Object[]> getQueueStats();
} 