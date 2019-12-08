package com.javaseniorevandrosouza.employee.remove.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
public class RemoveServiceBusiness {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public RemoveServiceBusiness(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void removeEmployee(String uuid) throws ServiceException {
        Employee employee;

        Optional<Employee> employeeOpt = employeeRepository.findByUuid(uuid);

        try {
            employee = employeeOpt.orElseThrow(() -> ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.EMPLOYEE_NOT_FOUND)))
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build());
        } catch (ServiceException e) {
            log.warn("[Remove][Business][Exception]: " + e.toString());
            throw e;
        }


        employeeRepository.delete(employee);
    }
}