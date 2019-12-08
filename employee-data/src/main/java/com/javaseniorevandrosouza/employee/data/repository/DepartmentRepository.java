package com.javaseniorevandrosouza.employee.data.repository;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {
}