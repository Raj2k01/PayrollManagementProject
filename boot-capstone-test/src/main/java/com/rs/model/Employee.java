package com.rs.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String designation;
    private Integer rating;
    private Double salary;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Payroll payroll;

    public Employee() {
        // Default constructor
    }

    public Employee(Long id, String name, Integer age, String designation, Integer rating, Double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.designation = designation;
        this.rating = rating;
        this.salary = salary;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Payroll getPayroll() {
		return payroll;
	}

	public void setPayroll(Payroll payroll) {
		this.payroll = payroll;
	}


    
    
}
