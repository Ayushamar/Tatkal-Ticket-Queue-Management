package com.tokenbackend.repository;

import com.tokenbackend.model.TokenRule;
import com.tokenbackend.model.TokenRule.RuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TokenRuleRepository extends JpaRepository<TokenRule, Integer> {
    
    List<TokenRule> findByIsActive(Boolean isActive);
    
    List<TokenRule> findByRuleType(RuleType ruleType);
    
    List<TokenRule> findByRuleTypeAndIsActive(RuleType ruleType, Boolean isActive);
    
    @Query("SELECT tr FROM TokenRule tr WHERE tr.isActive = true ORDER BY tr.priority ASC")
    List<TokenRule> findActiveRulesOrderByPriority();
    
    @Query("SELECT tr FROM TokenRule tr WHERE tr.ruleType = :ruleType AND tr.isActive = true ORDER BY tr.priority ASC")
    List<TokenRule> findActiveRulesByType(@Param("ruleType") RuleType ruleType);
    
    @Query("SELECT tr FROM TokenRule tr WHERE tr.gender = :gender AND tr.isActive = true ORDER BY tr.priority ASC")
    List<TokenRule> findActiveRulesByGender(@Param("gender") String gender);
    
    @Query("SELECT tr FROM TokenRule tr WHERE tr.trainNumber = :trainNumber AND tr.isActive = true ORDER BY tr.priority ASC")
    List<TokenRule> findActiveRulesByTrainNumber(@Param("trainNumber") String trainNumber);
    
    @Query("SELECT tr FROM TokenRule tr WHERE tr.station = :station AND tr.isActive = true ORDER BY tr.priority ASC")
    List<TokenRule> findActiveRulesByStation(@Param("station") String station);
} 