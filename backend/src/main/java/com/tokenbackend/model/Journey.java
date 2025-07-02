package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "journey")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journey_id")
    private Integer journeyId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_aadhaar", referencedColumnName = "aadhaar_no")
    private Person mainAadhaar;
    
    @Column(name = "station", length = 100, nullable = false)
    private String station;
    
    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;
    
    @Column(name = "train_no", length = 10)
    private String trainNo;
    
    @Column(name = "token_no", unique = true)
    private Integer tokenNo;
    
    @Column(name = "counter_no")
    private Integer counterNo;
    
    @Column(name = "counter_position")
    private Integer counterPosition;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "journey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CoPassenger> coPassengers;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 