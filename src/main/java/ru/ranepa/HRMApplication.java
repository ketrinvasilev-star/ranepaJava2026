package ru.ranepa;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.repository.EmployeeRepositoryImpl;
import ru.ranepa.service.EmployeeService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class HRMApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static EmployeeRepository repository = new EmployeeRepositoryImpl();
    private static EmployeeService service = new EmployeeService(repository);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== HRM System Menu ===");
            System.out.println("1. Список всех сотрудников");
            System.out.println("2. Добавить сотрудника");
            System.out.println("3. Удалить сотрудника по ID");
            System.out.println("4. Найти сотрудника по ID");
            System.out.println("5. Статистика");
            System.out.println("6. Выход");
            System.out.print("Выберите пункт: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showAllEmployees();
                    break;
                case 2:
                    addEmployee();
                    break;
                case 3:
                    deleteEmployee();
                    break;
                case 4:
                    findEmployee();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 6:
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Выберите пункт от 1 до 6");
            }
        }
    }

    // вывести всех сотрудников
    private static void showAllEmployees() {
        System.out.println("\n--- Список сотрудников ---");
        Iterable<Employee> employees = repository.findAll();
        boolean found = false;

        for (Employee emp : employees) {
            System.out.println(emp);
            found = true;
        }

        if (!found) {
            System.out.println("Сотрудников нет");
        }
    }

    // добавление сотрудника
    private static void addEmployee() {
        System.out.println("\n--- Добавление сотрудника ---");

        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Ошибка: имя не может быть пустым");
            return;
        }

        System.out.print("Введите должность: ");
        String position = scanner.nextLine();
        if (position.trim().isEmpty()) {
            System.out.println("Ошибка: должность не может быть пустой");
            return;
        }

        System.out.print("Введите зарплату: ");
        double salary = 0;
        try {
            salary = Double.parseDouble(scanner.nextLine().replace(',', '.'));
            if (salary <= 0) {
                System.out.println("Ошибка: зарплата должна быть положительным числом");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число для зарплаты");
            return;
        }

        System.out.print("Введите дату приема в формате ГГГГ-ММ-ДД: ");
        LocalDate hireDate = null;
        try {
            String dateStr = scanner.nextLine();
            hireDate = LocalDate.parse(dateStr);

            if (hireDate.isAfter(LocalDate.now())) {
                System.out.println("Ошибка: дата приема не может быть в будущем");
                return;
            }
        } catch (Exception e) {
            System.out.println("Ошибка: неверный формат даты. Используйте ГГГГ-ММ-ДД");
            return;
        }

        Employee emp = new Employee(hireDate, salary, position, name, null);
        String result = repository.save(emp);
        System.out.println(result);
    }

    // удаление сотрудника
    private static void deleteEmployee() {
        System.out.println("\n--- Удаление сотрудника ---");
        System.out.print("Введите ID: ");
        long id = 0;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректный ID (целое число)");
            return;
        }

        String result = repository.delete(id);
        System.out.println(result);
    }

    // найти сотрудника
    private static void findEmployee() {
        System.out.println("\n--- Поиск сотрудника ---");
        System.out.print("Введите ID: ");
        long id = 0;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректный ID (целое число)");
            return;
        }

        Optional<Employee> emp = repository.findById(id);
        if (emp.isPresent()) {
            System.out.println("Найден: " + emp.get());
        } else {
            System.out.println("Сотрудник с ID " + id + " не найден");
        }
    }

    // выводим статистику по БД
    private static void showStatistics() {
        System.out.println("\n--- Статистика ---");

        double avgSalary = service.calculateAverageSalary();
        System.out.printf("Средняя зарплата: %.2f\n", avgSalary);

        Optional<Employee> top = service.findTopSpender();
        if (top.isPresent()) {
            System.out.println("Cамый высокооплачиваемый сотрудник: " + top.get().getName() +
                    " (" + top.get().getSalary() + ")");
        } else {
            System.out.println("Нет сотрудников");
        }
    }
}