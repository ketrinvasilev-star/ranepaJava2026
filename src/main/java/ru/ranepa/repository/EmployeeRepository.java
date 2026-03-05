package ru.ranepa.repository;

import ru.ranepa.model.Employee;
import java.util.Optional;

// создание скелета БД
public interface EmployeeRepository {
    String save(Employee employee);
    Optional<Employee> findById(long id);
    Iterable<Employee> findAll();
    String delete(long id);
}