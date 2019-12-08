package com.javaseniorevandrosouza.employee.insert.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.DepartmentRepository;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import com.javaseniorevandrosouza.employee.insert.rest.InsertRestRequest;
import com.javaseniorevandrosouza.employee.insert.rest.InsertRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class InsertServiceBusiness {

    private static final BusinessMapper MAPPER_INSTANCE = Mappers.getMapper(BusinessMapper.class);

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public InsertServiceBusiness(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public InsertRestResponse insertEmployee(InsertRestRequest restRequest) throws ServiceException {
        InsertRestResponse businessResponse;

        Department department;

        try {
            department = findDepartment(restRequest.getDepartmentId());
        } catch (ServiceException e) {
            log.warn("[Insert][Business][Exception]: " + e.toString());
            throw e;
        }

        int totalDepartmentEmployees = employeeRepository.countByDepartment(department);

        try {
            validateDepartmentAgeUnder18Threshold(restRequest.getAge(), department, totalDepartmentEmployees);
        } catch (ServiceException e) {
            log.warn("[Insert][Business][Exception]: " + e.toString());
            throw e;
        }

        try {
            validateDepartmentAgeOver65Threshold(restRequest.getAge(), department, totalDepartmentEmployees);
        } catch (ServiceException e) {
            log.warn("[Insert][Business][Exception]: " + e.toString());
            throw e;
        }

        restRequest.setCpf(restRequest.getCpf().replaceAll("\\D", ""));
        restRequest.setPhone(restRequest.getPhone().replaceAll("\\D", ""));

        Employee employee = MAPPER_INSTANCE.insertRestRequestAndUUIDAndDepartmentToEmployee(restRequest, UUID.randomUUID().toString(), department);

        employeeRepository.save(employee);

        businessResponse = InsertRestResponse.builder().uuid(employee.getUuid()).build();

        return businessResponse;
    }

    private Department findDepartment(Integer id) throws ServiceException {
        Optional<Department> department = departmentRepository.findById(id);
        return department.orElseThrow(() -> ServiceException.builder()
                .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                        EnumServiceExceptions.DEPARTMENT_NOT_FOUND)))
                .httpStatus(HttpStatus.NOT_FOUND)
                .build());
    }

    private void validateDepartmentAgeUnder18Threshold(int age, Department department, int totalDepartmentEmployees) throws ServiceException {
        if (age >= 18) {
            return;
        }

        if (totalDepartmentEmployees <= 0) {
            throw ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.EMPLOYEE_UNDER_18_THRESHOLD)))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        int numberOfUnder18 = employeeRepository.countByDepartmentAndAgeLessThan(department, 18);

        numberOfUnder18++;

        double result = (double) numberOfUnder18 / (double) totalDepartmentEmployees;

        if (result > 0.2D) {
            throw ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.EMPLOYEE_UNDER_18_THRESHOLD)))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    private void validateDepartmentAgeOver65Threshold(int age, Department department,
                                                      int totalDepartmentEmployees) throws ServiceException {
        if (age <= 65) {
            return;
        }

        if (totalDepartmentEmployees <= 0) {
            throw ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.EMPLOYEE_OVER_65_THRESHOLD)))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        int numberOfOver65 = employeeRepository.countByDepartmentAndAgeGreaterThan(department, 65);

        numberOfOver65++;

        double result = (double) numberOfOver65 / (double) totalDepartmentEmployees;

        if (result > 0.2D) {
            throw ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.EMPLOYEE_OVER_65_THRESHOLD)))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}