package com.javaseniorevandrosouza.employee.insert.rest;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RestRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return InsertRestRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        validateMissingOrEmpty((InsertRestRequest) obj, errors);
    }

    private void validateMissingOrEmpty(InsertRestRequest restRequest, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                EnumServiceExceptions.INVALID_REST_INPUT_NAME_MISSING.getId().toString(),
                EnumServiceExceptions.INVALID_REST_INPUT_NAME_MISSING.getDescription());

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
                EnumServiceExceptions.INVALID_REST_INPUT_EMAIL_MISSING.getId().toString(),
                EnumServiceExceptions.INVALID_REST_INPUT_EMAIL_MISSING.getDescription());

        if (errors.getFieldError("email") == null) {
            validateEmail(restRequest.getEmail(), errors);
        }

        if (restRequest.getCpf() == null || restRequest.getCpf().trim().isEmpty()) {
            errors.rejectValue("cpf",
                    EnumServiceExceptions.INVALID_REST_INPUT_CPF_MISSING.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_CPF_MISSING.getDescription());
        } else {
            validateCPF(restRequest.getCpf(), errors);
        }

        if (restRequest.getPhone() == null || restRequest.getPhone().trim().isEmpty()) {
            errors.rejectValue("phone",
                    EnumServiceExceptions.INVALID_REST_INPUT_PHONE_MISSING.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_PHONE_MISSING.getDescription());
        } else {
            validatePhone(restRequest.getPhone(), errors);
        }

        if (restRequest.getDepartmentId() == null) {
            errors.rejectValue("departmentId",
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_MISSING.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_MISSING.getDescription());
        } else {
            validateDepartmentId(restRequest.getDepartmentId(), errors);
        }

        if (restRequest.getAge() == null) {
            errors.rejectValue("age",
                    EnumServiceExceptions.INVALID_REST_INPUT_AGE_MISSING.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_AGE_MISSING.getDescription());
        } else {
            validateAge(restRequest.getAge(), errors);
        }
    }

    private void validatePhone(String phone, Errors errors) {
        if (phone.replaceAll("\\D", "").trim().length() <= 0) {
            errors.rejectValue("phone",
                    EnumServiceExceptions.INVALID_REST_INPUT_PHONE_INVALID.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_PHONE_INVALID.getDescription());
        }
    }

    private void validateCPF(String cpf, Errors errors) {
        if (cpf.replaceAll("\\D", "").trim().length() != 11) {
            errors.rejectValue("cpf",
                    EnumServiceExceptions.INVALID_REST_INPUT_CPF_INVALID.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_CPF_INVALID.getDescription());
        }
    }

    private void validateDepartmentId(Integer departmentId, Errors errors) {
        if (departmentId <= 0) {
            errors.rejectValue("departmentId",
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_INVALID.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_DEPARTMENT_ID_INVALID.getDescription());
        }
    }

    private void validateAge(Integer age, Errors errors) {
        if (age < 0) {
            errors.rejectValue("age",
                    EnumServiceExceptions.INVALID_REST_INPUT_AGE_INVALID.getId().toString(),
                    EnumServiceExceptions.INVALID_REST_INPUT_AGE_INVALID.getDescription());
        }
    }

    private void validateEmail(String email, Errors errors) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            errors.rejectValue("email", EnumServiceExceptions.INVALID_REST_INPUT_EMAIL_INVALID.getId().toString(), EnumServiceExceptions.INVALID_REST_INPUT_EMAIL_INVALID.getDescription());
        }
    }
}