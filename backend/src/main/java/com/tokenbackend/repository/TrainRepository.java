package com.tokenbackend.repository;

import com.tokenbackend.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface TrainRepository extends JpaRepository<Train, String> {
} 