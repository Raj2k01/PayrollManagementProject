package com.rs.service;

import com.rs.model.Employee;
import com.rs.model.Payroll;
import com.rs.repository.EmployeeRepository;
import com.rs.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaxService taxService;

    public Payroll processPayroll(Employee employee) {
        Double basicSalary = employee.getSalary();
        Double annualSalary = basicSalary * 12;
        Double taxDeductions = taxService.calculateAnnualTax(annualSalary) / 12;
        Double netSalary = basicSalary - taxDeductions;

        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setBasicSalary(basicSalary);
        payroll.setTaxDeductions(taxDeductions);
        payroll.setNetSalary(netSalary);
        payroll.setPayDate(new Date());

        return payrollRepository.save(payroll);
    }

    public Payroll getPayroll(Long id) {
        return payrollRepository.findById(id).orElseThrow(() -> new RuntimeException("Payroll not found"));
    }

    public Payroll updatePayroll(Long id, Payroll payrollDetails) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new RuntimeException("Payroll not found"));
        payroll.setBasicSalary(payrollDetails.getBasicSalary());
        payroll.setTaxDeductions(payrollDetails.getTaxDeductions());
        payroll.setNetSalary(payrollDetails.getNetSalary());
        payroll.setPayDate(payrollDetails.getPayDate());
        return payrollRepository.save(payroll);
    }

    public void deletePayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new RuntimeException("Payroll not found"));
        payrollRepository.delete(payroll);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }
    
    public List<Map<String, Object>> getAllEmployeePayrolls() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        
        for (Employee employee : employees) {
            Payroll payroll = payrollRepository.findById(employee.getId()).orElse(null);
            if (payroll != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("employee", employee);
                map.put("payroll", payroll);
                result.add(map);
            }
        }
        
        return result;
    }
    
    public Map<String, Object> getAnnualFinancialReport() {
        List<Payroll> allPayrolls = payrollRepository.findAll();
        double totalBasicSalary = 0;
        double totalTaxDeductions = 0;
        double totalNetSalary = 0;

        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        for (Payroll payroll : allPayrolls) {
            totalBasicSalary += payroll.getBasicSalary() * 12;
            totalTaxDeductions += payroll.getTaxDeductions() * 12;
            totalNetSalary += payroll.getNetSalary() * 12;
        }

        double totalGrossSalary = totalBasicSalary + totalTaxDeductions;

        Map<String, Object> report = new HashMap<>();
        report.put("Year", currentYear);
        report.put("Total Basic Salary", totalBasicSalary);
        report.put("Total Tax Deductions", totalTaxDeductions);
        report.put("Total Net Salary", totalNetSalary);
        report.put("Total Gross Salary", totalGrossSalary);

        return report;
    }
    public Payroll getPayrollByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).orElse(null);
    }
    
    public Map<String, Object> getMonthlyFinancialReport(String month) {
        List<Payroll> allPayrolls = payrollRepository.findAll();
        double totalBasicSalary = 0;
        double totalTaxDeductions = 0;
        double totalNetSalary = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = sdf.parse(month);
            calendar.setTime(date);
            int monthValue = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            for (Payroll payroll : allPayrolls) {
                Calendar payrollCalendar = Calendar.getInstance();
                payrollCalendar.setTime(payroll.getPayDate());
                if (payrollCalendar.get(Calendar.MONTH) == monthValue &&
                    payrollCalendar.get(Calendar.YEAR) == year) {
                    totalBasicSalary += payroll.getBasicSalary();
                    totalTaxDeductions += payroll.getTaxDeductions();
                    totalNetSalary += payroll.getNetSalary();
                }
            }
            
            Map<String, Object> report = new HashMap<>();
            report.put("Month", month);
            report.put("Total Basic Salary", totalBasicSalary);
            report.put("Total Tax Deductions", totalTaxDeductions);
            report.put("Total Net Salary", totalNetSalary);

            return report;
        } catch (ParseException e) {
            throw new RuntimeException("Invalid month format, use yyyy-MM");
        }
    }
}
