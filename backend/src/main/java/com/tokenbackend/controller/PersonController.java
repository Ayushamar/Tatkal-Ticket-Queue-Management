package com.tokenbackend.controller;

import com.tokenbackend.dto.CoPassengerDto;
import com.tokenbackend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {
    
    @Autowired
    private PersonService personService;
    
    @GetMapping("/family/{aadhaarNo}")
    public ResponseEntity<List<CoPassengerDto>> getFamilyMembers(@PathVariable String aadhaarNo) {
        List<CoPassengerDto> familyMembers = personService.getFamilyMembers(aadhaarNo);
        return ResponseEntity.ok(familyMembers);
    }
} 