package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterPerformanceDto {
    private Integer counterId;
    private String counterName;
    private String assignedStaffName;
    private Integer totalServedToday;
    private Integer currentQueuePosition;
    private Double averageServiceTime;
    private String status;
    private LocalDate date;
} 