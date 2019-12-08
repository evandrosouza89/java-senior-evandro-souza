package com.javaseniorevandrosouza.employee.list.department.rest;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

class RestRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeListDepartmentRestRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        validateMissingOrEmpty((EmployeeListDepartmentRestRequest) obj, errors);
    }

    private void validateMissingOrEmpty(EmployeeListDepartmentRestRequest restRequest, Errors errors) {
        Integer departmentId = restRequest.getDepartmentId();

        if (departmentId != null) {
            validateDepartmentId(departmentId, errors);
        } else {
            errors.rejectValue("departmentId",
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_MISSING.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_MISSING.getDescription());
        }
    }

    private void validateDepartmentId(Integer departmentId, Errors errors) {
        if (departmentId < 0) {
            errors.rejectValue("departmentId",
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_INVALID.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_INVALID.getDescription());
        }
    }
}