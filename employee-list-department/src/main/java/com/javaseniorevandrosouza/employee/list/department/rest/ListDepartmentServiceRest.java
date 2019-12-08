package com.javaseniorevandrosouza.employee.list.department.rest;

import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.list.department.business.ListDepartmentServiceBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class ListDepartmentServiceRest {

    private final ListDepartmentServiceBusiness business;

    @Autowired
    public ListDepartmentServiceRest(ListDepartmentServiceBusiness business) {
        this.business = business;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new RestRequestValidator());
    }

    @GetMapping(value = "/departamentos/{departmentId}/colaboradores", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeListDepartmentResponse list(@Valid EmployeeListDepartmentRestRequest restRequest, BindingResult bindingResult) throws ServiceException {

        if (bindingResult.hasErrors()) {
            throw ServiceException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .exceptionList(Utils.buildErrorListFromBindingResult(bindingResult))
                    .build();
        }

        log.info("[EmployeeListDepartment][Rest][Input] GET /departamentos/{departmentId}/colaboradores: " + restRequest.toString());

        EmployeeListDepartmentResponse restResponse = business.list(restRequest);

        log.info("[EmployeeListDepartment][Rest][Output] GET /departamentos/{departmentId}/colaboradores: " + restResponse.toString());

        return restResponse;
    }

}