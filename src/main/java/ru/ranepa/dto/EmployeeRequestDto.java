package ru.ranepa.dto;

import java.time.LocalDate;

public class EmployeeRequestDto {
    private String name;
    private String position;
    private double salary;
    private LocalDate hireDate;

    public EmployeeRequestDto() {
    }

    public EmployeeRequestDto(String name, String position, double salary, LocalDate hireDate) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}