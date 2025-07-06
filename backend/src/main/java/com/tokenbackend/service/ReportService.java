package com.tokenbackend.service;

import com.tokenbackend.dto.*;
import com.tokenbackend.repository.JourneyRepository;
import com.tokenbackend.repository.CounterRepository;
import com.tokenbackend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    
    @Autowired
    private JourneyRepository journeyRepository;
    
    @Autowired
    private CounterRepository counterRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    public DailySummaryDto getDailySummary(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        
        // Get all journeys for the date
        List<Object[]> journeyStats = journeyRepository.getDailyStats(date);
        
        DailySummaryDto summary = new DailySummaryDto();
        summary.setDate(date);
        
        // Calculate totals
        int totalTokens = journeyStats.size();
        summary.setTotalTokensIssued(totalTokens);
        summary.setTotalPassengersServed(totalTokens); // Assuming 1 passenger per journey for now
        
        // Calculate tokens by counter
        Map<Integer, Integer> tokensByCounter = new HashMap<>();
        Map<String, Integer> tokensByStation = new HashMap<>();
        Map<String, Integer> tokensByGender = new HashMap<>();
        
        for (Object[] stat : journeyStats) {
            Integer counterNo = (Integer) stat[0];
            String station = (String) stat[1];
            String gender = (String) stat[2];
            
            // Count by counter
            tokensByCounter.merge(counterNo, 1, Integer::sum);
            
            // Count by station
            tokensByStation.merge(station, 1, Integer::sum);
            
            // Count by gender
            tokensByGender.merge(gender, 1, Integer::sum);
        }
        
        summary.setTokensByCounter(tokensByCounter);
        summary.setTokensByStation(tokensByStation);
        summary.setTokensByGender(tokensByGender);
        
        // Calculate average wait time (placeholder - would need actual timing data)
        summary.setAverageWaitTime(15.5); // Mock data
        
        // Find peak hour (placeholder)
        summary.setPeakHourTokens(50);
        summary.setPeakHour("10:00-11:00");
        
        return summary;
    }
    
    public List<CounterPerformanceDto> getCounterPerformance(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        
        List<CounterPerformanceDto> performance = new ArrayList<>();
        
        // Get all active counters
        List<Object[]> counterStats = counterRepository.getCounterStats(date);
        
        for (Object[] stat : counterStats) {
            Integer counterId = (Integer) stat[0];
            String counterName = (String) stat[1];
            String staffName = (String) stat[2];
            Integer totalServed = (Integer) stat[3];
            Integer currentQueue = (Integer) stat[4];
            String status = (String) stat[5];
            
            CounterPerformanceDto dto = new CounterPerformanceDto();
            dto.setCounterId(counterId);
            dto.setCounterName(counterName);
            dto.setAssignedStaffName(staffName);
            dto.setTotalServedToday(totalServed != null ? totalServed : 0);
            dto.setCurrentQueuePosition(currentQueue != null ? currentQueue : 0);
            dto.setAverageServiceTime(12.5); // Mock data
            dto.setStatus(status);
            dto.setDate(date);
            
            performance.add(dto);
        }
        
        return performance;
    }
    
    public List<StaffPerformanceDto> getStaffPerformance(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        
        List<StaffPerformanceDto> performance = new ArrayList<>();
        
        // Get all active staff
        List<Object[]> staffStats = staffRepository.getStaffStats(date);
        
        for (Object[] stat : staffStats) {
            String staffId = (String) stat[0];
            String staffName = (String) stat[1];
            String role = (String) stat[2];
            Integer assignedCounter = (Integer) stat[3];
            Integer totalServed = (Integer) stat[4];
            LocalDateTime lastActivity = (LocalDateTime) stat[5];
            String status = (String) stat[6];
            
            StaffPerformanceDto dto = new StaffPerformanceDto();
            dto.setStaffId(staffId);
            dto.setStaffName(staffName);
            dto.setRole(role);
            dto.setAssignedCounterId(assignedCounter);
            dto.setTotalServedToday(totalServed != null ? totalServed : 0);
            dto.setAverageServiceTime(10.5); // Mock data
            dto.setLastActivity(lastActivity);
            dto.setStatus(status);
            
            performance.add(dto);
        }
        
        return performance;
    }
    
    public QueueStatusDto getQueueStatus() {
        QueueStatusDto status = new QueueStatusDto();
        
        // Get current queue status
        List<Object[]> queueStats = counterRepository.getQueueStats();
        
        int totalInQueue = 0;
        Map<Integer, Integer> queueByCounter = new HashMap<>();
        Map<Integer, String> counterStatus = new HashMap<>();
        Map<Integer, String> assignedStaff = new HashMap<>();
        
        for (Object[] stat : queueStats) {
            Integer counterNo = (Integer) stat[0];
            Integer queuePosition = (Integer) stat[1];
            String statusStr = (String) stat[2];
            String staffName = (String) stat[3];
            
            queueByCounter.put(counterNo, queuePosition != null ? queuePosition : 0);
            counterStatus.put(counterNo, statusStr);
            assignedStaff.put(counterNo, staffName);
            
            totalInQueue += queuePosition != null ? queuePosition : 0;
        }
        
        status.setTotalInQueue(totalInQueue);
        status.setQueueByCounter(queueByCounter);
        status.setCounterStatus(counterStatus);
        status.setAssignedStaff(assignedStaff);
        
        // Calculate estimated wait time (mock calculation)
        int activeCounters = (int) counterStatus.values().stream()
                .filter(s -> "ACTIVE".equals(s))
                .count();
        int estimatedWaitTime = activeCounters > 0 ? (totalInQueue / activeCounters) * 10 : 0;
        status.setEstimatedWaitTime(estimatedWaitTime);
        
        return status;
    }
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Today's summary
        DailySummaryDto todaySummary = getDailySummary(LocalDate.now());
        stats.put("todaySummary", todaySummary);
        
        // Queue status
        QueueStatusDto queueStatus = getQueueStatus();
        stats.put("queueStatus", queueStatus);
        
        // Active counters count
        long activeCounters = counterRepository.countActiveCounters();
        stats.put("activeCounters", activeCounters);
        
        // Active staff count
        long activeStaff = staffRepository.countActiveStaff();
        stats.put("activeStaff", activeStaff);
        
        // Total served today
        Integer totalServed = counterRepository.getTotalServedToday();
        stats.put("totalServedToday", totalServed != null ? totalServed : 0);
        
        return stats;
    }
} 