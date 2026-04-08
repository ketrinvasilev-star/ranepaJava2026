package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // сохранем сотрудника
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    // ищем по ID
    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    // все сотрудники
    public List<Employee> findAll() {
        return repository.findAll();
    }

    // удаляем по ID
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ищем по должности
    public List<Employee> findByPosition(String position) {
        return repository.findByPosition(position);
    }

    // ищем с зарплатой больше или равной
    public List<Employee> findBySalaryGreaterThanEqual(BigDecimal salary) {
        return repository.findBySalaryGreaterThanEqual(salary);
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
}