package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoPassengerDto {
    private String aadhaarNo;
    private String name;
    private Integer age;
    private String gender;
} 