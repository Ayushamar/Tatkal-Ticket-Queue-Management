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
        // Get main passenger
        Optional<Person> mainPerson = personRepository.findByAadhaarNo(request.getAadhaarNo());
        if (mainPerson.isEmpty()) {
            throw new RuntimeException("Main passenger not found");
        }
        
        // Assign counter based on gender
        Integer counterNo = assignCounter(mainPerson.get().getGender());
        
        // Calculate next token number for the journey date (FIFO for the day)
        Long currentTokenCount = journeyRepository.countByJourneyDate(request.getJourneyDate());
        int tokenNo = currentTokenCount.intValue() + 1;

        // Calculate counter position (FIFO for the counter and day)
        Long currentPosition = journeyRepository.countByCounterNoAndJourneyDate(counterNo, request.getJourneyDate());
        int counterPosition = currentPosition.intValue() + 1;

        // Create journey
        Journey journey = new Journey();
        journey.setMainAadhaar(mainPerson.get());
        journey.setStation(request.getStation());
        journey.setJourneyDate(request.getJourneyDate());
        journey.setTrainNo(request.getTrainNo());
        journey.setCounterNo(counterNo);
        journey.setCounterPosition(counterPosition);
        journey.setTokenNo(tokenNo);
        
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
    
    private Integer assignCounter(String gender) {
        if ("Female".equals(gender)) {
            return 5;
        } else {
            // Round-robin assignment for males (counters 1-4)
            long totalMaleJourneys = 0;
            for (int i = 1; i <= 4; i++) {
                totalMaleJourneys += journeyRepository.countByCounterNoAndJourneyDate(i, java.time.LocalDate.now());
            }
            return (int) (totalMaleJourneys % 4) + 1;
        }
    }
    
    public Optional<Journey> getJourneyByTokenNo(Integer tokenNo) {
        return journeyRepository.findByTokenNo(tokenNo);
    }
} 