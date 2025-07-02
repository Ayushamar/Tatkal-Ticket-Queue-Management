package com.tokenbackend.controller;

import com.tokenbackend.model.Train;
import com.tokenbackend.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TrainController {
    @Autowired
    private TrainService trainService;

    // Find trains by from/to stations
    @GetMapping("/trains")
    public List<Map<String, Object>> findTrainsByStations(@RequestParam String from, @RequestParam String to) {
        List<Train> trains = trainService.findTrainsByStations(from, to);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Train t : trains) {
            Map<String, Object> map = new HashMap<>();
            map.put("trainNumber", t.getTrainNumber());
            map.put("trainName", t.getTrainName());
            map.put("trainType", t.getTrainType());
            map.put("fromStation", t.getFromStation());
            map.put("toStation", t.getToStation());
            result.add(map);
        }
        return result;
    }

    // Get full route for a train
    @GetMapping("/train/{trainNumber}")
    public Map<String, Object> getTrainRoute(@PathVariable String trainNumber) {
        Optional<Train> trainOpt = trainService.getTrain(trainNumber);
        Map<String, Object> result = new HashMap<>();
        if (trainOpt.isPresent()) {
            Train t = trainOpt.get();
            result.put("trainNumber", t.getTrainNumber());
            result.put("trainName", t.getTrainName());
            result.put("route", trainService.getRouteForTrain(trainNumber));
        }
        return result;
    }
} 