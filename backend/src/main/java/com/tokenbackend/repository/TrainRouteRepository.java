package com.tokenbackend.repository;

import com.tokenbackend.model.TrainRoute;
import com.tokenbackend.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainRouteRepository extends JpaRepository<TrainRoute, Long> {
    List<TrainRoute> findByStationName(String stationName);
    List<TrainRoute> findByTrainNumberOrderByStopNumberAsc(String trainNumber);
} 