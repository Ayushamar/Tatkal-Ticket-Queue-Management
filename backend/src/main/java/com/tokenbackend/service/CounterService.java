package com.tokenbackend.service;

import com.tokenbackend.dto.CounterDto;
import com.tokenbackend.model.Counter;
import com.tokenbackend.model.Counter.CounterStatus;
import com.tokenbackend.model.Staff;
import com.tokenbackend.repository.CounterRepository;
import com.tokenbackend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CounterService {
    
    @Autowired
    private CounterRepository counterRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    public List<CounterDto> getAllCounters() {
        return counterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<CounterDto> getActiveCounters() {
        return counterRepository.findActiveCounters().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<CounterDto> getCounterById(Integer counterId) {
        return counterRepository.findById(counterId)
                .map(this::convertToDto);
    }
    
    public Optional<CounterDto> getCounterByNumber(Integer counterNumber) {
        return counterRepository.findByCounterNumber(counterNumber)
                .map(this::convertToDto);
    }
    
    @Transactional
    public CounterDto createCounter(CounterDto counterDto) {
        // Check if counter number already exists
        if (counterRepository.findByCounterNumber(counterDto.getCounterNumber()).isPresent()) {
            throw new RuntimeException("Counter number already exists");
        }
        
        Counter counter = new Counter();
        counter.setCounterName(counterDto.getCounterName());
        counter.setCounterNumber(counterDto.getCounterNumber());
        counter.setStatus(CounterStatus.ACTIVE);
        counter.setCurrentQueuePosition(0);
        counter.setTotalServedToday(0);
        counter.setLastActivity(LocalDateTime.now());
        
        Counter savedCounter = counterRepository.save(counter);
        return convertToDto(savedCounter);
    }
    
    @Transactional
    public CounterDto updateCounter(Integer counterId, CounterDto counterDto) {
        Optional<Counter> existingCounter = counterRepository.findById(counterId);
        if (existingCounter.isEmpty()) {
            throw new RuntimeException("Counter not found");
        }
        
        Counter counter = existingCounter.get();
        counter.setCounterName(counterDto.getCounterName());
        counter.setStatus(counterDto.getStatus());
        counter.setLastActivity(LocalDateTime.now());
        
        // Update staff assignment if provided
        if (counterDto.getAssignedStaffId() != null) {
            counter.setAssignedStaffId(counterDto.getAssignedStaffId());
            staffRepository.findById(counterDto.getAssignedStaffId())
                    .ifPresent(staff -> counter.setAssignedStaffName(staff.getName()));
        }
        
        Counter savedCounter = counterRepository.save(counter);
        return convertToDto(savedCounter);
    }
    
    @Transactional
    public void deleteCounter(Integer counterId) {
        Optional<Counter> counter = counterRepository.findById(counterId);
        if (counter.isPresent()) {
            // Soft delete by setting status to INACTIVE
            Counter c = counter.get();
            c.setStatus(CounterStatus.INACTIVE);
            c.setLastActivity(LocalDateTime.now());
            counterRepository.save(c);
        }
    }
    
    @Transactional
    public CounterDto assignStaffToCounter(Integer counterId, String staffId) {
        Optional<Counter> counter = counterRepository.findById(counterId);
        Optional<Staff> staff = staffRepository.findById(staffId);
        
        if (counter.isEmpty()) {
            throw new RuntimeException("Counter not found");
        }
        if (staff.isEmpty()) {
            throw new RuntimeException("Staff not found");
        }
        
        Counter c = counter.get();
        Staff s = staff.get();
        
        // Remove staff from any other counter
        List<Counter> existingCounters = counterRepository.findByAssignedStaffId(staffId);
        for (Counter existingCounter : existingCounters) {
            existingCounter.setAssignedStaffId(null);
            existingCounter.setAssignedStaffName(null);
            existingCounter.setLastActivity(LocalDateTime.now());
            counterRepository.save(existingCounter);
        }
        
        // Assign to new counter
        c.setAssignedStaffId(staffId);
        c.setAssignedStaffName(s.getName());
        c.setLastActivity(LocalDateTime.now());
        
        Counter savedCounter = counterRepository.save(c);
        return convertToDto(savedCounter);
    }
    
    @Transactional
    public void updateCounterStatus(Integer counterId, CounterStatus status) {
        Optional<Counter> counter = counterRepository.findById(counterId);
        if (counter.isPresent()) {
            Counter c = counter.get();
            c.setStatus(status);
            c.setLastActivity(LocalDateTime.now());
            counterRepository.save(c);
        }
    }
    
    @Transactional
    public void incrementServedCount(Integer counterId) {
        Optional<Counter> counter = counterRepository.findById(counterId);
        if (counter.isPresent()) {
            Counter c = counter.get();
            c.setTotalServedToday(c.getTotalServedToday() + 1);
            c.setLastActivity(LocalDateTime.now());
            counterRepository.save(c);
        }
    }
    
    public Integer getTotalServedToday() {
        Integer total = counterRepository.getTotalServedToday();
        return total != null ? total : 0;
    }
    
    private CounterDto convertToDto(Counter counter) {
        return new CounterDto(
                counter.getCounterId(),
                counter.getCounterName(),
                counter.getCounterNumber(),
                counter.getStatus(),
                counter.getAssignedStaffId(),
                counter.getAssignedStaffName(),
                counter.getCurrentQueuePosition(),
                counter.getTotalServedToday(),
                counter.getLastActivity(),
                counter.getCreatedAt(),
                counter.getUpdatedAt()
        );
    }
} 