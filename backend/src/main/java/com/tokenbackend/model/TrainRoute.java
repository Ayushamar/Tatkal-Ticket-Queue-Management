package com.tokenbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "train_route")
@Data
public class TrainRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "train_number")
    // private Train train;

    @Column(name = "train_number")
    private String trainNumber;

    @Column(name = "station_name", length = 100, nullable = false)
    private String stationName;

    @Column(name = "stop_number", nullable = false)
    private Integer stopNumber;

    @Column(name = "station_code", length = 10, nullable = false)
    private String stationCode;

    public String getStationCode() {
        return stationCode;
    }
} 