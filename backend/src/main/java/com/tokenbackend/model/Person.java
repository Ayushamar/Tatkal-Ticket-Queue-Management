package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @Column(name = "aadhaar_no", length = 12)
    private String aadhaarNo;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "mobile_no", length = 10, nullable = false)
    private String mobileNo;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;
} 