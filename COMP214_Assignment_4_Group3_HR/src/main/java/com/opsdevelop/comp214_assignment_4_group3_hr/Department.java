/*
  File Name: Department.java
  Description: This class represents a Department entity in an HR management system.
               It includes fields to capture the department's unique details, such as
               its ID, name, manager, and location. The class provides a constructor
               for initializing department data and getter/setter methods for accessing
               and updating its properties.
  Group Number: 03
  Date: December 8, 2024
*/


package com.opsdevelop.comp214_assignment_4_group3_hr;

public class Department {

    //Department information fields
    private int departmentId;
    private String departmentName;
    private int managerId;
    private int locationId;


    // Constructor to initialize a new Department object with all details
    public Department(int departmentId, String departmentName, int managerId, int locationId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.managerId = managerId;
        this.locationId = locationId;
    }

    // Getters
    public int getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }
    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

}
