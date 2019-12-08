package com.javaseniorevandrosouza.employee.list.department.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.wrapper.DepartmentRepositoryCacheWrapper;
import com.javaseniorevandrosouza.employee.data.wrapper.EmployeeRepositoryCacheWrapper;
import com.javaseniorevandrosouza.employee.list.department.rest.EmployeeListDepartmentResponse;
import com.javaseniorevandrosouza.employee.list.department.rest.EmployeeListDepartmentRestRequest;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ListDepartmentServiceBusiness {

    private static final BusinessMapper MAPPER_INSTANCE = Mappers.getMapper(BusinessMapper.class);

    private final EmployeeRepositoryCacheWrapper cachedEmployeeRepository;

    private final DepartmentRepositoryCacheWrapper cachedDepartmentRepository;

    @Autowired
    public ListDepartmentServiceBusiness(EmployeeRepositoryCacheWrapper employeeRepository, DepartmentRepositoryCacheWrapper departmentRepository) {
        this.cachedEmployeeRepository = employeeRepository;
        this.cachedDepartmentRepository = departmentRepository;
    }

    public EmployeeListDepartmentResponse list(EmployeeListDepartmentRestRequest restRequest) throws ServiceException {
        Department department;

        try {
            department = findDepartment(restRequest.getDepartmentId());
        } catch (ServiceException e) {
            log.warn("[EmployeeListDepartment][Business][Exception]: " + e.toString());
            throw e;
        }

        List<Employee> employeeList;

        employeeList = cachedEmployeeRepository.findByDepartment(department);

        return EmployeeListDepartmentResponse.builder()
                .employeeList(MAPPER_INSTANCE.employeeListToEmployeeListRestResponse(employeeList))
                .build();
    }

    private Department findDepartment(Integer id) throws ServiceException {
        Optional<Department> department = cachedDepartmentRepository.findById(id);
        return department.orElseThrow(() -> ServiceException.builder()
                .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                        EnumServiceExceptions.DEPARTMENT_NOT_FOUND)))
                .httpStatus(HttpStatus.NOT_FOUND)
                .build());
    }
}