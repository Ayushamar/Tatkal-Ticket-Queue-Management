package com.tokenbackend.repository;

import com.tokenbackend.model.Staff;
import com.tokenbackend.model.Staff.StaffRole;
import com.tokenbackend.model.Staff.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    
    Optional<Staff> findByEmail(String email);
    
    List<Staff> findByRole(StaffRole role);
    
    List<Staff> findByStatus(StaffStatus status);
    
    Optional<Staff> findByAssignedCounterId(Integer counterId);
    
    @Query("SELECT s FROM Staff s WHERE s.status = 'ACTIVE' ORDER BY s.name")
    List<Staff> findActiveStaff();
    
    @Query("SELECT s FROM Staff s WHERE s.role = 'COUNTER_STAFF' AND s.status = 'ACTIVE'")
    List<Staff> findActiveCounterStaff();
    
    @Query("SELECT s FROM Staff s WHERE s.assignedCounterId = :counterId AND s.status = 'ACTIVE'")
    Optional<Staff> findActiveStaffByCounterId(@Param("counterId") Integer counterId);
    
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.status = 'ACTIVE'")
    Long countActiveStaff();
    
    @Query("SELECT SUM(s.totalServedToday) FROM Staff s WHERE s.status = 'ACTIVE'")
    Integer getTotalServedToday();
    
    @Query("SELECT s.staffId, s.name, s.role, s.assignedCounterId, s.totalServedToday, s.lastLogin, s.status FROM Staff s WHERE s.status = 'ACTIVE'")
    List<Object[]> getStaffStats(@Param("date") LocalDate date);
} 