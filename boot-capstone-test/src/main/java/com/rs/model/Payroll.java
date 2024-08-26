package com.rs.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    private Double basicSalary;
    private Double taxDeductions;
    private Double netSalary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payDate;

    public Payroll() {
        // Default constructor
    }

    public Payroll(Long id, Employee employee, Double basicSalary, Double taxDeductions, Double netSalary, Date payDate) {
        this.id = id;
        this.employee = employee;
        this.basicSalary = basicSalary;
        this.taxDeductions = taxDeductions;
        this.netSalary = netSalary;
        this.payDate = payDate;
    }
   

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getTaxDeductions() {
		return taxDeductions;
	}

	public void setTaxDeductions(Double taxDeductions) {
		this.taxDeductions = taxDeductions;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

    
    
    
}
