package com.javaseniorevandrosouza.employee.data.wrapper;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class DepartmentRepositoryCacheWrapper {

    @Getter(AccessLevel.PACKAGE)
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentRepositoryCacheWrapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Cacheable(value = "department", key = "#id")
    public Optional<Department> findById(Integer id) {
        return departmentRepository.findById(id);
    }
}