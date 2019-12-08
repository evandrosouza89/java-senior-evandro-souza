package com.javaseniorevandrosouza.employee.list.department.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EmployeeRestResponse {

    private String uuid;

    @JsonProperty("nome")
    private String name;

    private String email;
}