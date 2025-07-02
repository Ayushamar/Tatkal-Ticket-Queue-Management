package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "train")
@Data
public class Train {
    @Id
    @Column(name = "train_number", length = 10)
    private String trainNumber;

    @Column(name = "train_name", length = 100, nullable = false)
    private String trainName;

    @Column(name = "train_type", length = 50)
    private String trainType;

    @Column(name = "from_station", length = 20)
    private String fromStation;

    @Column(name = "to_station", length = 20)
    private String toStation;
} 