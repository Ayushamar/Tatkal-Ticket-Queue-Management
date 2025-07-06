package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueStatusDto {
    private Integer totalInQueue;
    private Map<Integer, Integer> queueByCounter;
    private Map<Integer, String> counterStatus;
    private Map<Integer, String> assignedStaff;
    private Integer estimatedWaitTime;
} 