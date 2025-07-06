package com.tokenbackend.controller.admin;

import com.tokenbackend.dto.StaffDto;
import com.tokenbackend.model.Staff.StaffStatus;
import com.tokenbackend.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/staff")
@CrossOrigin(origins = "http://localhost:5173")
public class StaffController {
    
    @Autowired
    private StaffService staffService;
    
    @GetMapping
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        List<StaffDto> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<StaffDto>> getActiveStaff() {
        List<StaffDto> staff = staffService.getActiveStaff();
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/counter-staff")
    public ResponseEntity<List<StaffDto>> getCounterStaff() {
        List<StaffDto> staff = staffService.getCounterStaff();
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable String staffId) {
        Optional<StaffDto> staff = staffService.getStaffById(staffId);
        return staff.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<StaffDto> getStaffByEmail(@PathVariable String email) {
        Optional<StaffDto> staff = staffService.getStaffByEmail(email);
        return staff.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto staffDto) {
        try {
            StaffDto createdStaff = staffService.createStaff(staffDto);
            return ResponseEntity.ok(createdStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffDto> updateStaff(@PathVariable String staffId, 
                                               @RequestBody StaffDto staffDto) {
        try {
            StaffDto updatedStaff = staffService.updateStaff(staffId, staffDto);
            return ResponseEntity.ok(updatedStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{staffId}/status")
    public ResponseEntity<Void> updateStaffStatus(@PathVariable String staffId, 
                                                 @RequestParam StaffStatus status) {
        staffService.updateStaffStatus(staffId, status);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{staffId}/assign-counter")
    public ResponseEntity<StaffDto> assignStaffToCounter(@PathVariable String staffId, 
                                                        @RequestParam Integer counterId) {
        try {
            StaffDto updatedStaff = staffService.assignStaffToCounter(staffId, counterId);
            return ResponseEntity.ok(updatedStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stats/total-served")
    public ResponseEntity<Integer> getTotalServedToday() {
        Integer totalServed = staffService.getTotalServedToday();
        return ResponseEntity.ok(totalServed);
    }
} 