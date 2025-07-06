package com.tokenbackend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import com.tokenbackend.model.Train;
import com.tokenbackend.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class TrainController {
    @Autowired
    private TrainService trainService;

    // Find trains by from/to stations
    @GetMapping("/trains")
    public List<Map<String, Object>> findTrainsByStations(@RequestParam String from, @RequestParam String to) {
        // Implementation of findTrainsByStations method
        return new ArrayList<>();
    }

    // Get full route for a train
    @GetMapping("/train/{trainNumber}")
    public Map<String, Object> getTrainRoute(@PathVariable String trainNumber) {
        System.out.println("TrainController: getTrainRoute called with trainNumber = " + trainNumber);
        Map<String, Object> result = new HashMap<>();
        trainService.getTrain(trainNumber).ifPresent(train -> {
            result.put("trainNumber", train.getTrainNumber());
            result.put("trainName", train.getTrainName());
            result.put("trainType", train.getTrainType());
            result.put("fromStation", train.getFromStation());
            result.put("toStation", train.getToStation());
        });
        return result;
    }
} 