package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    @Id
    @Column(name = "staff_id", length = 50)
    private String staffId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "email", length = 100, unique = true)
    private String email;
    
    @Column(name = "mobile_no", length = 15)
    private String mobileNo;
    
    @Column(name = "role", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffRole role = StaffRole.COUNTER_STAFF;
    
    @Column(name = "assigned_counter_id")
    private Integer assignedCounterId;
    
    @Column(name = "shift_start_time")
    private String shiftStartTime = "09:00";
    
    @Column(name = "shift_end_time")
    private String shiftEndTime = "17:00";
    
    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffStatus status = StaffStatus.ACTIVE;
    
    @Column(name = "total_served_today")
    private Integer totalServedToday = 0;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
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
    
    public enum StaffRole {
        ADMIN, SUPERVISOR, COUNTER_STAFF, SUPPORT
    }
    
    public enum StaffStatus {
        ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
    }
} 