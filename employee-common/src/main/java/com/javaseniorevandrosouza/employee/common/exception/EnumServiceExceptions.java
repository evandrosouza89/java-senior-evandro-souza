package com.javaseniorevandrosouza.employee.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumServiceExceptions {

    //1xxx Input validations
    INVALID_REST_INPUT_NAME_MISSING(1000, "O preenchimento do campo nome é obrigatório."),
    INVALID_REST_INPUT_EMAIL_MISSING(1001, "O preenchimento do campo email é obrigatório. Utilize o formato: user@provider.domain."),
    INVALID_REST_INPUT_CPF_MISSING(1002, "O preenchimento do campo cpf é é obrigatório. Utilize 11 dígitos podendo ou não conter pontos e traço."),
    INVALID_REST_INPUT_PHONE_MISSING(1003, "O preenchimento do campo telefone é obrigatório."),
    INVALID_REST_INPUT_DEPARTMENT_ID_MISSING(1004, "O preenchimento do campo departamento é obrigatório. Utilize valores maiores que zero."),
    INVALID_REST_INPUT_AGE_MISSING(1005, "O preenchimento do campo idade é obrigatório. Utilize valores maiores ou iguais a zero."),
    INVALID_REST_INPUT_UUID_MISSING(1006, "O preenchimento do campo uuid é obrigatório. Utilize 36 caracteres incluindo traços."),

    INVALID_REST_INPUT_EMAIL_INVALID(1011, "O valor do campo email é inválido. Utilize o formato: user@provider.domain."),
    INVALID_REST_INPUT_CPF_INVALID(1012, "O valor do campo CPF é inválido. Utilize 11 dígitos podendo ou não conter pontos e traço."),
    INVALID_REST_INPUT_PHONE_INVALID(1013, "O valor do campo telefone é inválido."),
    INVALID_REST_INPUT_DEPARTMENT_ID_INVALID(1014, "O valor do campo id de departamento é inválido. Utilize valores maiores que zero."),
    INVALID_REST_INPUT_AGE_INVALID(1015, "O valor do campo idade é inválido. Utilize valores maiores ou iguais a zero."),
    INVALID_REST_INPUT_UUID_INVALID(1016, "O valor do campo uuid é inválido. Utilize 36 caracteres incluindo traços."),

    //2xxx Database errors
    DEPARTMENT_NOT_FOUND(2000, "Departamento não encontrado."),
    EMPLOYEE_NOT_FOUND(2001, "Colaborador não encontrado."),

    //3xxx Business errors
    EMPLOYEE_UNDER_18_THRESHOLD(3000, "Somente é permitido até 20% de colaboradores menores que 18 anos por setor."),
    EMPLOYEE_OVER_65_THRESHOLD(3001, "Somente é permitido até 20% de colaboradores maiores que 65 anos na empresa.");

    @NonNull
    private Integer id;

    @NonNull
    private String description;

}