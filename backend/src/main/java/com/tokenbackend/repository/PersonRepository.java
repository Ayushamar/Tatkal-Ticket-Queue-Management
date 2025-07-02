package com.tokenbackend.repository;

import com.tokenbackend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    
    Optional<Person> findByAadhaarNo(String aadhaarNo);
    
    @Query("SELECT p FROM Person p WHERE p.family.familyId = :familyId AND p.aadhaarNo != :excludeAadhaar")
    List<Person> findByFamilyIdExcludingAadhaar(@Param("familyId") Integer familyId, @Param("excludeAadhaar") String excludeAadhaar);
    
    @Query("SELECT p.mobileNo FROM Person p WHERE p.aadhaarNo = :aadhaarNo")
    Optional<String> findMobileNoByAadhaarNo(@Param("aadhaarNo") String aadhaarNo);
} 