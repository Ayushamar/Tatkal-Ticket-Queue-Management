package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffPerformanceDto {
    private String staffId;
    private String staffName;
    private String role;
    private Integer assignedCounterId;
    private Integer totalServedToday;
    private Double averageServiceTime;
    private LocalDateTime lastActivity;
    private String status;
} 