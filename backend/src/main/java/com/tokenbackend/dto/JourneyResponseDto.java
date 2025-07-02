package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyResponseDto {
    private Integer tokenNo;
    private Integer counterNo;
    private Integer counterPosition;
} 