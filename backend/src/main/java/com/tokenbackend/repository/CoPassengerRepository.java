package com.tokenbackend.repository;

import com.tokenbackend.model.CoPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CoPassengerRepository extends JpaRepository<CoPassenger, Integer> {
    
    List<CoPassenger> findByJourneyJourneyId(Integer journeyId);
} 