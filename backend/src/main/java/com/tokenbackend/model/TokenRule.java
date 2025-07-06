package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer ruleId;
    
    @Column(name = "rule_name", length = 100, nullable = false)
    private String ruleName;
    
    @Column(name = "rule_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleType ruleType;
    
    @Column(name = "priority", nullable = false)
    private Integer priority = 1;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    // Gender-based rule
    @Column(name = "gender")
    private String gender;
    
    // Train-based rule
    @Column(name = "train_number", length = 10)
    private String trainNumber;
    
    // Station-based rule
    @Column(name = "station", length = 100)
    private String station;
    
    // Counter assignment
    @Column(name = "assigned_counter")
    private Integer assignedCounter;
    
    @Column(name = "counter_range_start")
    private Integer counterRangeStart;
    
    @Column(name = "counter_range_end")
    private Integer counterRangeEnd;
    
    // Time-based rules
    @Column(name = "start_time")
    private String startTime;
    
    @Column(name = "end_time")
    private String endTime;
    
    @Column(name = "max_tokens_per_day")
    private Integer maxTokensPerDay;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_by", length = 50)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum RuleType {
        GENDER_BASED, TRAIN_BASED, STATION_BASED, TIME_BASED, CUSTOM
    }
} 