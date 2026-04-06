package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private Map<Long, Employee> employees = new HashMap<>();
    private long nextId = 1;

    @Override
    public String save(Employee employee) {
        if (employee == null) {
            return "Сотрудник не может быть null";
        }
        // тут присваиваем id чтобы добавить в БД
        if (employee.getId() == null) {
            employee.setId(nextId);
            nextId++;
        }
        employees.put(employee.getId(), employee);
        return "Сотрудник " + employee.getId() + " " + employee.getName() + " добавлен";
    }

    @Override
    public Optional<Employee> findById(long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public Iterable<Employee> findAll() {
        return employees.values();
    }

    @Override
    public String delete(long id) {
        if (employees.containsKey(id)) {
            employees.remove(id);
            return "Сотрудник с ID " + id + " удален";
        }
        return "Сотрудник с ID " + id + " не найден";
    }


}