package com.tokenbackend.service;

import com.tokenbackend.dto.JourneyRequestDto;
import com.tokenbackend.dto.JourneyResponseDto;
import com.tokenbackend.model.Journey;
import com.tokenbackend.model.Person;
import com.tokenbackend.model.CoPassenger;
import com.tokenbackend.repository.JourneyRepository;
import com.tokenbackend.repository.PersonRepository;
import com.tokenbackend.repository.CoPassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JourneyService {
    
    @Autowired
    private JourneyRepository journeyRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private CoPassengerRepository coPassengerRepository;
    
    @Transactional
    public JourneyResponseDto createJourney(JourneyRequestDto request) {
        // Parse journeyDate from String to LocalDate
        LocalDate journeyDate;
        try {
            journeyDate = LocalDate.parse(request.getJourneyDate());
        } catch (Exception e) {
            throw new RuntimeException("Invalid journeyDate format. Expected yyyy-MM-dd");
        }
        // Validate journey date (must be today + 2)
        LocalDate today = LocalDate.now();
        LocalDate allowedJourneyDate = today.plusDays(2);
        
        if (!journeyDate.equals(allowedJourneyDate)) {
            throw new RuntimeException("Journey date must be " + allowedJourneyDate + " (2 days from today)");
        }
        
        // Get main passenger
        Optional<Person> mainPerson = personRepository.findByAadhaarNo(request.getAadhaarNo());
        if (mainPerson.isEmpty()) {
            throw new RuntimeException("Main passenger not found");
        }
        
        // Get next token number for today (FIFO for the day)
        Integer tokenNo = getNextTokenNoForToday();
        
        // Assign counter and position based on gender and arrival order
        CounterAssignment assignment = assignCounterAndPosition(mainPerson.get().getGender());
        
        // Create journey
        Journey journey = new Journey();
        journey.setMainAadhaar(mainPerson.get());
        journey.setStation(request.getStation());
        journey.setToStation(request.getToStation());
        journey.setJourneyDate(journeyDate);
        journey.setTrainNo(request.getTrainNo());
        journey.setTokenNo(tokenNo);
        journey.setTokenIssueDate(today);
        journey.setCounterNo(assignment.counterNo);
        journey.setCounterPosition(assignment.position);
        
        // Save journey
        Journey savedJourney = journeyRepository.save(journey);
        
        // Add co-passengers
        if (request.getCoPassengers() != null && !request.getCoPassengers().isEmpty()) {
            List<CoPassenger> coPassengers = request.getCoPassengers().stream()
                    .map(aadhaarNo -> {
                        Optional<Person> coPerson = personRepository.findByAadhaarNo(aadhaarNo);
                        if (coPerson.isPresent()) {
                            CoPassenger coPassenger = new CoPassenger();
                            coPassenger.setJourney(savedJourney);
                            coPassenger.setAadhaarNo(coPerson.get());
                            return coPassenger;
                        }
                        return null;
                    })
                    .filter(cp -> cp != null)
                    .collect(Collectors.toList());
            
            coPassengerRepository.saveAll(coPassengers);
        }
        
        return new JourneyResponseDto(
                savedJourney.getTokenNo(),
                savedJourney.getCounterNo(),
                savedJourney.getCounterPosition()
        );
    }
    
    private Integer getNextTokenNoForToday() {
        LocalDate today = LocalDate.now();
        Optional<Integer> maxTokenNo = journeyRepository.findMaxTokenNoForDate(today);
        return maxTokenNo.orElse(0) + 1;
    }
    
    private CounterAssignment assignCounterAndPosition(String gender) {
        LocalDate today = LocalDate.now();
        
        if ("Female".equals(gender)) {
            // Female passengers go to counter 5
            Integer nextPosition = journeyRepository.findMaxPositionForCounterAndDate(5, today).orElse(0) + 1;
            return new CounterAssignment(5, nextPosition);
        } else {
            // Male passengers: fill positions sequentially across counters 1-4
            // Find the counter with the least number of passengers today
            int counterWithLeastPassengers = 1;
            long minPassengers = Long.MAX_VALUE;
            
            for (int counter = 1; counter <= 4; counter++) {
                long passengerCount = journeyRepository.countByCounterNoAndTokenIssueDate(counter, today);
                if (passengerCount < minPassengers) {
                    minPassengers = passengerCount;
                    counterWithLeastPassengers = counter;
                }
            }
            
            // Get next position for the selected counter
            Integer nextPosition = journeyRepository.findMaxPositionForCounterAndDate(counterWithLeastPassengers, today).orElse(0) + 1;
            return new CounterAssignment(counterWithLeastPassengers, nextPosition);
        }
    }
    
    // Helper class to return both counter and position
    private static class CounterAssignment {
        final Integer counterNo;
        final Integer position;
        
        CounterAssignment(Integer counterNo, Integer position) {
            this.counterNo = counterNo;
            this.position = position;
        }
    }
    
    public Optional<Journey> getJourneyByTokenNo(Integer tokenNo) {
        return journeyRepository.findByTokenNo(tokenNo);
    }
} 