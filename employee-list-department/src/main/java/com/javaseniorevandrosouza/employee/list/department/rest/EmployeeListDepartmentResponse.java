package com.javaseniorevandrosouza.employee.list.department.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class EmployeeListDepartmentResponse {

    @JsonProperty("listaDeColaboradores")
    private List<EmployeeRestResponse> employeeList;

}