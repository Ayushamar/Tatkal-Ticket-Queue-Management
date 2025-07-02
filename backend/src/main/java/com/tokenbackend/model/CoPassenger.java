package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "co_passenger")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journey_id")
    private Journey journey;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aadhaar_no", referencedColumnName = "aadhaar_no")
    private Person aadhaarNo;
} 