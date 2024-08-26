package com.rs.service;

import com.rs.model.Employee;
import com.rs.model.Payroll;
import com.rs.repository.EmployeeRepository;
import com.rs.repository.PayrollRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PayrollServiceTest {

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaxService taxService;

    @InjectMocks
    private PayrollService payrollService;

    public PayrollServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayroll_Success() {
        Employee employee = new Employee();
        employee.setSalary(50000.0);

        Payroll payroll = new Payroll();
        payroll.setBasicSalary(50000.0);
        payroll.setTaxDeductions(5000.0);
        payroll.setNetSalary(45000.0);
        payroll.setPayDate(new Date());

        when(taxService.calculateAnnualTax(any(Double.class))).thenReturn(60000.0);
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        Payroll result = payrollService.processPayroll(employee);

        assertNotNull(result);
        assertEquals(50000.0, result.getBasicSalary());
        assertEquals(5000.0, result.getTaxDeductions());
        assertEquals(45000.0, result.getNetSalary());
    }

    @Test
    void testGetPayroll_Success() {
        Long payrollId = 1L;
        Payroll payroll = new Payroll();
        payroll.setId(payrollId);
        
        when(payrollRepository.findById(payrollId)).thenReturn(Optional.of(payroll));

        Payroll result = payrollService.getPayroll(payrollId);

        assertNotNull(result);
        assertEquals(payrollId, result.getId());
    }

    @Test
    void testUpdatePayroll_Success() {
        Long payrollId = 1L;
        Payroll existingPayroll = new Payroll();
        existingPayroll.setId(payrollId);
        existingPayroll.setBasicSalary(50000.0);
        existingPayroll.setTaxDeductions(5000.0);
        existingPayroll.setNetSalary(45000.0);
        
        Payroll updatedPayroll = new Payroll();
        updatedPayroll.setBasicSalary(60000.0);
        updatedPayroll.setTaxDeductions(6000.0);
        updatedPayroll.setNetSalary(54000.0);

        when(payrollRepository.findById(payrollId)).thenReturn(Optional.of(existingPayroll));
        when(payrollRepository.save(any(Payroll.class))).thenReturn(updatedPayroll);

        Payroll result = payrollService.updatePayroll(payrollId, updatedPayroll);

        assertNotNull(result);
        assertEquals(60000.0, result.getBasicSalary());
        assertEquals(6000.0, result.getTaxDeductions());
        assertEquals(54000.0, result.getNetSalary());
    }

    @Test
    void testDeletePayroll_Success() {
        Long payrollId = 1L;
        Payroll payroll = new Payroll();
        payroll.setId(payrollId);

        when(payrollRepository.findById(payrollId)).thenReturn(Optional.of(payroll));
        doNothing().when(payrollRepository).delete(payroll);

        payrollService.deletePayroll(payrollId);

        verify(payrollRepository, times(1)).delete(payroll);
    }

    @Test
    void testGetAllPayrolls_Success() {
        Payroll payroll1 = new Payroll();
        Payroll payroll2 = new Payroll();
        when(payrollRepository.findAll()).thenReturn(List.of(payroll1, payroll2));

        List<Payroll> payrolls = payrollService.getAllPayrolls();

        assertNotNull(payrolls);
        assertEquals(2, payrolls.size());
    }

    @Test
    void testGetAllEmployeePayrolls_Success() {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        Employee employee2 = new Employee();
        employee2.setId(2L);

        Payroll payroll1 = new Payroll();
        payroll1.setEmployee(employee1);
        Payroll payroll2 = new Payroll();
        payroll2.setEmployee(employee2);

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        when(payrollRepository.findById(1L)).thenReturn(Optional.of(payroll1));
        when(payrollRepository.findById(2L)).thenReturn(Optional.of(payroll2));

        List<Map<String, Object>> result = payrollService.getAllEmployeePayrolls();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(map -> map.get("employee") == employee1 && map.get("payroll") == payroll1));
        assertTrue(result.stream().anyMatch(map -> map.get("employee") == employee2 && map.get("payroll") == payroll2));
    }

    

    @Test
    void testGetMonthlyFinancialReport_Success() throws ParseException {
        Payroll payroll1 = new Payroll();
        payroll1.setBasicSalary(50000.0);
        payroll1.setTaxDeductions(5000.0);
        payroll1.setNetSalary(45000.0);
        payroll1.setPayDate(new SimpleDateFormat("yyyy-MM").parse("2024-07"));

        Payroll payroll2 = new Payroll();
        payroll2.setBasicSalary(60000.0);
        payroll2.setTaxDeductions(6000.0);
        payroll2.setNetSalary(54000.0);
        payroll2.setPayDate(new SimpleDateFormat("yyyy-MM").parse("2024-07"));

        when(payrollRepository.findAll()).thenReturn(List.of(payroll1, payroll2));

        Map<String, Object> report = payrollService.getMonthlyFinancialReport("2024-07");

        assertNotNull(report);
        assertEquals("2024-07", report.get("Month"));
        assertEquals(110000.0, report.get("Total Basic Salary"));
        assertEquals(11000.0, report.get("Total Tax Deductions"));
        assertEquals(99000.0, report.get("Total Net Salary"));
    }
}
