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

    // Find all trains that have both stations in order
    public List<Train> findTrainsByStations(String from, String to) {
        List<Train> allTrains = trainRepository.findAll();
        List<Train> result = new ArrayList<>();
        for (Train train : allTrains) {
            List<TrainRoute> route = trainRouteRepository.findByTrainNumberOrderByStopNumberAsc(train.getTrainNumber());
            int fromIdx = -1, toIdx = -1;
            for (int i = 0; i < route.size(); i++) {
                if (route.get(i).getStationName().toLowerCase().contains(from.toLowerCase())) fromIdx = i;
                if (route.get(i).getStationName().toLowerCase().contains(to.toLowerCase())) toIdx = i;
            }
            if (fromIdx != -1 && toIdx != -1 && fromIdx < toIdx) {
                result.add(train);
            }
        }
        return result;
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
        System.out.println("Looking for train: [" + trainNumber + "]");
        List<Train> result = entityManager.createNativeQuery(
            "SELECT * FROM train WHERE train_number = ?", Train.class)
            .setParameter(1, trainNumber)
            .getResultList();
        System.out.println("Native query found: " + !result.isEmpty());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
} 