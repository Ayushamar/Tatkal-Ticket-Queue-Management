package com.tokenbackend.controller.admin;

import com.tokenbackend.dto.*;
import com.tokenbackend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = reportService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/daily-summary")
    public ResponseEntity<DailySummaryDto> getDailySummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailySummaryDto summary = reportService.getDailySummary(date);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/counter-performance")
    public ResponseEntity<List<CounterPerformanceDto>> getCounterPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CounterPerformanceDto> performance = reportService.getCounterPerformance(date);
        return ResponseEntity.ok(performance);
    }
    
    @GetMapping("/staff-performance")
    public ResponseEntity<List<StaffPerformanceDto>> getStaffPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<StaffPerformanceDto> performance = reportService.getStaffPerformance(date);
        return ResponseEntity.ok(performance);
    }
    
    @GetMapping("/queue-status")
    public ResponseEntity<QueueStatusDto> getQueueStatus() {
        QueueStatusDto status = reportService.getQueueStatus();
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/export/daily-summary")
    public ResponseEntity<String> exportDailySummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // TODO: Implement CSV/Excel export
        DailySummaryDto summary = reportService.getDailySummary(date);
        String csvData = convertToCSV(summary);
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=\"daily-summary-" + date + ".csv\"")
                .body(csvData);
    }
    
    @GetMapping("/export/counter-performance")
    public ResponseEntity<String> exportCounterPerformance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // TODO: Implement CSV/Excel export
        List<CounterPerformanceDto> performance = reportService.getCounterPerformance(date);
        String csvData = convertToCSV(performance);
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=\"counter-performance-" + date + ".csv\"")
                .body(csvData);
    }
    
    private String convertToCSV(DailySummaryDto summary) {
        StringBuilder csv = new StringBuilder();
        csv.append("Date,Total Tokens,Total Passengers,Average Wait Time,Peak Hour,Peak Hour Tokens\n");
        csv.append(String.format("%s,%d,%d,%.1f,%s,%d\n",
                summary.getDate(),
                summary.getTotalTokensIssued(),
                summary.getTotalPassengersServed(),
                summary.getAverageWaitTime(),
                summary.getPeakHour(),
                summary.getPeakHourTokens()));
        return csv.toString();
    }
    
    private String convertToCSV(List<CounterPerformanceDto> performance) {
        StringBuilder csv = new StringBuilder();
        csv.append("Counter ID,Counter Name,Staff Name,Total Served,Current Queue,Average Service Time,Status,Date\n");
        for (CounterPerformanceDto dto : performance) {
            csv.append(String.format("%d,%s,%s,%d,%d,%.1f,%s,%s\n",
                    dto.getCounterId(),
                    dto.getCounterName(),
                    dto.getAssignedStaffName(),
                    dto.getTotalServedToday(),
                    dto.getCurrentQueuePosition(),
                    dto.getAverageServiceTime(),
                    dto.getStatus(),
                    dto.getDate()));
        }
        return csv.toString();
    }
} 