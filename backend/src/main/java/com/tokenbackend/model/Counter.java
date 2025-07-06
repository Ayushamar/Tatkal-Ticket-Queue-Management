package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "counter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counter_id")
    private Integer counterId;
    
    @Column(name = "counter_name", length = 100, nullable = false)
    private String counterName;
    
    @Column(name = "counter_number", unique = true, nullable = false)
    private Integer counterNumber;
    
    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private CounterStatus status = CounterStatus.ACTIVE;
    
    @Column(name = "assigned_staff_id", length = 50)
    private String assignedStaffId;
    
    @Column(name = "assigned_staff_name", length = 100)
    private String assignedStaffName;
    
    @Column(name = "current_queue_position")
    private Integer currentQueuePosition = 0;
    
    @Column(name = "total_served_today")
    private Integer totalServedToday = 0;
    
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastActivity = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum CounterStatus {
        ACTIVE, INACTIVE, BREAK, MAINTENANCE
    }
} 