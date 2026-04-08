package ru.ranepa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ranepa.dto.EmployeeRequestDto;
import ru.ranepa.model.Employee;
import ru.ranepa.service.EmployeeService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;
    private EmployeeRequestDto requestDto;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(LocalDate.of(2024, 1, 15), 150000.0, "Java Developer", "Иван Иванов");
        employee1.setId(1L);

        employee2 = new Employee(LocalDate.of(2024, 2, 10), 120000.0, "QA Engineer", "Петр Петров");
        employee2.setId(2L);

        requestDto = new EmployeeRequestDto("Сидр Сидоров", "Team Lead", 200000.0, LocalDate.of(2024, 3, 1));
    }

    @Test
    void shouldReturnAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeService.findAll()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Иван Иванов")))
                .andExpect(jsonPath("$[1].name", is("Петр Петров")));
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        when(employeeService.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee emp = invocation.getArgument(0);
            emp.setId(3L);
            return emp;
        });

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Сидр Сидоров")))
                .andExpect(jsonPath("$.position", is("Team Lead")));
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Иван Иванов")));
    }

    @Test
    void shouldReturn404WhenEmployeeNotFound() throws Exception {
        when(employeeService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employee1));
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnEmptyStatsWhenNoEmployees() throws Exception {
        when(employeeService.findAll()).thenReturn(List.of());
        when(employeeService.calculateAverageSalary()).thenReturn(0.0);
        when(employeeService.findTopSpender()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageSalary", is(0.0)))
                .andExpect(jsonPath("$.topEmployeeName", is("Нет сотрудников")));
    }

    @Test
    void shouldReturnCorrectStats() throws Exception {
        when(employeeService.findAll()).thenReturn(Arrays.asList(employee1, employee2));
        when(employeeService.calculateAverageSalary()).thenReturn(135000.0);
        when(employeeService.findTopSpender()).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/api/employees/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageSalary", is(135000.0)))
                .andExpect(jsonPath("$.topEmployeeName", is("Иван Иванов")));
    }
}