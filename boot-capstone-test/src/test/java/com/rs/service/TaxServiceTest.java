package com.rs.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxServiceTest {

    private final TaxService taxService = new TaxService();

    @Test
    void testCalculateAnnualTax_LowIncome() {
        Double annualSalary = 400000.0;
        Double expectedTax = 0.0;

        Double result = taxService.calculateAnnualTax(annualSalary);

        assertEquals(expectedTax, result);
    }

    @Test
    void testCalculateAnnualTax_MiddleIncome() {
        Double annualSalary = 600000.0;
        Double expectedTax = 10000.0;

        Double result = taxService.calculateAnnualTax(annualSalary);

        assertEquals(expectedTax, result);
    }

    

    

    
}
