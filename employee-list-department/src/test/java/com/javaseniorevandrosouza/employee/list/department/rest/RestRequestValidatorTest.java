package com.javaseniorevandrosouza.employee.list.department.rest;

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

    private EmployeeListDepartmentRestRequest request;

    private Boolean isSupported;

    private Errors errors;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        restRequestValidator = new RestRequestValidator();
    }

    @Test
    public void supports_shall_support_expected_class() {
        testSpec.whenSupports(EmployeeListDepartmentRestRequest.class)
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
            request = EmployeeListDepartmentRestRequest.builder()
                    .departmentId(1)
                    .build();

            return this;
        }

        TestSpec givenInValidRestRequest() {
            request = EmployeeListDepartmentRestRequest.builder()
                    .departmentId(-1)
                    .build();

            return this;
        }

        TestSpec givenEmptyRestRequest() {
            request = EmployeeListDepartmentRestRequest.builder().build();
            return this;
        }

        TestSpec givenValidErrors() {
            errors = new BeanPropertyBindingResult(request, "employeeListDepartmentRestRequest");
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

            assertThat(1).isEqualTo(errors.getErrorCount());

            assertThat("Field error in object 'employeeListDepartmentRestRequest' on field 'departmentId': rejected value [-1]; codes [1014.employeeListDepartmentRestRequest.departmentId,1014.departmentId,1014.java.lang.Integer,1014]; arguments []; default message [O valor do campo id de departamento é inválido. Utilize valores maiores que zero.]").isEqualTo(errors.getAllErrors().get(0).toString());

            return this;
        }

        TestSpec thenErrorsContainsExpectedErrorsForEmptyInput() {
            assertThat(errors.hasErrors()).isTrue();

            assertThat(1).isEqualTo(errors.getErrorCount());

            assertThat("Field error in object 'employeeListDepartmentRestRequest' on field 'departmentId': rejected value [null]; codes [1004.employeeListDepartmentRestRequest.departmentId,1004.departmentId,1004.java.lang.Integer,1004]; arguments []; default message [O preenchimento do campo departamento é obrigatório. Utilize valores maiores que zero.]").isEqualTo(errors.getAllErrors().get(0).toString());

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