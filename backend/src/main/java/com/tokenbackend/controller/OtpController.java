package com.tokenbackend.controller;

import com.tokenbackend.dto.OtpRequestDto;
import com.tokenbackend.dto.OtpVerifyDto;
import com.tokenbackend.dto.PersonDto;
import com.tokenbackend.service.OtpService;
import com.tokenbackend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "http://localhost:5173")
public class OtpController {
    
    @Autowired
    private OtpService otpService;
    
    @Autowired
    private PersonService personService;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDto request) {
        boolean success = otpService.sendOtp(request.getAadhaarNo());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Aadhaar number not found");
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyDto request) {
        boolean isValid = otpService.verifyOtp(request.getAadhaarNo(), request.getOtp());
        if (isValid) {
            return personService.getPersonDetails(request.getAadhaarNo())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }
} 