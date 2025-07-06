package com.tokenbackend.service;

import com.tokenbackend.dto.TokenRuleDto;
import com.tokenbackend.model.TokenRule;
import com.tokenbackend.model.TokenRule.RuleType;
import com.tokenbackend.repository.TokenRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenRuleService {
    
    @Autowired
    private TokenRuleRepository tokenRuleRepository;
    
    public List<TokenRuleDto> getAllRules() {
        return tokenRuleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<TokenRuleDto> getActiveRules() {
        return tokenRuleRepository.findActiveRulesOrderByPriority().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<TokenRuleDto> getActiveRulesByType(RuleType ruleType) {
        return tokenRuleRepository.findActiveRulesByType(ruleType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<TokenRuleDto> getRuleById(Integer ruleId) {
        return tokenRuleRepository.findById(ruleId)
                .map(this::convertToDto);
    }
    
    @Transactional
    public TokenRuleDto createRule(TokenRuleDto ruleDto) {
        TokenRule rule = new TokenRule();
        rule.setRuleName(ruleDto.getRuleName());
        rule.setRuleType(ruleDto.getRuleType());
        rule.setPriority(ruleDto.getPriority() != null ? ruleDto.getPriority() : 1);
        rule.setIsActive(ruleDto.getIsActive() != null ? ruleDto.getIsActive() : true);
        rule.setGender(ruleDto.getGender());
        rule.setTrainNumber(ruleDto.getTrainNumber());
        rule.setStation(ruleDto.getStation());
        rule.setAssignedCounter(ruleDto.getAssignedCounter());
        rule.setCounterRangeStart(ruleDto.getCounterRangeStart());
        rule.setCounterRangeEnd(ruleDto.getCounterRangeEnd());
        rule.setStartTime(ruleDto.getStartTime());
        rule.setEndTime(ruleDto.getEndTime());
        rule.setMaxTokensPerDay(ruleDto.getMaxTokensPerDay());
        rule.setDescription(ruleDto.getDescription());
        rule.setCreatedBy(ruleDto.getCreatedBy());
        
        TokenRule savedRule = tokenRuleRepository.save(rule);
        return convertToDto(savedRule);
    }
    
    @Transactional
    public TokenRuleDto updateRule(Integer ruleId, TokenRuleDto ruleDto) {
        Optional<TokenRule> existingRule = tokenRuleRepository.findById(ruleId);
        if (existingRule.isEmpty()) {
            throw new RuntimeException("Rule not found");
        }
        
        TokenRule rule = existingRule.get();
        rule.setRuleName(ruleDto.getRuleName());
        rule.setRuleType(ruleDto.getRuleType());
        rule.setPriority(ruleDto.getPriority());
        rule.setIsActive(ruleDto.getIsActive());
        rule.setGender(ruleDto.getGender());
        rule.setTrainNumber(ruleDto.getTrainNumber());
        rule.setStation(ruleDto.getStation());
        rule.setAssignedCounter(ruleDto.getAssignedCounter());
        rule.setCounterRangeStart(ruleDto.getCounterRangeStart());
        rule.setCounterRangeEnd(ruleDto.getCounterRangeEnd());
        rule.setStartTime(ruleDto.getStartTime());
        rule.setEndTime(ruleDto.getEndTime());
        rule.setMaxTokensPerDay(ruleDto.getMaxTokensPerDay());
        rule.setDescription(ruleDto.getDescription());
        
        TokenRule savedRule = tokenRuleRepository.save(rule);
        return convertToDto(savedRule);
    }
    
    @Transactional
    public void deleteRule(Integer ruleId) {
        Optional<TokenRule> rule = tokenRuleRepository.findById(ruleId);
        if (rule.isPresent()) {
            // Soft delete by setting isActive to false
            TokenRule r = rule.get();
            r.setIsActive(false);
            tokenRuleRepository.save(r);
        }
    }
    
    @Transactional
    public void toggleRuleStatus(Integer ruleId) {
        Optional<TokenRule> rule = tokenRuleRepository.findById(ruleId);
        if (rule.isPresent()) {
            TokenRule r = rule.get();
            r.setIsActive(!r.getIsActive());
            tokenRuleRepository.save(r);
        }
    }
    
    public List<TokenRuleDto> getRulesByGender(String gender) {
        return tokenRuleRepository.findActiveRulesByGender(gender).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<TokenRuleDto> getRulesByTrainNumber(String trainNumber) {
        return tokenRuleRepository.findActiveRulesByTrainNumber(trainNumber).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<TokenRuleDto> getRulesByStation(String station) {
        return tokenRuleRepository.findActiveRulesByStation(station).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private TokenRuleDto convertToDto(TokenRule rule) {
        return new TokenRuleDto(
                rule.getRuleId(),
                rule.getRuleName(),
                rule.getRuleType(),
                rule.getPriority(),
                rule.getIsActive(),
                rule.getGender(),
                rule.getTrainNumber(),
                rule.getStation(),
                rule.getAssignedCounter(),
                rule.getCounterRangeStart(),
                rule.getCounterRangeEnd(),
                rule.getStartTime(),
                rule.getEndTime(),
                rule.getMaxTokensPerDay(),
                rule.getDescription(),
                rule.getCreatedBy(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }
} 