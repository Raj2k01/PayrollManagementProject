package com.rs.service;

import com.rs.exception.ResourceNotFoundException;
import com.rs.exception.BadRequestException;
import com.rs.model.Employee;
import com.rs.model.Payroll;
import com.rs.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollService payrollService;
    @Autowired
    private TaxService taxService;

   
    public Employee addEmployee(Employee employee) {
        // Validate employee name
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new BadRequestException("Employee name cannot be null or empty");
        }

        // Calculate and set employee salary
        employee.setSalary(calculateSalary(employee));

        // Save the employee to the repository
        Employee savedEmployee = employeeRepository.save(employee);

        // Process payroll immediately after saving the employee
        Payroll payroll = payrollService.processPayroll(savedEmployee);

        // Attach payroll details to the employee object for immediate response
        savedEmployee.setPayroll(payroll);

        return savedEmployee;
    }
    
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Update the existing employee details
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setAge(updatedEmployee.getAge());
        existingEmployee.setDesignation(updatedEmployee.getDesignation());
        existingEmployee.setRating(updatedEmployee.getRating());
        existingEmployee.setSalary(calculateSalary(updatedEmployee));

        Employee savedEmployee = employeeRepository.save(existingEmployee);

        // Fetch the existing payroll entry for the employee
        Payroll existingPayroll = payrollService.getPayrollByEmployeeId(savedEmployee.getId());

        if (existingPayroll != null) {
            // Update the existing payroll
            existingPayroll.setBasicSalary(savedEmployee.getSalary());
            existingPayroll.setTaxDeductions(taxService.calculateAnnualTax(savedEmployee.getSalary() * 12) / 12);
            existingPayroll.setNetSalary(existingPayroll.getBasicSalary() - existingPayroll.getTaxDeductions());
            payrollService.updatePayroll(existingPayroll.getId(), existingPayroll);
        } else {
            // Create new payroll if it doesn't exist (if required)
            Payroll payroll = payrollService.processPayroll(savedEmployee);
            savedEmployee.setPayroll(payroll);
        }

        return savedEmployee;
    }

 
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    private Double calculateSalary(Employee employee) {
        int age = employee.getAge();
        int rating = employee.getRating();
        double baseSalary;

        
        if (age >= 21 && age <= 26) {
            baseSalary = 40000; // Default base salary 
        } else if (age >= 26 && age <= 35) {
            switch (employee.getDesignation()) {
                case "Senior Developer":
                    baseSalary = 80000;
                    break;
                case "Tech Lead":
                    baseSalary = 90000;
                    break;
                case "Architect":
                    baseSalary = 100000;
                    break;
                default:
                    baseSalary = 41000;
            }
        } else if (age >= 35 && age <= 60) {
            switch (employee.getDesignation()) {
                case "Manager":
                    baseSalary = 150000;
                    break;
                case "Senior Manager":
                    baseSalary = 200000;
                    break;
                case "Delivery Head":
                    baseSalary = 300000;
                    break;
                default:
                    baseSalary = 100000;
            }
        } else {
            baseSalary = 30000; // Default base salary for other ages
        }

        // Calculate deduction based on rating (1 is the best, 5 is the worst)
        double ratingDeduction = (rating - 1) * (baseSalary * 0.1); // 10% deduction per rating point

        return baseSalary - ratingDeduction;
            
    }
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
    }
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}
