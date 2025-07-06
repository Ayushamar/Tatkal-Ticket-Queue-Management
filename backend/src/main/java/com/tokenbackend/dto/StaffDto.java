package com.tokenbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.tokenbackend.model.Staff.StaffRole;
import com.tokenbackend.model.Staff.StaffStatus;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private String staffId;
    private String name;
    private String email;
    private String mobileNo;
    private StaffRole role;
    private Integer assignedCounterId;
    private String shiftStartTime;
    private String shiftEndTime;
    private StaffStatus status;
    private Integer totalServedToday;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 