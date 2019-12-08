package com.javaseniorevandrosouza.employee.common.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class UtilsTest {

    private TestSpec testSpec;

    private BindingResult bindingResult;

    private EnumServiceExceptions enumServiceException;

    private String error;

    private List<String> errorList;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_buildErrorListFromBindingResult_shallReturnAFormattedErrorList() {
        testSpec.givenAValidBindingResult()
                .givenBindingResultReturnsValidFieldErrors()
                .whenBuildErrorListFromBindingResult()
                .thenShallReturnAFormattedErrorList();
    }

    @Test
    public void test_buildErrorFromEnumServiceExceptions_shallReturnAFormattedError() {
        testSpec.givenAValidEnumServiceException()
                .whenBuildErrorFromEnumServiceExceptions()
                .thenShallReturnAFormattedError();
    }

    private class TestSpec {

        TestSpec givenAValidBindingResult() {
            bindingResult = Mockito.mock(BindingResult.class);
            return this;
        }

        TestSpec givenAValidEnumServiceException() {
            enumServiceException = EnumServiceExceptions.DEPARTMENT_NOT_FOUND;
            return this;
        }

        TestSpec givenBindingResultReturnsValidFieldErrors() {

            FieldError fieldError1 = new FieldError("", "", null, true, new String[]{"test1"}, null, "test1");
            FieldError fieldError2 = new FieldError("", "", null, true, new String[]{"test2"}, null, "test2");

            when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
            return this;
        }

        TestSpec whenBuildErrorListFromBindingResult() {
            errorList = Utils.buildErrorListFromBindingResult(bindingResult);
            return this;
        }

        TestSpec whenBuildErrorFromEnumServiceExceptions() {
            error = Utils.buildErrorFromEnumServiceException(enumServiceException);
            return this;
        }

        TestSpec thenShallReturnAFormattedErrorList() {
            assertThat(errorList).isNotNull();
            assertThat("Código: test1 - Descrição: test1").isEqualTo(errorList.get(0));
            assertThat("Código: test2 - Descrição: test2").isEqualTo(errorList.get(1));
            return this;
        }

        TestSpec thenShallReturnAFormattedError() {
            assertThat(error).isNotNull();
            assertThat("Código: 2000 - Descrição: Departamento não encontrado.").isEqualTo(error);
            return this;
        }
    }
}