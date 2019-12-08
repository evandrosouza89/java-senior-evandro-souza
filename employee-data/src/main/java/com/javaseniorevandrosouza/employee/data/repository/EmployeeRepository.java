package com.javaseniorevandrosouza.employee.data.repository;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    Optional<Employee> findByUuid(String uuid);

    List<Employee> findByDepartment(Department department);

    Integer countByDepartment(Department department);

    Integer countByDepartmentAndAgeLessThan(Department department, int age);

    Integer countByDepartmentAndAgeGreaterThan(Department department, int age);

}