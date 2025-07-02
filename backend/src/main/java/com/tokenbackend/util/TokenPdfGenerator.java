package com.tokenbackend.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.tokenbackend.model.Journey;
import com.tokenbackend.model.CoPassenger;
import com.tokenbackend.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Component
public class TokenPdfGenerator {
    
    @Autowired
    private JourneyService journeyService;
    
    public Resource generateTokenPdf(Integer tokenNo) throws Exception {
        Journey journey = journeyService.getJourneyByTokenNo(tokenNo)
                .orElseThrow(() -> new RuntimeException("Journey not found"));
        
        String dir = "./pdf-tokens";
        new File(dir).mkdirs();
        String filePath = dir + "/token_" + tokenNo + ".pdf";
        
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        
        // Add title
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("RAILWAY TOKEN SLIP", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        // Add token details
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
        
        document.add(new Paragraph("Token Number: " + journey.getTokenNo(), headerFont));
        document.add(new Paragraph("Counter Number: " + journey.getCounterNo(), headerFont));
        document.add(new Paragraph("Counter Position: " + journey.getCounterPosition(), headerFont));
        document.add(new Paragraph(" "));
        
        // Add journey details
        document.add(new Paragraph("Journey Details:", headerFont));
        document.add(new Paragraph("Station: " + journey.getStation(), normalFont));
        document.add(new Paragraph("Date: " + journey.getJourneyDate(), normalFont));
        if (journey.getTrainNo() != null && !journey.getTrainNo().isEmpty()) {
            document.add(new Paragraph("Train Number: " + journey.getTrainNo(), normalFont));
        }
        document.add(new Paragraph(" "));
        
        // Add main passenger details
        document.add(new Paragraph("Main Passenger:", headerFont));
        document.add(new Paragraph("Name: " + journey.getMainAadhaar().getName(), normalFont));
        document.add(new Paragraph("Aadhaar: " + maskAadhaar(journey.getMainAadhaar().getAadhaarNo()), normalFont));
        document.add(new Paragraph("Age: " + journey.getMainAadhaar().getAge(), normalFont));
        document.add(new Paragraph("Gender: " + journey.getMainAadhaar().getGender(), normalFont));
        document.add(new Paragraph("Address: " + journey.getMainAadhaar().getAddress(), normalFont));
        document.add(new Paragraph(" "));
        
        // Add co-passengers
        List<CoPassenger> coPassengers = journey.getCoPassengers();
        if (coPassengers != null && !coPassengers.isEmpty()) {
            document.add(new Paragraph("Co-Passengers:", headerFont));
            for (CoPassenger cp : coPassengers) {
                document.add(new Paragraph("• " + cp.getAadhaarNo().getName() + 
                        " (Aadhaar: " + maskAadhaar(cp.getAadhaarNo().getAadhaarNo()) + 
                        ", Age: " + cp.getAadhaarNo().getAge() + 
                        ", Gender: " + cp.getAadhaarNo().getGender() + ")", normalFont));
            }
        } else {
            document.add(new Paragraph("Co-Passengers: None", headerFont));
        }
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Generated on: " + journey.getCreatedAt(), normalFont));
        
        document.close();
        
        return new FileSystemResource(filePath);
    }
    
    private String maskAadhaar(String aadhaarNo) {
        if (aadhaarNo == null || aadhaarNo.length() != 12) {
            return aadhaarNo;
        }
        return "XXXX-XXXX-" + aadhaarNo.substring(8);
    }
} 