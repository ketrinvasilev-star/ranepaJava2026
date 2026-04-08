package ru.ranepa.controller;

import ru.ranepa.dto.EmployeeRequestDto;
import ru.ranepa.dto.EmployeeResponseDto;
import ru.ranepa.dto.EmployeeStatsDto;
import ru.ranepa.model.Employee;
import ru.ranepa.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // GET /api/employees — все сотрудники
    @GetMapping
    public List<EmployeeResponseDto> getAllEmployees() {
        return service.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // GET /api/employees/{id} — сотрудник по ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = service.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(convertToResponseDto(employee.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/employees — создать сотрудника
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeRequestDto requestDto) {
        Employee employee = new Employee(
                requestDto.getHireDate(),
                requestDto.getSalary(),
                requestDto.getPosition(),
                requestDto.getName()
        );
        Employee savedEmployee = service.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDto(savedEmployee));
    }

    // DELETE /api/employees/{id} — удалить
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = service.findById(id);
        if (employee.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/employees/position/{position} — по должности
    @GetMapping("/position/{position}")
    public List<EmployeeResponseDto> getEmployeesByPosition(@PathVariable String position) {
        return service.findByPosition(position).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // GET /api/employees/stats — статистика (средняя ЗП, топ)
    @GetMapping("/stats")
    public EmployeeStatsDto getStats() {
        double averageSalary = service.calculateAverageSalary();
        
        Optional<Employee> topSpender = service.findTopSpender();
        String topEmployeeName = topSpender.map(Employee::getName).orElse("Нет сотрудников");
        double topEmployeeSalary = topSpender.map(e -> e.getSalary().doubleValue()).orElse(0.0);
        
        return new EmployeeStatsDto(averageSalary, topEmployeeName, topEmployeeSalary);
    }

    private EmployeeResponseDto convertToResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getSalary(),
                employee.getHireDate()
        );
    }
}