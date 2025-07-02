package com.tokenbackend.service;

import com.tokenbackend.dto.PersonDto;
import com.tokenbackend.dto.CoPassengerDto;
import com.tokenbackend.model.Person;
import com.tokenbackend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    
    public Optional<PersonDto> getPersonDetails(String aadhaarNo) {
        Optional<Person> person = personRepository.findByAadhaarNo(aadhaarNo);
        return person.map(this::convertToDto);
    }
    
    public List<CoPassengerDto> getFamilyMembers(String aadhaarNo) {
        Optional<Person> mainPerson = personRepository.findByAadhaarNo(aadhaarNo);
        if (mainPerson.isEmpty() || mainPerson.get().getFamily() == null) {
            return List.of();
        }
        
        Integer familyId = mainPerson.get().getFamily().getFamilyId();
        List<Person> familyMembers = personRepository.findByFamilyIdExcludingAadhaar(familyId, aadhaarNo);
        
        return familyMembers.stream()
                .map(this::convertToCoPassengerDto)
                .collect(Collectors.toList());
    }
    
    private PersonDto convertToDto(Person person) {
        return new PersonDto(
                maskAadhaar(person.getAadhaarNo()),
                person.getName(),
                person.getAge(),
                person.getGender(),
                person.getAddress()
        );
    }
    
    private CoPassengerDto convertToCoPassengerDto(Person person) {
        return new CoPassengerDto(
                maskAadhaar(person.getAadhaarNo()),
                person.getName(),
                person.getAge(),
                person.getGender()
        );
    }
    
    private String maskAadhaar(String aadhaarNo) {
        if (aadhaarNo == null || aadhaarNo.length() != 12) {
            return aadhaarNo;
        }
        return "XXXX-XXXX-" + aadhaarNo.substring(8);
    }
} 