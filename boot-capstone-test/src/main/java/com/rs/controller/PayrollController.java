package com.rs.controller;

import com.rs.model.Employee;
import com.rs.service.EmployeeService;
import com.rs.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/history")
    public ResponseEntity<List<Map<String, Object>>> getPayrollReport() {
        List<Map<String, Object>> employeePayrolls = payrollService.getAllEmployeePayrolls();
        return new ResponseEntity<>(employeePayrolls, HttpStatus.OK);
    }
    @GetMapping("/financialReport")
    public ResponseEntity<Map<String, Object>> getFinancialReport() {
        Map<String, Object> financialReport = payrollService.getAnnualFinancialReport();
        return new ResponseEntity<>(financialReport, HttpStatus.OK);
    }
    
    @GetMapping("/payPeriodReport/{month}")
    public ResponseEntity<Map<String, Object>> getPayPeriodReport(@PathVariable String month) {
        Map<String, Object> monthlyReport = payrollService.getMonthlyFinancialReport(month);
        return new ResponseEntity<>(monthlyReport, HttpStatus.OK);
    }
    
    @GetMapping("/employeeReport/{id}")
    public ResponseEntity<Employee> getEmployeeReportById(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok().body(employee);
    }
  
}
