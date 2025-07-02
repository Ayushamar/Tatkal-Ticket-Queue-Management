package com.tokenbackend.repository;

import com.tokenbackend.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer> {
    
    Optional<Journey> findByTokenNo(Integer tokenNo);
    
    @Query("SELECT COUNT(j) FROM Journey j WHERE j.counterNo = :counterNo AND j.journeyDate = :journeyDate")
    Long countByCounterNoAndJourneyDate(@Param("counterNo") Integer counterNo, @Param("journeyDate") LocalDate journeyDate);
    
    @Query("SELECT MAX(j.tokenNo) FROM Journey j")
    Optional<Integer> findMaxTokenNo();
    
    @Query("SELECT COUNT(j) FROM Journey j WHERE j.journeyDate = :journeyDate")
    Long countByJourneyDate(@Param("journeyDate") LocalDate journeyDate);
} 