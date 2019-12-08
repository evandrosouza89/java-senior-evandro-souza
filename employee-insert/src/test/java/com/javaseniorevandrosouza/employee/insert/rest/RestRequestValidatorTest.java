package com.javaseniorevandrosouza.employee.insert.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class RestRequestValidatorTest {

    private TestSpec testSpec;

    private RestRequestValidator restRequestValidator;

    private InsertRestRequest request;

    private Boolean isSupported;

    private Errors errors;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        restRequestValidator = new RestRequestValidator();
    }

    @Test
    public void supports_shall_support_expected_class() {
        testSpec.whenSupports(InsertRestRequest.class)
                .thenReturnsTrue();
    }

    @Test
    public void supports_shall_not_support_unexpected_class() {
        testSpec.whenSupports(String.class)
                .thenReturnsFalse();
    }

    @Test
    public void validate_shall_not_populate_errors_on_valid_rest_request() {
        testSpec.givenValidRestRequest()
                .givenValidErrors()
                .whenValidate()
                .thenErrorsDoesntContainErrors();
    }

    @Test
    public void validate_shall_populate_errors_on_invalid_rest_request() {
        testSpec.givenInValidRestRequest()
                .givenValidErrors()
                .whenValidate()
                .thenErrorsContainsExpectedErrorsForInvalid();
    }

    @Test
    public void validate_shall_populate_errors_on_empty_rest_request() {
        testSpec.givenEmptyRestRequest()
                .givenValidErrors()
                .whenValidate()
                .thenErrorsContainsExpectedErrorsForEmptyInput();
    }

    private class TestSpec {


        TestSpec givenValidRestRequest() {
            request = InsertRestRequest.builder()
                    .age(50)
                    .cpf("02236547896")
                    .departmentId(1)
                    .email("user@email.net")
                    .name("name")
                    .phone("1234568789")
                    .build();

            return this;
        }

        TestSpec givenInValidRestRequest() {
            request = InsertRestRequest.builder()
                    .age(-1)
                    .cpf("cpf")
                    .departmentId(-1)
                    .email("email")
                    .name(null)
                    .phone("phone")
                    .build();

            return this;
        }

        TestSpec givenEmptyRestRequest() {
            request = InsertRestRequest.builder().build();
            return this;
        }

        TestSpec givenValidErrors() {
            errors = new BeanPropertyBindingResult(request, "insertRestRequest");
            return this;
        }

        TestSpec whenSupports(Class c) {
            isSupported = restRequestValidator.supports(c);
            return this;
        }

        TestSpec whenValidate() {
            restRequestValidator.validate(request, errors);
            return this;
        }

        TestSpec thenErrorsContainsExpectedErrorsForInvalid() {
            assertThat(errors.hasErrors()).isTrue();

            assertThat(6).isEqualTo(errors.getErrorCount());

            assertThat("Field error in object 'insertRestRequest' on field 'name': rejected value [null]; codes [1000.insertRestRequest.name,1000.name,1000.java.lang.String,1000]; arguments []; default message [O preenchimento do campo nome é obrigatório.]").isEqualTo(errors.getAllErrors().get(0).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'email': rejected value [email]; codes [1011.insertRestRequest.email,1011.email,1011.java.lang.String,1011]; arguments []; default message [O valor do campo email é inválido. Utilize o formato: user@provider.domain.]").isEqualTo(errors.getAllErrors().get(1).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'cpf': rejected value [cpf]; codes [1012.insertRestRequest.cpf,1012.cpf,1012.java.lang.String,1012]; arguments []; default message [O valor do campo CPF é inválido. Utilize 11 dígitos podendo ou não conter pontos e traço.]").isEqualTo(errors.getAllErrors().get(2).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'phone': rejected value [phone]; codes [1013.insertRestRequest.phone,1013.phone,1013.java.lang.String,1013]; arguments []; default message [O valor do campo telefone é inválido.]").isEqualTo(errors.getAllErrors().get(3).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'departmentId': rejected value [-1]; codes [1014.insertRestRequest.departmentId,1014.departmentId,1014.java.lang.Integer,1014]; arguments []; default message [O valor do campo id de departamento é inválido. Utilize valores maiores que zero.]").isEqualTo(errors.getAllErrors().get(4).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'age': rejected value [-1]; codes [1015.insertRestRequest.age,1015.age,1015.java.lang.Integer,1015]; arguments []; default message [O valor do campo idade é inválido. Utilize valores maiores ou iguais a zero.]").isEqualTo(errors.getAllErrors().get(5).toString());

            return this;
        }

        TestSpec thenErrorsContainsExpectedErrorsForEmptyInput() {
            assertThat(errors.hasErrors()).isTrue();

            assertThat(6).isEqualTo(errors.getErrorCount());

            assertThat("Field error in object 'insertRestRequest' on field 'name': rejected value [null]; codes [1000.insertRestRequest.name,1000.name,1000.java.lang.String,1000]; arguments []; default message [O preenchimento do campo nome é obrigatório.]").isEqualTo(errors.getAllErrors().get(0).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'email': rejected value [null]; codes [1001.insertRestRequest.email,1001.email,1001.java.lang.String,1001]; arguments []; default message [O preenchimento do campo email é obrigatório. Utilize o formato: user@provider.domain.]").isEqualTo(errors.getAllErrors().get(1).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'cpf': rejected value [null]; codes [1002.insertRestRequest.cpf,1002.cpf,1002.java.lang.String,1002]; arguments []; default message [O preenchimento do campo cpf é é obrigatório. Utilize 11 dígitos podendo ou não conter pontos e traço.]").isEqualTo(errors.getAllErrors().get(2).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'phone': rejected value [null]; codes [1003.insertRestRequest.phone,1003.phone,1003.java.lang.String,1003]; arguments []; default message [O preenchimento do campo telefone é obrigatório.]").isEqualTo(errors.getAllErrors().get(3).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'departmentId': rejected value [null]; codes [1004.insertRestRequest.departmentId,1004.departmentId,1004.java.lang.Integer,1004]; arguments []; default message [O preenchimento do campo departamento é obrigatório. Utilize valores maiores que zero.]").isEqualTo(errors.getAllErrors().get(4).toString());
            assertThat("Field error in object 'insertRestRequest' on field 'age': rejected value [null]; codes [1005.insertRestRequest.age,1005.age,1005.java.lang.Integer,1005]; arguments []; default message [O preenchimento do campo idade é obrigatório. Utilize valores maiores ou iguais a zero.]").isEqualTo(errors.getAllErrors().get(5).toString());

            return this;
        }


        TestSpec thenReturnsTrue() {
            assertThat(isSupported).isTrue();
            return this;
        }

        TestSpec thenReturnsFalse() {
            assertThat(isSupported).isFalse();
            return this;
        }

        TestSpec thenErrorsDoesntContainErrors() {
            assertThat(errors.hasErrors()).isFalse();
            return this;
        }
    }

}