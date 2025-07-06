package com.tokenbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Collections;
import com.tokenbackend.repository.PersonRepository;
import com.tokenbackend.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import com.tokenbackend.dto.CounterPassengerDto;
import com.tokenbackend.model.Journey;
import com.tokenbackend.repository.JourneyRepository;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JourneyRepository journeyRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            // Generate a real JWT token
            String token = Jwts.builder()
                .setSubject(username)
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/passengers")
    public ResponseEntity<?> getPassengers() {
        // Return all persons as passengers
        return ResponseEntity.ok(personRepository.findAll());
    }

    @GetMapping("/passengers-by-counter")
    public ResponseEntity<?> getPassengersByCounter() {
        List<Journey> journeys = journeyRepository.findAll();
        Map<Integer, List<CounterPassengerDto>> grouped = journeys.stream().map(journey -> {
            var p = journey.getMainAadhaar();
            CounterPassengerDto dto = new CounterPassengerDto();
            dto.setCounterNo(journey.getCounterNo());
            dto.setTokenNo(journey.getTokenNo());
            dto.setName(p.getName());
            dto.setMaskedAadhaar(maskAadhaar(p.getAadhaarNo()));
            dto.setJourneyDate(journey.getJourneyDate().toString());
            dto.setTrainNo(journey.getTrainNo());
            dto.setStation(journey.getStation());
            return dto;
        }).collect(Collectors.groupingBy(CounterPassengerDto::getCounterNo));
        return ResponseEntity.ok(grouped);
    }

    private String maskAadhaar(String aadhaar) {
        if (aadhaar == null || aadhaar.length() < 4) return "****";
        return "**** **** " + aadhaar.substring(aadhaar.length() - 4);
    }
} 