package com.javaseniorevandrosouza.employee.search.rest;

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

    private SearchRestRequest request;

    private Boolean isSupported;

    private Errors errors;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        restRequestValidator = new RestRequestValidator();
    }

    @Test
    public void supports_shall_support_expected_class() {
        testSpec.whenSupports(SearchRestRequest.class)
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
            request = SearchRestRequest.builder()
                    .uuid("876e9101-c3be-49d3-a2f9-121d244597a3")
                    .build();

            return this;
        }

        TestSpec givenInValidRestRequest() {
            request = SearchRestRequest.builder()
                    .uuid("876e9101-c3be")
                    .build();

            return this;
        }

        TestSpec givenEmptyRestRequest() {
            request = SearchRestRequest.builder().build();
            return this;
        }

        TestSpec givenValidErrors() {
            errors = new BeanPropertyBindingResult(request, "searchRestRequest");
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

            assertThat("Field error in object 'searchRestRequest' on field 'uuid': rejected value [876e9101-c3be]; codes [1016.searchRestRequest.uuid,1016.uuid,1016.java.lang.String,1016]; arguments []; default message [O valor do campo uuid é inválido. Utilize 36 caracteres incluindo traços.]").isEqualTo(errors.getAllErrors().get(0).toString());

            return this;
        }

        TestSpec thenErrorsContainsExpectedErrorsForEmptyInput() {
            assertThat(errors.hasErrors()).isTrue();

            assertThat(1).isEqualTo(errors.getErrorCount());

            assertThat("Field error in object 'searchRestRequest' on field 'uuid': rejected value [null]; codes [1006.searchRestRequest.uuid,1006.uuid,1006.java.lang.String,1006]; arguments []; default message [O preenchimento do campo uuid é obrigatório. Utilize 36 caracteres incluindo traços.]").isEqualTo(errors.getAllErrors().get(0).toString());

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