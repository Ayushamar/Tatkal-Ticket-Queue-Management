package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyRequestDto {
    private String aadhaarNo;
    private String station;
    private String toStation;
    private String journeyDate;
    private String trainNo;
    private List<String> coPassengers;
} 