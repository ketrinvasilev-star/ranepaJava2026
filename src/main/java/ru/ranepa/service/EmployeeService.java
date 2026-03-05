package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // считаем среднюю зп
    public double calculateAverageSalary() {
        Iterable<Employee> employees = repository.findAll();
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (Employee emp : employees) {
            sum = sum.add(emp.getSalary());
            count++;
        }

        if (count == 0) {
            return 0.0;
        }

        return sum.doubleValue() / count;
    }

    // считаем самого высокооплачиваемого сотрудника
    public Optional<Employee> findTopSpender() {
        Iterable<Employee> employees = repository.findAll();
        Employee topSpender = null;
        BigDecimal maxSalary = BigDecimal.ZERO;

        for (Employee emp : employees) {
            if (emp.getSalary().compareTo(maxSalary) > 0) {
                maxSalary = emp.getSalary();
                topSpender = emp;
            }
        }

        return Optional.ofNullable(topSpender);
    }

    // ищем всех сотрудников с должностью position
    public List<Employee> filterByPosition(String position) {
        List<Employee> result = new ArrayList<>();
        Iterable<Employee> employees = repository.findAll();

        for (Employee emp : employees) {
            if (emp.getPosition().equalsIgnoreCase(position)) {
                result.add(emp);
            }
        }

        return result;
    }

}