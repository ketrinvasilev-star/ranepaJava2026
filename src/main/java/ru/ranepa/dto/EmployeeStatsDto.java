package ru.ranepa.dto;

public class EmployeeStatsDto {
    private double averageSalary;
    private String topEmployeeName;
    private double topEmployeeSalary;
    
    public EmployeeStatsDto() {
    }

    public EmployeeStatsDto(double averageSalary, String topEmployeeName, double topEmployeeSalary) {
        this.averageSalary = averageSalary;
        this.topEmployeeName = topEmployeeName;
        this.topEmployeeSalary = topEmployeeSalary;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public String getTopEmployeeName() {
        return topEmployeeName;
    }

    public double getTopEmployeeSalary() {
        return topEmployeeSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public void setTopEmployeeName(String topEmployeeName) {
        this.topEmployeeName = topEmployeeName;
    }

    public void setTopEmployeeSalary(double topEmployeeSalary) {
        this.topEmployeeSalary = topEmployeeSalary;
    }
}