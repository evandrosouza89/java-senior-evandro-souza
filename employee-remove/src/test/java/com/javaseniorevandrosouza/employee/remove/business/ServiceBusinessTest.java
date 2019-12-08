package com.javaseniorevandrosouza.employee.remove.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class ServiceBusinessTest {

    @InjectMocks
    private RemoveServiceBusiness business;

    @Mock
    private EmployeeRepository employeeRepository;

    private String request;

    private TestSpec testSpec;

    private ServiceException serviceException;

    private Employee employee;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_removeEmployee_shall_remove_employee_from_database() {
        testSpec.givenAValidRestRequest()
                .givenEmployeeRepositoryFindsByUuid()
                .whenRemoveEmployee()
                .thenEmployeeRepositoryFindByUuidIsUsed()
                .thenEmployeeRepositoryDeleteIsUsed()
                .thenNoExceptionIsThrown();
    }

    @Test
    public void test_insertEmployee_shall_persist_and_return_valid_response() {
        testSpec.givenAValidRestRequest()
                .givenEmployeeRepositoryDoesntFindByUuid()
                .whenRemoveEmployee()
                .thenEmployeeRepositoryFindByUuidIsUsed()
                .thenEmployeeNotFoundExceptionIsThrown();
    }

    private class TestSpec {

        TestSpec givenAValidRestRequest() {
            request = "6e6c0d14-fc07-4447-b7f9-1234cbb2fe5d";
            return this;
        }

        TestSpec givenEmployeeRepositoryFindsByUuid() {
            employee = Employee.builder().id(1).build();

            when(employeeRepository.findByUuid(any())).thenReturn(Optional.of(employee));

            return this;
        }

        TestSpec givenEmployeeRepositoryDoesntFindByUuid() {
            when(employeeRepository.findByUuid(request)).thenReturn(Optional.empty());

            return this;
        }

        TestSpec whenRemoveEmployee() {
            try {
                business.removeEmployee(request);
            } catch (ServiceException e) {
                serviceException = e;
            }
            return this;
        }

        TestSpec thenEmployeeRepositoryFindByUuidIsUsed() {
            verify(employeeRepository, times(1)).findByUuid(any());
            return this;
        }

        TestSpec thenEmployeeRepositoryDeleteIsUsed() {
            verify(employeeRepository, times(1)).delete(any());
            return this;
        }


        TestSpec thenNoExceptionIsThrown() {
            assertThat(serviceException).isNull();
            return this;
        }

        TestSpec thenEmployeeNotFoundExceptionIsThrown() {
            assertThat(serviceException).isNotNull();
            assertThat(404).isEqualTo(serviceException.getHttpStatus().value());
            assertThat(Utils.buildErrorFromEnumServiceException(
                    EnumServiceExceptions.EMPLOYEE_NOT_FOUND)).isEqualTo(serviceException.getExceptionList().get(0));
            return this;
        }
    }

}