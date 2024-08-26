package com.rs.service;

import org.springframework.stereotype.Service;

@Service
public class TaxService {
    public Double calculateAnnualTax(Double annualSalary) {
        if (annualSalary <= 500000) {
            return 0.0;
        } else if (annualSalary <= 700000) {
            return (annualSalary - 500000) * 0.10;
        } else if (annualSalary <= 1000000) {
            return (200000 * 0.10) + (annualSalary - 700000) * 0.20;
        } else {
            return (200000 * 0.10) + (300000 * 0.20) + (annualSalary - 1000000) * 0.30;
        }
    }
}