package com.tokenbackend.controller.admin;

import com.tokenbackend.dto.TokenRuleDto;
import com.tokenbackend.model.TokenRule.RuleType;
import com.tokenbackend.service.TokenRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/rules")
@CrossOrigin(origins = "http://localhost:5173")
public class TokenRuleController {
    
    @Autowired
    private TokenRuleService tokenRuleService;
    
    @GetMapping
    public ResponseEntity<List<TokenRuleDto>> getAllRules() {
        List<TokenRuleDto> rules = tokenRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<TokenRuleDto>> getActiveRules() {
        List<TokenRuleDto> rules = tokenRuleService.getActiveRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/type/{ruleType}")
    public ResponseEntity<List<TokenRuleDto>> getRulesByType(@PathVariable RuleType ruleType) {
        List<TokenRuleDto> rules = tokenRuleService.getActiveRulesByType(ruleType);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<TokenRuleDto>> getRulesByGender(@PathVariable String gender) {
        List<TokenRuleDto> rules = tokenRuleService.getRulesByGender(gender);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/train/{trainNumber}")
    public ResponseEntity<List<TokenRuleDto>> getRulesByTrainNumber(@PathVariable String trainNumber) {
        List<TokenRuleDto> rules = tokenRuleService.getRulesByTrainNumber(trainNumber);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/station/{station}")
    public ResponseEntity<List<TokenRuleDto>> getRulesByStation(@PathVariable String station) {
        List<TokenRuleDto> rules = tokenRuleService.getRulesByStation(station);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/{ruleId}")
    public ResponseEntity<TokenRuleDto> getRuleById(@PathVariable Integer ruleId) {
        Optional<TokenRuleDto> rule = tokenRuleService.getRuleById(ruleId);
        return rule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TokenRuleDto> createRule(@RequestBody TokenRuleDto ruleDto) {
        try {
            TokenRuleDto createdRule = tokenRuleService.createRule(ruleDto);
            return ResponseEntity.ok(createdRule);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{ruleId}")
    public ResponseEntity<TokenRuleDto> updateRule(@PathVariable Integer ruleId, 
                                                  @RequestBody TokenRuleDto ruleDto) {
        try {
            TokenRuleDto updatedRule = tokenRuleService.updateRule(ruleId, ruleDto);
            return ResponseEntity.ok(updatedRule);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Integer ruleId) {
        tokenRuleService.deleteRule(ruleId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{ruleId}/toggle")
    public ResponseEntity<Void> toggleRuleStatus(@PathVariable Integer ruleId) {
        tokenRuleService.toggleRuleStatus(ruleId);
        return ResponseEntity.ok().build();
    }
} 