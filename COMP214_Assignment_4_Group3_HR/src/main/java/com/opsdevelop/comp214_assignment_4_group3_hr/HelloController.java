package com.opsdevelop.comp214_assignment_4_group3_hr;

/*
  File Name: HelloController.java
  Description: A JavaFX controller class that manages a database-driven application
               to register, update, and manage players, games, and scores.
  Student's Name: Orlando Velasco Rios
  Student ID: 301368612
  Date: November 30, 2024
*/

// Import necessary libraries for handling JavaFX UI components, database operations, and date/time management
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

public class HelloController {

    // Required FXML fields


    @FXML
    private TextField registerFirstNameField, registerLastNameField, registerEmailField, registerPhoneNumberField,registerSalaryField,filterEmployeeIdField;

    @FXML
    private DatePicker registerHireDatePicker;

    @FXML
    private ComboBox<String> employeeJobComboBox,employeeManagerComboBox,employeeDepartmentComboBox;

    @FXML
    private ComboBox<String> updateJobComboBox,updateManagerComboBox,updateDepartmentComboBox;

    @FXML
    private TextField updateSalaryField;

    @FXML
    private TextField registerJobIdField, registerJobTitleField, registerMinSalaryField, registerMaxSalaryField;

    @FXML
    private TextField jobIdField,jobDescriptionField, filterJobIdField;

    @FXML
    private TableView<Job> jobsTable;
    @FXML
    private TableColumn<Job, String> jobIdColumn;
    @FXML
    private TableColumn<Job, String> jobTitleColumn;
    @FXML
    private TableColumn<Job, Integer> minSalaryColumn;
    @FXML
    private TableColumn<Job, Integer> maxSalaryColumn;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;
    @FXML
    private TableColumn<Employee, Date> hireDateColumn;
    @FXML
    private TableColumn<Employee, Integer> salaryColumn;
    @FXML
    private TableColumn<Employee, String> jobColumn;
    @FXML
    private TableColumn<Employee, String> managerColumn;
    @FXML
    private TableColumn<Employee, String> departmentColumn;

    // Connection object to interact with the database
    private Connection dbConnection;


    // ObservableLists to hold job, manager and department data.
    private ObservableList<String> jobList = FXCollections.observableArrayList();
    private ObservableList<String> managerList = FXCollections.observableArrayList();
    private ObservableList<String> departmentList = FXCollections.observableArrayList();

    private ObservableList<String> updateJobList = FXCollections.observableArrayList();
    private ObservableList<String> updateManagerList = FXCollections.observableArrayList();
    private ObservableList<String> updateDepartmentList = FXCollections.observableArrayList();

    // Constructor for HelloController
    public HelloController() {
        try {

            // Attempts to establish a connection to the database when the controller is created.
            dbConnection = HelloApplication.DatabaseConnector.connect();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to connect to the database: " + e.getMessage());
        }
    }

    // Initialize method: Called when the controller is loaded to set up components
    @FXML
    public void initialize() {
        // Load Jobs, Managers and Departments into the lists
        loadJobs();
        loadManagers();
        loadDepartments();
        loadUpdateJobs();
        loadUpdateManagers();
        loadUpdateDepartments();
        registerHireDatePicker.setValue(LocalDate.now());

        // Set the items for combo boxes using the loaded lists
        employeeJobComboBox.setItems(jobList);
        employeeManagerComboBox.setItems(managerList);
        employeeDepartmentComboBox.setItems(departmentList);


        // Set the items for combo boxes using the update loaded lists
        updateJobComboBox.setItems(updateJobList);
        updateManagerComboBox.setItems(updateManagerList);
        updateDepartmentComboBox.setItems(updateDepartmentList);


        // Set up TableView columns
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        minSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("minSalary"));
        maxSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("maxSalary"));

        // Make the SALARY column editable
        salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        salaryColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            int newSalary = event.getNewValue();
            int employeeId = employee.getEmployeeId();

            // Update the model
            employee.setSalary(newSalary);

            // Update the database
            updateDatabaseEmployeesTable("Salary", newSalary, employeeId);
        });

        // Make the PHONE column editable
        phoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumberColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            String newPhone = event.getNewValue();
            int employeeId = employee.getEmployeeId();

            // Update the model
            employee.setPhoneNumber(newPhone);

            // Update the database
            updateDatabaseEmployeesTable("Phone_Number", newPhone, employeeId);
        });

        // Make the EMAIL column editable
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            String newEmail = event.getNewValue();
            int employeeId = employee.getEmployeeId();

            // Update the model
            employee.setEmail(newEmail);

            // Update the database
            updateDatabaseEmployeesTable("Email", newEmail, employeeId);
        });

        // Make JOB_TITLE column editable
        jobTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        jobTitleColumn.setOnEditCommit(event -> {
            Job job = event.getRowValue();
            String newTitle = event.getNewValue();
            String jobId = job.getJobId();

            // Update the model
            job.setJobTitle(newTitle);

            // Update the database
            updateDatabaseJobsTable("job_title", newTitle, jobId);
        });

        // Make MIN_SALARY column editable
        minSalaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        minSalaryColumn.setOnEditCommit(event -> {
            Job job = event.getRowValue();
            int newMinSalary = event.getNewValue();
            String jobId = job.getJobId();

            // Update the model
            job.setMinSalary(newMinSalary);

            // Update the database
            updateDatabaseJobsTable("min_salary", newMinSalary, jobId);
        });

        // Make MAX_SALARY column editable
        maxSalaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        maxSalaryColumn.setOnEditCommit(event -> {
            Job job = event.getRowValue();
            int newMaxSalary = event.getNewValue();
            String jobId = job.getJobId();

            // Update the model
            job.setMaxSalary(newMaxSalary);

            // Update the database
            updateDatabaseJobsTable("max_salary", newMaxSalary, jobId);
        });

    }

    // Method to load Jobs from the database and populate the job list
    private void loadJobs() {
        jobList.clear(); // Clear any existing data in the job list
        try {
            String query = "SELECT job_id, job_title FROM hr_jobs order by 1 asc";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Process each player returned from the query
            while (rs.next()) {
                String jobId = rs.getString("job_id");
                String jobTitle = rs.getString("job_title") ;
                jobList.add(jobId + " - " + jobTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in loading players: " + e.getMessage());
        }
    }


    // Method to load Jobs from the database and populate the job list
    private void loadUpdateJobs() {
        updateJobList.clear();
        updateJobList.add("All");
        try {
            String query = "SELECT job_id, job_title FROM hr_jobs order by 1 asc";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Process each player returned from the query
            while (rs.next()) {
                String jobId = rs.getString("job_id");
                String jobTitle = rs.getString("job_title") ;
                updateJobList.add(jobId + " - " + jobTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in loading players: " + e.getMessage());
        }
    }


    // Method to load Managers from the database and populate the manager list
    private void loadManagers() {
        managerList.clear(); // Clear any existing data in the manager list
        try {
            String query = "SELECT employee_id, first_name, last_name FROM hr_employees order by 1 asc";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Process each player returned from the query
            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                managerList.add(employeeId + " - " + fullName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in loading players: " + e.getMessage());
        }
    }

    // Method to load Managers from the database and populate the manager list
    private void loadUpdateManagers() {
        updateManagerList.clear();
        updateManagerList.add("All");
        try {
            String query = "SELECT employee_id, first_name, last_name FROM hr_employees order by 1 asc";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Process each player returned from the query
            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                updateManagerList.add(employeeId + " - " + fullName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in loading players: " + e.getMessage());
        }
    }


    // Method to load Departments from the database and populate the department list
    private void loadDepartments() {
        departmentList.clear(); // Clear any existing data in the department list
        try {
            String query = "SELECT department_id, department_name FROM hr_departments order by 1 asc";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Process each player returned from the query
            while (rs.next()) {
                int departmentId = rs.getInt("department_id");
                String departmentName = rs.getString("department_name") ;
                departmentList.add(departmentId + " - " + departmentName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in loading players: " + e.getMessage());
        }
    }

    // Method to load Departments from the database and populate the department list
    private void loadUpdateDepartments() {
        updateDepartmentList.clear();
        updateDepartmentList.add("All");
        try {
            String query = "SELECT department_id, department_name FROM hr_departments order by 1 asc";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Process each player returned from the query
            while (rs.next()) {
                int departmentId = rs.getInt("department_id");
                String departmentName = rs.getString("department_name") ;
                updateDepartmentList.add(departmentId + " - " + departmentName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in loading players: " + e.getMessage());
        }
    }

    @FXML
    private void onSubmitHireEmployeeForm(ActionEvent event) {
        try {
            // Check if required fields are empty
            if (registerFirstNameField.getText().isEmpty()) {
                showError("First Name is required.");
                return;
            }
            if (registerLastNameField.getText().isEmpty()) {
                showError("Last Name is required.");
                return;
            }
            if (registerEmailField.getText().isEmpty()) {
                showError("Email is required.");
                return;
            }
//            if (registerPhoneNumberField.getText().isEmpty()) {
//                showError("Phone Number is required.");
//                return;
//            }
            if (registerHireDatePicker.getValue() == null) {
                showError("Hire Date is required.");
                return;
            }
            if (registerSalaryField.getText().isEmpty()) {
                showError("Salary is required.");
                return;
            }

            String selectedJob = employeeJobComboBox.getValue();
            String selectedManager = employeeManagerComboBox.getValue();
            String selectedDepartment = employeeDepartmentComboBox.getValue();

            // Validate the selected Job from the ComboBox
            if (selectedJob == null || selectedJob.isEmpty()) {
                showError("Please select a Job to register the new Employee.");
                return;
            }

            // Validate the selected Manager from the ComboBox
            if (selectedManager == null || selectedManager.isEmpty()) {
                showError("Please select a Manager to register the new Employee.");
                return;
            }

            // Validate the selected Department from the ComboBox
            if (selectedDepartment == null || selectedDepartment.isEmpty()) {
                showError("Please select a Department to register the new Employee.");
                return;
            }

            String selectedJobId = extractJobIdFromComboBox(selectedJob);
            int selectedManagerId = extractManagerIdFromComboBox(selectedManager);
            int selectedDepartmentId = extractDepartmentIdFromComboBox(selectedDepartment);

            // Convert LocalDate from DatePicker to java.sql.Date
            LocalDate hireDate = registerHireDatePicker.getValue();
            java.sql.Date sqlDate = java.sql.Date.valueOf(hireDate);

            // Call the stored procedure to insert the new employee
            String callQuery = "{call Employee_hire_sp(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement stmt = dbConnection.prepareCall(callQuery);

            // Set the parameters for the stored procedure
            stmt.setString(1, registerFirstNameField.getText());  // p_first_name
            stmt.setString(2, registerLastNameField.getText());   // p_last_name
            stmt.setString(3, registerEmailField.getText());      // p_email
            stmt.setInt(4, Integer.parseInt(registerSalaryField.getText()));  // p_salary
            stmt.setDate(5, sqlDate);  // p_hire_date
            stmt.setString(6, registerPhoneNumberField.getText());  // p_phone
            stmt.setString(7, selectedJobId);  // p_job_id
            stmt.setInt(8, selectedManagerId);  // p_manager_id
            stmt.setInt(9, selectedDepartmentId);  // p_department_id

            // Execute the stored procedure
            stmt.executeUpdate();

            // Show success message
            showMessage("Employee registered successfully.");

            // Clear input fields and refresh the manager list
            clearHireEmployeeForm();
            loadManagers(); // Refresh Manager list
            loadUpdateManagers();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in registering player: " + e.getMessage());
        }
    }


    // Cancel the Hire Employee form
    @FXML
    private void onCancelHireEmployeeForm(ActionEvent event) {
        Platform.exit(); // Closes the application
    }


    // Clears the input fields in the Hire Employee form
    @FXML
    private void onClearHireEmployeeForm(ActionEvent event) {

        clearHireEmployeeForm();
    }


    private void clearHireEmployeeForm() {
        registerFirstNameField.clear();
        registerLastNameField.clear();
        registerEmailField.clear();
        registerPhoneNumberField.clear();
        registerSalaryField.clear();
        registerHireDatePicker.setValue(LocalDate.now());
        employeeJobComboBox.getSelectionModel().clearSelection();
        employeeManagerComboBox.getSelectionModel().clearSelection();
        employeeDepartmentComboBox.getSelectionModel().clearSelection();

        // Set the prompt text explicitly after clearing
        employeeJobComboBox.setPromptText("Please select ...");
        employeeManagerComboBox.setPromptText("Please select ...");
        employeeDepartmentComboBox.setPromptText("Please select ...");

    }


    // Extracts the job ID from the ComboBox selection
    private String extractJobIdFromComboBox(String selectedJob) {
        String[] parts = selectedJob.split(" - ");
        return parts[0].trim(); // Return jobId as a String
    }

    // Extracts the Manager ID from the ComboBox selection
    private int extractManagerIdFromComboBox(String selectedManger) {
        String[] parts = selectedManger.split(" - ");
        String managerIdString = parts[0].trim();
        return Integer.parseInt(managerIdString);  // Converts the ID to an integer
    }

    // Extracts the Department ID from the ComboBox selection
    private int extractDepartmentIdFromComboBox(String selectedDepartment) {
        String[] parts = selectedDepartment.split(" - ");
        String departmentIdString = parts[0].trim();
        return Integer.parseInt(departmentIdString);  // Converts the ID to an integer
    }

    // Handles the registration of a new job
    @FXML
    private void onSubmitJobForm(ActionEvent event) {
        try {
            // Check if the job id field is empty
            if (registerJobIdField.getText().isEmpty()) {
                showError("Job ID is required.");
                return;
            }

            // Check if the job title field is empty
            if (registerJobTitleField.getText().isEmpty()) {
                showError("Job Title is required.");
                return;
            }

            // Check if the Min Salary field is empty
            if (registerMinSalaryField.getText().isEmpty()) {
                showError("Min Salary is required.");
                return;
            }

            // Prepare the min salary and max salary variables
            int minSalary = Integer.parseInt(registerMinSalaryField.getText());
            Integer maxSalary = null;
            if (!registerMaxSalaryField.getText().isEmpty()) {
                maxSalary = Integer.parseInt(registerMaxSalaryField.getText());
            }

            // Prepare SQL query to call the stored procedure
            String query = "{ CALL new_job(?, ?, ?, ?) }";
            CallableStatement stmt = dbConnection.prepareCall(query);

            // Set input parameters for the procedure
            stmt.setString(1, registerJobIdField.getText());  // job_id
            stmt.setString(2, registerJobTitleField.getText()); // job_title
            stmt.setInt(3, minSalary); // min_salary
            if (maxSalary != null) {
                stmt.setInt(4, maxSalary); // max_salary (input value)
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER); // Set NULL if max salary is not provided
            }

            // Execute the stored procedure
            stmt.executeUpdate();
            showMessage("Job registered successfully.");

            // Clear input fields and refresh the job list
            clearJobForm();
            loadJobs(); // Refresh job list
            loadUpdateJobs();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in registering job: " + e.getMessage());
        }
    }



    // Clears the input fields in the Job form
    @FXML
    private void onClearJobForm(ActionEvent event) {

        clearJobForm();
    }

    @FXML
    private void clearJobForm() {

        registerJobIdField.clear();
        registerJobTitleField.clear();
        registerMinSalaryField.clear();
        registerMaxSalaryField.clear();
    }


    @FXML
    private void onGetDescription() {
        // Get the job ID entered by the user
        String jobId = jobIdField.getText().trim();

        // Validate the input
        if (jobId.isEmpty()) {
            jobDescriptionField.setText("Please enter a Job ID.");
            return;
        }

        // SQL query to fetch the job title
        String query = "SELECT job_title FROM hr_jobs WHERE job_id = ?";

        try {
            // Prepare the statement using the existing dbConnection
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, jobId); // Set the job ID parameter

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Handle the result
            if (rs.next()) {
                // Fetch the job title and display it
                String jobTitle = rs.getString("job_title");
                jobDescriptionField.setText(jobTitle);
            } else {
                // No job found for the given ID
                jobDescriptionField.setText("No job found with the given ID.");
            }

            // Close resources
            rs.close();
            stmt.close();

        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
            jobDescriptionField.setText("Error retrieving job description.");
        }
    }

    @FXML
    public void onEmployeesListTabSelected() {

        // Clear previous data in the employeeTable
        employeeTable.getItems().clear();

        // Reset ComboBoxes to their prompt state
        updateJobComboBox.getSelectionModel().clearSelection();
        updateJobComboBox.setPromptText("Please select ...");

        updateManagerComboBox.getSelectionModel().clearSelection();
        updateManagerComboBox.setPromptText("Please select ...");

        updateDepartmentComboBox.getSelectionModel().clearSelection();
        updateDepartmentComboBox.setPromptText("Please select ...");

        // Clear the text fields
        filterEmployeeIdField.clear();
        updateSalaryField.clear();

        // SQL query to fetch all employee details
        String query = "SELECT " +
                "    a.Employee_id, " +
                "    a.first_name, " +
                "    a.last_name, " +
                "    a.email, " +
                "    a.phone_Number, " +
                "    a.Hire_Date, " +
                "    c.job_title, " +
                "    a.salary, " +
                "    (SELECT first_name || ' ' || last_name FROM hr_employees WHERE employee_id = a.manager_id) AS \"MANAGER\", " +
                "    b.department_name AS \"Department\" " +
                "FROM " +
                "    hr_employees a " +
                "JOIN " +
                "    hr_departments b ON a.department_id = b.department_id " +
                "JOIN " +
                "    hr_jobs c ON a.job_id = c.job_id " +
                "ORDER BY " +
                "    1 ASC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // ObservableList to hold employee data
            ObservableList<Employee> employees = FXCollections.observableArrayList();

            // Process the result set
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("Employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_Number"),
                        rs.getDate("Hire_Date"),
                        rs.getString("job_title"),
                        rs.getInt("salary"),
                        rs.getString("MANAGER"),
                        rs.getString("Department")
                );
                employees.add(employee); // Add the employee to the list
            }

            // Populate the employeeTable with the retrieved employee data
            employeeTable.setItems(employees);

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in fetching employee data: " + e.getMessage());
        }
    }

    @FXML
    private void onJobSelection(ActionEvent event) {
        filterEmployeesTable();
    }

    @FXML
    private void onManagerSelection(ActionEvent event) {
        filterEmployeesTable();
    }

    @FXML
    private void onDepartmentSelection(ActionEvent event) {
        filterEmployeesTable();
    }

    private void filterEmployeesTable() {
        String selectedJob = updateJobComboBox.getValue();
        String selectedManager = updateManagerComboBox.getValue();
        String selectedDepartment = updateDepartmentComboBox.getValue();

        StringBuilder queryBuilder = new StringBuilder("SELECT " +
                "a.Employee_id, a.first_name, a.last_name, a.email, a.phone_Number, " +
                "a.Hire_Date, c.job_title, a.salary, " +
                "(SELECT first_name || ' ' || last_name FROM hr_employees WHERE employee_id = a.manager_id) AS \"MANAGER\", " +
                "b.department_name AS \"Department\" " +
                "FROM hr_employees a " +
                "JOIN hr_departments b ON a.department_id = b.department_id " +
                "JOIN hr_jobs c ON a.job_id = c.job_id ");

        boolean hasCondition = false;

        if (selectedJob != null && !"All".equals(selectedJob)) {
            queryBuilder.append(hasCondition ? " AND " : " WHERE ").append("a.job_id = ?");
            hasCondition = true;
        }

        if (selectedManager != null && !"All".equals(selectedManager)) {
            queryBuilder.append(hasCondition ? " AND " : " WHERE ").append("a.manager_id = ?");
            hasCondition = true;
        }

        if (selectedDepartment != null && !"All".equals(selectedDepartment)) {
            queryBuilder.append(hasCondition ? " AND " : " WHERE ").append("a.department_id = ?");
        }

        queryBuilder.append(" ORDER BY a.Employee_id ASC");

        try (PreparedStatement stmt = dbConnection.prepareStatement(queryBuilder.toString())) {
            int paramIndex = 1;

            if (selectedJob != null && !"All".equals(selectedJob)) {
                stmt.setString(paramIndex++, extractJobIdFromComboBox(selectedJob));
            }

            if (selectedManager != null && !"All".equals(selectedManager)) {
                stmt.setInt(paramIndex++, extractManagerIdFromComboBox(selectedManager));
            }

            if (selectedDepartment != null && !"All".equals(selectedDepartment)) {
                stmt.setInt(paramIndex++, extractDepartmentIdFromComboBox(selectedDepartment));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList();

                while (rs.next()) {
                    Employee employee = new Employee(
                            rs.getInt("Employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_Number"),
                            rs.getDate("Hire_Date"),
                            rs.getString("job_title"),
                            rs.getInt("salary"),
                            rs.getString("MANAGER"),
                            rs.getString("Department")
                    );
                    filteredEmployees.add(employee);
                }

                employeeTable.setItems(filteredEmployees);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error filtering employees: " + e.getMessage());
        }
    }


    private void updateDatabaseEmployeesTable(String columnName, Object newValue, int employeeId) {
        String query = "UPDATE hr_employees SET " + columnName + " = ? WHERE Employee_id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setObject(1, newValue);  // Set the new value
            stmt.setInt(2, employeeId);  // Set the Employee ID

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(columnName + " updated successfully for Employee ID " + employeeId);
            } else {
                showError("Failed to update " + columnName + " for Employee ID " + employeeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error while updating " + columnName + ": " + e.getMessage());
        }
    }


    @FXML
    public void onFilterEmployee() {
        // Clear previous data in the employeeTable
        employeeTable.getItems().clear();

        // Get the Employee ID from the filter field
        String employeeIdInput = filterEmployeeIdField.getText().trim();

        // Validate the input
        if (employeeIdInput.isEmpty()) {
            showError("Please enter an Employee ID to filter.");
            return;
        }

        // SQL query to fetch employee data for the specified Employee ID
        String query = "SELECT " +
                "    a.Employee_id, " +
                "    a.first_name, " +
                "    a.last_name, " +
                "    a.email, " +
                "    a.phone_Number, " +
                "    a.Hire_Date, " +
                "    c.job_title, " +
                "    a.salary, " +
                "    (SELECT first_name || ' ' || last_name FROM hr_employees WHERE employee_id = a.manager_id) AS \"MANAGER\", " +
                "    b.department_name AS \"Department\" " +
                "FROM " +
                "    hr_employees a " +
                "JOIN " +
                "    hr_departments b ON a.department_id = b.department_id " +
                "JOIN " +
                "    hr_jobs c ON a.job_id = c.job_id " +
                "WHERE " +
                "    a.Employee_id = ?";  // Filtering by employee ID

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, employeeIdInput); // Set the Employee ID in the query

            try (ResultSet rs = stmt.executeQuery()) {
                ObservableList<Employee> employees = FXCollections.observableArrayList();

                // Process the result set
                while (rs.next()) {
                    Employee employee = new Employee(
                            rs.getInt("Employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_Number"),
                            rs.getDate("Hire_Date"),
                            rs.getString("job_title"),
                            rs.getInt("salary"),
                            rs.getString("MANAGER"),
                            rs.getString("Department")
                    );
                    employees.add(employee); // Add the employee to the list
                }

                // Handle the case where no employees are found
                if (employees.isEmpty()) {
                    showError("No employee found with the given ID.");
                } else {
                    // Populate the employeeTable with the filtered employees
                    employeeTable.setItems(employees);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in filtering employee data: " + e.getMessage());
        }
    }

    @FXML
    public void onUpdateSalary() {
        // Get the new salary value from the text field
        String newSalaryStr = updateSalaryField.getText();

        // Validate input
        if (newSalaryStr == null || newSalaryStr.trim().isEmpty()) {
            showError("Please enter a valid salary.");
            return;
        }

        int newSalary;
        try {
            newSalary = Integer.parseInt(newSalaryStr);
        } catch (NumberFormatException e) {
            showError("Invalid salary format. Please enter a numeric value.");
            return;
        }

        // Check if there are employees in the table
        ObservableList<Employee> employees = employeeTable.getItems();
        if (employees.isEmpty()) {
            showError("No employees to update. Please filter the table first.");
            return;
        }

        // Prepare the SQL update statement
        String updateQuery = "UPDATE hr_employees SET salary = ? WHERE employee_id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(updateQuery)) {
            for (Employee employee : employees) {
                // Set parameters for the prepared statement
                stmt.setInt(1, newSalary); // New salary
                stmt.setInt(2, employee.getEmployeeId()); // Employee ID
                stmt.addBatch(); // Add to batch for efficient execution
            }

            // Execute batch update
            int[] updateCounts = stmt.executeBatch();

            // Display success message
            int updatedRows = Arrays.stream(updateCounts).sum();
            showMessage(updatedRows + " employees' salaries updated successfully.");

            // Refresh the table to reflect changes
            filterEmployeesTable();
            updateSalaryField.clear();


        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error while updating salaries: " + e.getMessage());
        }
    }


    @FXML
    public void onFilterEmployeeRestart() {
        // Clear the filter input field
        filterEmployeeIdField.clear();

        // Clear previous data in the employeeTable
        employeeTable.getItems().clear();

        // SQL query to fetch all employees from the database
        String query = "SELECT " +
                "    a.Employee_id, " +
                "    a.first_name, " +
                "    a.last_name, " +
                "    a.email, " +
                "    a.phone_Number, " +
                "    a.Hire_Date, " +
                "    c.job_title, " +
                "    a.salary, " +
                "    (SELECT first_name || ' ' || last_name FROM hr_employees WHERE employee_id = a.manager_id) AS \"MANAGER\", " +
                "    b.department_name AS \"Department\" " +
                "FROM " +
                "    hr_employees a " +
                "JOIN " +
                "    hr_departments b ON a.department_id = b.department_id " +
                "JOIN " +
                "    hr_jobs c ON a.job_id = c.job_id " +
                "ORDER BY " +
                "    1 ASC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<Employee> employees = FXCollections.observableArrayList();

            // Process the result set
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("Employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_Number"),
                        rs.getDate("Hire_Date"),
                        rs.getString("job_title"),
                        rs.getInt("salary"),
                        rs.getString("MANAGER"),
                        rs.getString("Department")
                );
                employees.add(employee);
            }

            // Populate the employeeTable with all employees
            employeeTable.setItems(employees);

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in restarting the employee filter: " + e.getMessage());
        }
    }


    @FXML
    public void onJobsListTabSelected() {
        // Clear previous data in the jobsTable
        jobsTable.getItems().clear();

        // SQL query to fetch all job details
        String query = "SELECT job_id, job_title, min_salary, max_salary FROM hr_jobs ORDER BY job_id ASC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // ObservableList to hold job data
            ObservableList<Job> jobs = FXCollections.observableArrayList();

            // Process the result set
            while (rs.next()) {
                // Create a new Job object for each row
                Job job = new Job(
                        rs.getString("job_id"),
                        rs.getString("job_title"),
                        rs.getInt("min_salary"),
                        rs.getInt("max_salary")
                );
                jobs.add(job); // Add the Job object to the list
            }

            // Populate the jobsTable with the retrieved job data
            jobsTable.setItems(jobs);

        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
            showError("SQL Error in fetching jobs data: " + e.getMessage());
        }
    }


    private void updateDatabaseJobsTable(String columnName, Object newValue, String jobId) {
        String query = "UPDATE hr_jobs SET " + columnName + " = ? WHERE job_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setObject(1, newValue);
            stmt.setString(2, jobId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error while updating job: " + e.getMessage());
        }
    }


    @FXML
    public void onFilterJob() {
        // Clear previous data in the jobsTable
        jobsTable.getItems().clear();

        // Get the Job ID from the filter field
        String jobIdInput = filterJobIdField.getText().trim();

        // Validate the input
        if (jobIdInput.isEmpty()) {
            showError("Please enter a Job ID to filter.");
            return;
        }

        // SQL query to fetch job data for the specified Job ID
        String query = "SELECT job_id, job_title, min_salary, max_salary FROM hr_jobs WHERE job_id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, jobIdInput); // Set the Job ID in the query

            try (ResultSet rs = stmt.executeQuery()) {
                ObservableList<Job> jobs = FXCollections.observableArrayList();

                // Process the result set
                while (rs.next()) {
                    Job job = new Job(
                            rs.getString("job_id"),
                            rs.getString("job_title"),
                            rs.getInt("min_salary"),
                            rs.getInt("max_salary")
                    );
                    jobs.add(job);
                }

                // Handle the case where no jobs are found
                if (jobs.isEmpty()) {
                    showError("No job found with the given ID.");
                } else {
                    // Populate the jobsTable with the filtered jobs
                    jobsTable.setItems(jobs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in filtering job data: " + e.getMessage());
        }
    }

    @FXML
    public void onFilterJobRestart() {
        // Clear the filter input field
        filterJobIdField.clear();

        // Clear previous data in the jobsTable
        jobsTable.getItems().clear();

        // SQL query to fetch all jobs from the database
        String query = "SELECT job_id, job_title, min_salary, max_salary FROM hr_jobs ORDER BY job_id ASC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<Job> jobs = FXCollections.observableArrayList();

            // Process the result set
            while (rs.next()) {
                Job job = new Job(
                        rs.getString("job_id"),
                        rs.getString("job_title"),
                        rs.getInt("min_salary"),
                        rs.getInt("max_salary")
                );
                jobs.add(job);
            }

            // Populate the jobsTable with all jobs
            jobsTable.setItems(jobs);

        } catch (SQLException e) {
            e.printStackTrace();
            showError("SQL Error in restarting the job filter: " + e.getMessage());
        }
    }


    // Displays an error message in an alert dialog
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Displays an informational message in an alert dialog
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

