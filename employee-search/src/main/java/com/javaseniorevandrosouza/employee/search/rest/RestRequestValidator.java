package com.javaseniorevandrosouza.employee.search.rest;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RestRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchRestRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        validateMissingOrEmpty((SearchRestRequest) obj, errors);
    }

    private void validateMissingOrEmpty(SearchRestRequest restRequest, Errors errors) {
        String uuid = restRequest.getUuid();

        if("null".equals(uuid)) {
            uuid = null;
            restRequest.setUuid(null);
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uuid",
                EnumServiceExceptions.INVALID_REST_INPUT_UUID_MISSING.getId().toString(),
                EnumServiceExceptions.INVALID_REST_INPUT_UUID_MISSING.getDescription());

        if (uuid != null) {
            validateUuid(restRequest.getUuid(), errors);
        }
    }

    private void validateUuid(String uuid, Errors errors) {
        String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(uuid);
        if (!matcher.matches()) {
            errors.rejectValue("uuid", EnumServiceExceptions.INVALID_REST_INPUT_UUID_INVALID.getId().toString()
                    , EnumServiceExceptions.INVALID_REST_INPUT_UUID_INVALID.getDescription());
        }
    }
}