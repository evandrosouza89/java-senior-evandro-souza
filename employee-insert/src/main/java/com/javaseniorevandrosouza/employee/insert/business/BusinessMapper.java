package com.javaseniorevandrosouza.employee.insert.business;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.insert.rest.InsertRestRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface BusinessMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "restRequest.name")
    @Mapping(target = "department", source = "department")
    Employee insertRestRequestAndUUIDAndDepartmentToEmployee(InsertRestRequest restRequest, String uuid, Department department);

}