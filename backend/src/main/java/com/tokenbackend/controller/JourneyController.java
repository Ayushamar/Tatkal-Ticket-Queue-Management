package com.tokenbackend.controller;

import com.tokenbackend.dto.JourneyRequestDto;
import com.tokenbackend.dto.JourneyResponseDto;
import com.tokenbackend.model.Journey;
import com.tokenbackend.service.JourneyService;
import com.tokenbackend.util.TokenPdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class JourneyController {
    
    @Autowired
    private JourneyService journeyService;
    
    @Autowired
    private TokenPdfGenerator pdfGenerator;
    
    @GetMapping("/stations")
    public ResponseEntity<List<String>> getStations() {
        // Static list of stations - can be moved to database later
        List<String> stations = Arrays.asList(
            "Patna Junction", "New Delhi", "Mumbai Central", "Howrah Junction",
            "Chennai Central", "Bangalore City", "Hyderabad Deccan", "Ahmedabad Junction",
            "Kolkata", "Pune Junction", "Jaipur Junction", "Lucknow Junction"
        );
        return ResponseEntity.ok(stations);
    }
    
    @PostMapping("/journey")
    public ResponseEntity<JourneyResponseDto> createJourney(@RequestBody JourneyRequestDto request) {
        try {
            JourneyResponseDto response = journeyService.createJourney(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/journey/{tokenNo}/pdf")
    public ResponseEntity<Resource> generatePdf(@PathVariable Integer tokenNo) {
        try {
            Resource pdfResource = pdfGenerator.generateTokenPdf(tokenNo);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"token-" + tokenNo + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 