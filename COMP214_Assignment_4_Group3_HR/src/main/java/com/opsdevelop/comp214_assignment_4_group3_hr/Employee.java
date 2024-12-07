/*
  File Name: Employee.java
  Description: This class represents an Employee entity in an HR management system.
               It includes fields for employee details such as ID, name, contact
               information, job details, and managerial relationships. The class
               provides a constructor for initializing employee data and getter/setter
               methods for accessing and modifying employee information.
  Group Number: 03
  Date: December 8, 2024
*/

package com.opsdevelop.comp214_assignment_4_group3_hr;

import java.sql.Date;

public class Employee {
    // Employee information fields
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date hireDate;
    private String job;
    private int salary;
    private String manager;
    private String department;

    // Constructor to initialize a new Employee object with all details
    public Employee(int employeeId, String firstName, String lastName, String email, String phone,
                Date hireDate,  String job, int salary, String manager, String department) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.job = job;
        this.salary = salary;
        this.manager = manager;
        this.department = department;
    }

    // Getters
    public int getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }
    public void setPhoneNumber(String phone) { this.phone = phone; }
    public String getPhone() { return phone; }
    public Date getHireDate() { return hireDate; }
    public String getJob() { return job; }
    public void setSalary(int salary) { this.salary = salary; }
    public int getSalary() { return salary; }
    public String getManager() { return manager; }
    public String getDepartment() { return department; }

}
