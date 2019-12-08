package com.javaseniorevandrosouza.employee.data.wrapper;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EmployeeRepositoryCacheWrapper {

    @Getter(AccessLevel.PACKAGE)
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRepositoryCacheWrapper(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value = "employeeList", key = "#department.id")
    public List<Employee> findByDepartment(Department department) {
        return employeeRepository.findByDepartment(department);
    }

    @CachePut(value = "employeeList", key = "#employee.department.id")
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @CacheEvict(value = "employeeList", key = "#employee.department.id")
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }
}