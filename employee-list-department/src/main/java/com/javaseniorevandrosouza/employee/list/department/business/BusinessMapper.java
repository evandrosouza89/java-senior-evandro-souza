package com.javaseniorevandrosouza.employee.list.department.business;

import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.list.department.rest.EmployeeRestResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
interface BusinessMapper {

    List<EmployeeRestResponse> employeeListToEmployeeListRestResponse(List<Employee> employeeList);
}