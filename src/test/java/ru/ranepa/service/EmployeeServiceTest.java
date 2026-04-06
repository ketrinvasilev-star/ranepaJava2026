package ru.ranepa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.repository.EmployeeRepositoryImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EmployeeRepositoryImpl();
        service = new EmployeeService(repository);
    }

    // проверка средней зп когда сотрудники есть
    @Test
    void shouldCalculateAverageSalary() {
        repository.save(new Employee(
                LocalDate.of(2020, 1, 1),
                100_000,
                "Разработчик",
                "Иванов Иван",
                null
        ));
        repository.save(new Employee(
                LocalDate.of(2020, 2, 1),
                200_000,
                "Старший разработчик",
                "Петров Петр",
                null
        ));
        repository.save(new Employee(
                LocalDate.of(2020, 3, 1),
                300_000,
                "Тимлид",
                "Сидоров Сидор",
                null
        ));

        double averageSalary = service.calculateAverageSalary();

        assertEquals(200_000, averageSalary, 0.00001);
    }

    // проверка топ спендера когда сотрудники есть
    @Test
    void shouldFindTopSpender() {
        repository.save(new Employee(
                LocalDate.of(2020, 1, 1),
                100_000,
                "Разработчик",
                "Иванов Иван",
                null
        ));
        repository.save(new Employee(
                LocalDate.of(2020, 2, 1),
                200_000,
                "Старший разработчик",
                "Петров Петр",
                null
        ));
        repository.save(new Employee(
                LocalDate.of(2020, 3, 1),
                300_000,
                "Тимлид",
                "Сидоров Сидор",
                null
        ));

        Optional<Employee> result = service.findTopSpender();

        assertTrue(result.isPresent());
        assertEquals("Сидоров Сидор", result.get().getName());
        assertEquals(300_000, result.get().getSalary().doubleValue());
    }

    // проверка средней зп когда сотрудников нет
    @Test
    void shouldReturnEmptyOptionalWhenNoEmployees() {
        Optional<Employee> result = service.findTopSpender();
        assertTrue(result.isEmpty());
    }

    // проверка топ спендера когда сотрудников нет
    @Test
    void shouldReturnZeroAverageWhenNoEmployees() {
        double average = service.calculateAverageSalary();
        assertEquals(0.0, average, 0.001);
    }

    // проверка фильтрации
    @Test
    void shouldFilterEmployeesByPosition() {
        repository.save(new Employee(LocalDate.now(), 300_000, "Директор", "Иванов", null));
        repository.save(new Employee(LocalDate.now(), 100_000, "Разработчик", "Петров", null));
        repository.save(new Employee(LocalDate.now(), 350_000, "Директор", "Сидоров", null));
        repository.save(new Employee(LocalDate.now(), 120_000, "Тимлид", "Козлов", null));

        var developers = service.filterByPosition("Директор");

        assertEquals(2, developers.size());
        assertEquals("Иванов", developers.get(0).getName());
        assertEquals("Сидоров", developers.get(1).getName());
    }
}