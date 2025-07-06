package com.tokenbackend.controller.admin;

import com.tokenbackend.dto.CounterDto;
import com.tokenbackend.model.Counter.CounterStatus;
import com.tokenbackend.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/counters")
@CrossOrigin(origins = "http://localhost:5173")
public class CounterController {
    
    @Autowired
    private CounterService counterService;
    
    @GetMapping
    public ResponseEntity<List<CounterDto>> getAllCounters() {
        List<CounterDto> counters = counterService.getAllCounters();
        return ResponseEntity.ok(counters);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CounterDto>> getActiveCounters() {
        List<CounterDto> counters = counterService.getActiveCounters();
        return ResponseEntity.ok(counters);
    }
    
    @GetMapping("/{counterId}")
    public ResponseEntity<CounterDto> getCounterById(@PathVariable Integer counterId) {
        Optional<CounterDto> counter = counterService.getCounterById(counterId);
        return counter.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CounterDto> createCounter(@RequestBody CounterDto counterDto) {
        try {
            CounterDto createdCounter = counterService.createCounter(counterDto);
            return ResponseEntity.ok(createdCounter);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{counterId}")
    public ResponseEntity<CounterDto> updateCounter(@PathVariable Integer counterId, 
                                                   @RequestBody CounterDto counterDto) {
        try {
            CounterDto updatedCounter = counterService.updateCounter(counterId, counterDto);
            return ResponseEntity.ok(updatedCounter);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{counterId}")
    public ResponseEntity<Void> deleteCounter(@PathVariable Integer counterId) {
        counterService.deleteCounter(counterId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{counterId}/status")
    public ResponseEntity<Void> updateCounterStatus(@PathVariable Integer counterId, 
                                                   @RequestParam CounterStatus status) {
        counterService.updateCounterStatus(counterId, status);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{counterId}/assign-staff")
    public ResponseEntity<CounterDto> assignStaffToCounter(@PathVariable Integer counterId, 
                                                          @RequestParam String staffId) {
        try {
            CounterDto updatedCounter = counterService.assignStaffToCounter(counterId, staffId);
            return ResponseEntity.ok(updatedCounter);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stats/total-served")
    public ResponseEntity<Integer> getTotalServedToday() {
        Integer totalServed = counterService.getTotalServedToday();
        return ResponseEntity.ok(totalServed);
    }
} 