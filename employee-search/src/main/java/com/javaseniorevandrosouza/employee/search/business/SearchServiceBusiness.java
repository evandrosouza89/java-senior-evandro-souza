package com.javaseniorevandrosouza.employee.search.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import com.javaseniorevandrosouza.employee.search.rest.SearchRestRequest;
import com.javaseniorevandrosouza.employee.search.rest.SearchRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
public class SearchServiceBusiness {

    private static final BusinessMapper MAPPER_INSTANCE = Mappers.getMapper(BusinessMapper.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public SearchServiceBusiness(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public SearchRestResponse searchEmployee(SearchRestRequest restRequest) throws ServiceException {
        SearchRestResponse restResponse;

        Employee queryResult;

        Optional<Employee> employeeOpt;

        employeeOpt = employeeRepository.findByUuid(restRequest.getUuid());

        try {
            queryResult = employeeOpt.orElseThrow(() -> ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.EMPLOYEE_NOT_FOUND)))
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build());
        } catch (ServiceException e) {
            log.warn("[Search][Business][Exception]: " + e.toString());
            throw e;
        }

        restResponse = MAPPER_INSTANCE.employeeToSearchRestResponse(queryResult);

        return restResponse;
    }
}