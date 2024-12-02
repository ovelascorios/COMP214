package com.opsdevelop.comp214_assignment_4_group3_hr;

public class Job {
    //Jobs information fields
    private String jobId;
    private String jobTitle;
    private int minSalary;
    private int maxSalary;


    // Constructor to initialize a new Jobs object with all details
    public Job(String jobId, String jobTitle, int minSalary, int maxSalary) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    // Getters
    public String getJobId() { return jobId; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobTitle() { return jobTitle; }
    public void setMinSalary(int minSalary) { this.minSalary = minSalary; }
    public int getMinSalary() { return minSalary; }
    public void setMaxSalary(int maxSalary) { this.maxSalary = maxSalary; }
    public int getMaxSalary() { return maxSalary; }
}
