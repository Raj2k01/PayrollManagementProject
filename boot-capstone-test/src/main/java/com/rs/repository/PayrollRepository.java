package com.rs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.rs.model.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
	 Optional<Payroll> findByEmployeeId(Long employeeId);
}