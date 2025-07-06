package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryDto {
    private LocalDate date;
    private Integer totalTokensIssued;
    private Integer totalPassengersServed;
    private Map<Integer, Integer> tokensByCounter;
    private Map<String, Integer> tokensByStation;
    private Map<String, Integer> tokensByGender;
    private Double averageWaitTime;
    private Integer peakHourTokens;
    private String peakHour;
} 