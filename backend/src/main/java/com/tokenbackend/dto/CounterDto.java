package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.tokenbackend.model.Counter.CounterStatus;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterDto {
    private Integer counterId;
    private String counterName;
    private Integer counterNumber;
    private CounterStatus status;
    private String assignedStaffId;
    private String assignedStaffName;
    private Integer currentQueuePosition;
    private Integer totalServedToday;
    private LocalDateTime lastActivity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 