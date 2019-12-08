package com.javaseniorevandrosouza.employee.search.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SearchRestResponse {

    private String cpf;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("telefone")
    private String phone;

    private String email;

    @JsonProperty("departamento")
    private Integer departmentId;

    @JsonProperty("idade")
    private Integer age;

}
