package org.example.hw.functional;

import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FunctionalHW {

    public Optional<Employee> findEmployeeById(List<Employee> employees, int id) {
        return employees.stream().filter(employee -> employee.id == id).findFirst();
    }

    public List<Employee> sortEmployeesByName(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparing(Employee::getName)).collect(Collectors.toList());
    }

    public long findNumberJobsWithMaxSalaryMoreThan(List<Job> jobs, double maxSalary) {
        return jobs.stream().filter(job -> job.maxSalary > maxSalary).count();
    }

    public Set<String> getEmployeeNamesFromDepartment(List<Employee> employees, Department department) {
        return employees.stream().filter(employee -> employee.getDepartmentId() == department.getDepartmentId())
                .map(Employee::getName).collect(Collectors.toSet());
    }

    // employeeId, Employee
    public Map<Integer, Employee> buildEmployeeMap(List<Employee> employees) {
        return employees.stream().collect(Collectors.toMap(Employee::getId, employee -> employee));
    }

    // find employee with the closest birthdate
    public Employee findByClosestBirthDate(List<Employee> employees, LocalDate birthDate) {
        return employees.stream().reduce((acc, curr) -> {
            long dAcc = Math.abs(ChronoUnit.DAYS.between(acc.getBirthDate(), birthDate));
            long dCurr = Math.abs(ChronoUnit.DAYS.between(curr.getBirthDate(), birthDate));
            return dAcc <= dCurr ? acc : curr;
        }).orElse(null);
    }

    // make map where key is department name and long is total amount of employees
    // working in that department
    public Map<String, Long> totalSalaryReport(List<Employee> employees, List<Department> departments) {
        Map<Integer, String> deptNames = departments.stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Department::getDepartmentName));

        return employees.stream()
                .filter(employee -> deptNames.containsKey(employee.getDepartmentId()))
                .collect(Collectors.groupingBy(
                        employee -> deptNames.get(employee.getDepartmentId()),
                        Collectors.counting()));
    }

    public Optional<Employee> findRichestEmployee(List<Employee> employees) {
        return employees.stream().max(Comparator.comparing(Employee::getSalary));
    }

}
