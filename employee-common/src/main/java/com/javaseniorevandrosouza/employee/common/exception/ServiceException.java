package com.javaseniorevandrosouza.employee.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ServiceException extends Exception {

    private final List<String> exceptionList;

    private final HttpStatus httpStatus;

}