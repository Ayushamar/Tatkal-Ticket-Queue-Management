package com.tokenbackend.service;

import com.tokenbackend.dto.StaffDto;
import com.tokenbackend.model.Staff;
import com.tokenbackend.model.Staff.StaffRole;
import com.tokenbackend.model.Staff.StaffStatus;
import com.tokenbackend.repository.StaffRepository;
import com.tokenbackend.repository.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private CounterRepository counterRepository;
    
    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<StaffDto> getActiveStaff() {
        return staffRepository.findActiveStaff().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<StaffDto> getCounterStaff() {
        return staffRepository.findActiveCounterStaff().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<StaffDto> getStaffById(String staffId) {
        return staffRepository.findById(staffId)
                .map(this::convertToDto);
    }
    
    public Optional<StaffDto> getStaffByEmail(String email) {
        return staffRepository.findByEmail(email)
                .map(this::convertToDto);
    }
    
    @Transactional
    public StaffDto createStaff(StaffDto staffDto) {
        // Check if staff ID or email already exists
        if (staffRepository.findById(staffDto.getStaffId()).isPresent()) {
            throw new RuntimeException("Staff ID already exists");
        }
        if (staffRepository.findByEmail(staffDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        Staff staff = new Staff();
        staff.setStaffId(staffDto.getStaffId());
        staff.setName(staffDto.getName());
        staff.setEmail(staffDto.getEmail());
        staff.setMobileNo(staffDto.getMobileNo());
        staff.setRole(staffDto.getRole() != null ? staffDto.getRole() : StaffRole.COUNTER_STAFF);
        staff.setShiftStartTime(staffDto.getShiftStartTime() != null ? staffDto.getShiftStartTime() : "09:00");
        staff.setShiftEndTime(staffDto.getShiftEndTime() != null ? staffDto.getShiftEndTime() : "17:00");
        staff.setStatus(StaffStatus.ACTIVE);
        staff.setTotalServedToday(0);
        
        Staff savedStaff = staffRepository.save(staff);
        return convertToDto(savedStaff);
    }
    
    @Transactional
    public StaffDto updateStaff(String staffId, StaffDto staffDto) {
        Optional<Staff> existingStaff = staffRepository.findById(staffId);
        if (existingStaff.isEmpty()) {
            throw new RuntimeException("Staff not found");
        }
        
        Staff staff = existingStaff.get();
        staff.setName(staffDto.getName());
        staff.setEmail(staffDto.getEmail());
        staff.setMobileNo(staffDto.getMobileNo());
        staff.setRole(staffDto.getRole());
        staff.setShiftStartTime(staffDto.getShiftStartTime());
        staff.setShiftEndTime(staffDto.getShiftEndTime());
        staff.setStatus(staffDto.getStatus());
        
        Staff savedStaff = staffRepository.save(staff);
        return convertToDto(savedStaff);
    }
    
    @Transactional
    public void deleteStaff(String staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            // Soft delete by setting status to TERMINATED
            Staff s = staff.get();
            s.setStatus(StaffStatus.TERMINATED);
            staffRepository.save(s);
        }
    }
    
    @Transactional
    public StaffDto assignStaffToCounter(String staffId, Integer counterId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isEmpty()) {
            throw new RuntimeException("Staff not found");
        }
        
        Staff s = staff.get();
        s.setAssignedCounterId(counterId);
        
        Staff savedStaff = staffRepository.save(s);
        return convertToDto(savedStaff);
    }
    
    @Transactional
    public void updateStaffStatus(String staffId, StaffStatus status) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            Staff s = staff.get();
            s.setStatus(status);
            staffRepository.save(s);
        }
    }
    
    @Transactional
    public void incrementServedCount(String staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            Staff s = staff.get();
            s.setTotalServedToday(s.getTotalServedToday() + 1);
            staffRepository.save(s);
        }
    }
    
    public Integer getTotalServedToday() {
        Integer total = staffRepository.getTotalServedToday();
        return total != null ? total : 0;
    }
    
    private StaffDto convertToDto(Staff staff) {
        return new StaffDto(
                staff.getStaffId(),
                staff.getName(),
                staff.getEmail(),
                staff.getMobileNo(),
                staff.getRole(),
                staff.getAssignedCounterId(),
                staff.getShiftStartTime(),
                staff.getShiftEndTime(),
                staff.getStatus(),
                staff.getTotalServedToday(),
                staff.getLastLogin(),
                staff.getCreatedAt(),
                staff.getUpdatedAt()
        );
    }
} 