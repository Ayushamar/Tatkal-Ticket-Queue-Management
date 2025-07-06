package com.tokenbackend.service;

import com.tokenbackend.model.Train;
import com.tokenbackend.model.TrainRoute;
import com.tokenbackend.repository.TrainRepository;
import com.tokenbackend.repository.TrainRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class TrainService {
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TrainRouteRepository trainRouteRepository;
    @PersistenceContext
    private EntityManager entityManager;

    // Find all trains that have both stations in order using a native SQL query for reliability
    public List<Train> findTrainsByStations(String from, String to) {
        String fromCode = from.trim().toUpperCase();
        String toCode = to.trim().toUpperCase();
        // Native SQL: find train_numbers where fromStation stop_number < toStation stop_number
        String sql = "SELECT tr1.train_number FROM train_route tr1 " +
                "JOIN train_route tr2 ON tr1.train_number = tr2.train_number " +
                "WHERE tr1.station_code = :fromCode AND tr2.station_code = :toCode AND tr1.stop_number < tr2.stop_number";
        List<String> trainNumbers = entityManager.createNativeQuery(sql)
                .setParameter("fromCode", fromCode)
                .setParameter("toCode", toCode)
                .getResultList();
        System.out.println("[DEBUG] findTrainsByStations from: " + fromCode + ", to: " + toCode + ", found: " + trainNumbers);
        if (trainNumbers.isEmpty()) return new ArrayList<>();
        // Fetch Train entities for these trainNumbers
        return trainRepository.findAllById(trainNumbers);
    }

    // Get full route for a train
    public List<Map<String, String>> getRouteForTrain(String trainNumber) {
        List<TrainRoute> route = trainRouteRepository.findByTrainNumberOrderByStopNumberAsc(trainNumber);
        return route.stream()
            .map(r -> {
                Map<String, String> map = new HashMap<>();
                map.put("code", r.getStationCode());
                map.put("name", r.getStationName());
                return map;
            })
            .collect(Collectors.toList());
    }

    public Optional<Train> getTrain(String trainNumber) {
        String trimmed = trainNumber.trim();
        System.out.println("Looking for train: [" + trimmed + "]");
        Optional<Train> train = trainRepository.findById(trimmed);
        System.out.println("Repository found: " + train.isPresent());
        return train;
    }

    // Get all trains for testing
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
} 