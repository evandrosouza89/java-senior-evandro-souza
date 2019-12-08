package com.javaseniorevandrosouza.employee.search.business;

import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.search.rest.SearchRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface BusinessMapper {

    @Mapping(source = "department.id", target = "departmentId")
    SearchRestResponse employeeToSearchRestResponse(Employee employee);
}