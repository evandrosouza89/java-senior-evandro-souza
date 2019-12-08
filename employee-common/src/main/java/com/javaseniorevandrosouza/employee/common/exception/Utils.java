package com.javaseniorevandrosouza.employee.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static List<String> buildErrorListFromBindingResult(BindingResult bindingResult) {
        return bindingResult
                .getFieldErrors()
                .stream()
                .map(error -> "Código: " + error.getCode() + " - Descrição: " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }

    public static String buildErrorFromEnumServiceException(EnumServiceExceptions enumServiceException) {
        return "Código: " + enumServiceException.getId() + " - Descrição: " + enumServiceException.getDescription();
    }

}
