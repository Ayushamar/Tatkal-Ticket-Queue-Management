package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.tokenbackend.model.TokenRule.RuleType;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRuleDto {
    private Integer ruleId;
    private String ruleName;
    private RuleType ruleType;
    private Integer priority;
    private Boolean isActive;
    private String gender;
    private String trainNumber;
    private String station;
    private Integer assignedCounter;
    private Integer counterRangeStart;
    private Integer counterRangeEnd;
    private String startTime;
    private String endTime;
    private Integer maxTokensPerDay;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 