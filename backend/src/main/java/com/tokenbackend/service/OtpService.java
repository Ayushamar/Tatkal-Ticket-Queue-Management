package com.tokenbackend.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tokenbackend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    
    @Autowired
    private PersonRepository personRepository;
    
    private final Cache<String, String> otpCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(3))
            .build();
    
    public boolean sendOtp(String aadhaarNo) {
        Optional<String> mobileNo = personRepository.findMobileNoByAadhaarNo(aadhaarNo);
        if (mobileNo.isEmpty()) {
            return false;
        }
        
        String otp = generateOtp();
        otpCache.put(aadhaarNo, otp);
        
        // Print OTP to console for development/testing
        System.out.println("OTP for Aadhaar " + aadhaarNo + ": " + otp);
        
        return true;
    }
    
    public boolean verifyOtp(String aadhaarNo, String otp) {
        String storedOtp = otpCache.getIfPresent(aadhaarNo);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpCache.invalidate(aadhaarNo);
            return true;
        }
        return false;
    }
    
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
} 