package org.example.hw.functional;

import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transformers {
    private Employee findEmployeeBornAtThisYear(Collection<Employee> employees, int year) {
        return employees.stream().filter(employee -> employee.getBirthDate().getYear() == year).findFirst()
                .orElse(null);
    }

    private Collection<String> findAllJobTitles(Collection<Job> jobs, Collection<Job> jobs2) {
        return Stream.of(jobs, jobs2).flatMap(Collection::stream).map(Job::getJobTitle).collect(Collectors.toList());
    }

    private Collection<String> findAllNameWithoutDuplicatesFromBothList(Collection<Employee> employees,
            Collection<Employee> employees2) {
        return Stream.of(employees, employees2).flatMap(Collection::stream).map(Employee::getName)
                .collect(Collectors.toSet());
    }

    private Integer findAverageAgeOfEmployeesThatWorkInDepartment(Collection<Employee> employees,
            Department departments) {
        return (int) Math
                .round(employees.stream().filter(employee -> employee.getDepartmentId() == departments.departmentId)
                        .mapToInt(employee -> Period.between(employee.getBirthDate(), LocalDate.now()).getYears())
                        .average().orElse(0));
    }

    private Employee findEmployeeWithNameOrUseDefaultOne(Collection<Employee> employees, String name,
            Employee defaultUser) {
        return employees.stream().filter(employee -> employee.getName().equals(name)).findFirst().orElse(defaultUser);
    }

    private Job findJobWithMinSalaryMoreThanOrThrowException(Collection<Job> jobs, Integer minSalary) {
        return jobs.stream().filter(job -> job.getMinSalary() > minSalary)
                .min(Comparator.comparingDouble(job -> job.minSalary)).orElseThrow();
    }

    // try to find 2 options.
    // option 1
    private Collection<String> findAllLocationNamesButWithoutDuplicates(Collection<Location> locations) {
        return locations.stream().map(location -> location.getLocation()).collect(Collectors.toSet());
    }

    // option 2
    private Collection<String> findAllLocationNamesButWithoutDuplicates2(Collection<Location> locations) {
        return locations.stream().map(location -> location.getLocation()).distinct().collect(Collectors.toList());
    }
}
