package com.javaseniorevandrosouza.employee.search.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import com.javaseniorevandrosouza.employee.search.rest.SearchRestRequest;
import com.javaseniorevandrosouza.employee.search.rest.SearchRestResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class ServiceBusinessTest {

    @InjectMocks
    private SearchServiceBusiness business;

    @Mock
    private EmployeeRepository employeeRepository;

    private SearchRestRequest request;

    private SearchRestResponse response;

    private TestSpec testSpec;

    private ServiceException serviceException;

    private Employee foundEmployee;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_searchEmployee_shall_return_valid_response() {
        testSpec.givenAValidRestRequest()
                .givenEmployeeRepositoryFindsAEmployee()
                .whenSearchEmployee()
                .thenEmployeeRepositoryFindByUuidIsUsed()
                .thenAValidResponseIsReturned();
    }

    @Test
    public void test_searchEmployee_shall_throw_exception_if_employee_not_found() {
        testSpec.givenAValidRestRequest()
                .givenEmployeeRepositoryDoesntFindAEmployee()
                .whenSearchEmployee()
                .thenEmployeeRepositoryFindByUuidIsUsed()
                .thenEmployeeNotFoundExceptionIsThrown();
    }

    private class TestSpec {

        TestSpec givenAValidRestRequest() {
            request = SearchRestRequest.builder()
                    .uuid("2544abcf-13e1-4238-ac0a-5fcd5f9a4710")
                    .build();

            return this;
        }

        TestSpec givenEmployeeRepositoryFindsAEmployee() {
            Department department = Department.builder()
                    .id(1)
                    .build();

            foundEmployee = Employee.builder()
                    .name("name")
                    .age(15)
                    .cpf("0123456789")
                    .phone("0123456789")
                    .uuid(UUID.randomUUID().toString())
                    .email("user@email.com")
                    .department(department)
                    .build();

            when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(foundEmployee));

            return this;
        }

        TestSpec givenEmployeeRepositoryDoesntFindAEmployee() {
            when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.empty());
            return this;
        }

        TestSpec whenSearchEmployee() {
            try {
                response = business.searchEmployee(request);
            } catch (ServiceException e) {
                serviceException = e;
            }
            return this;
        }

        TestSpec thenEmployeeRepositoryFindByUuidIsUsed() {
            verify(employeeRepository, times(1)).findByUuid(any());
            return this;
        }

        TestSpec thenAValidResponseIsReturned() {
            assertThat(response).isNotNull();

            assertThat(foundEmployee.getCpf()).isEqualTo(response.getCpf());
            assertThat(foundEmployee.getEmail()).isEqualTo(response.getEmail());
            assertThat(foundEmployee.getName()).isEqualTo(response.getName());
            assertThat(foundEmployee.getPhone()).isEqualTo(response.getPhone());
            assertThat(foundEmployee.getDepartment().getId()).isEqualTo(response.getDepartmentId());
            assertThat(foundEmployee.getAge()).isEqualTo(response.getAge());
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