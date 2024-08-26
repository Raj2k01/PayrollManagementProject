package com.rs.service;

import com.rs.model.Employee;
import com.rs.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeById_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(employeeId);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(employeeId));
    }

    
    @Test
    void testSaveEmployee_Success() {
        Employee employee = new Employee();
        employee.setName("John Doe");

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.saveEmployee(employee);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

   

    @Test
    void testUpdateEmployee_NotFound() {
        Long employeeId = 1L;
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Jane Doe");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.updateEmployee(employeeId, updatedEmployee));
    }

    @Test
    void testDeleteEmployee_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(employeeId));
    }
}
