package com.javaseniorevandrosouza.employee.list.department.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.wrapper.DepartmentRepositoryCacheWrapper;
import com.javaseniorevandrosouza.employee.data.wrapper.EmployeeRepositoryCacheWrapper;
import com.javaseniorevandrosouza.employee.list.department.rest.EmployeeListDepartmentResponse;
import com.javaseniorevandrosouza.employee.list.department.rest.EmployeeListDepartmentRestRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class ServiceBusinessTest {

    @InjectMocks
    private ListDepartmentServiceBusiness business;

    @Mock
    private EmployeeRepositoryCacheWrapper cachedEmployeeRepository;

    @Mock
    private DepartmentRepositoryCacheWrapper cachedDepartmentRepository;

    private EmployeeListDepartmentRestRequest request;

    private EmployeeListDepartmentResponse response;

    private TestSpec testSpec;

    private ServiceException serviceException;

    private Department department;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_list_shall_return_valid_response() {
        testSpec.givenAValidRestRequest()
                .givenDepartmentRepositoryFindsAValidDepartment()
                .givenEmployeeRepositoryFindsByDepartment()
                .whenList()
                .thenDepartmentRepositoryFindByIdIsUsed()
                .thenEmployeeRepositoryFindByDepartmentIsUsed()
                .thenAValidRestResponseIsReturned();
    }

    @Test
    public void test_list_shall_throw_exception_if_department_not_found() {
        testSpec.givenAValidRestRequest()
                .givenDepartmentRepositoryDoesntFindAValidDepartment()
                .givenEmployeeRepositoryFindsByDepartment()
                .whenList()
                .thenDepartmentRepositoryFindByIdIsUsed()
                .thenDepartmentNotFoundExceptionIsThrown();
    }

    private class TestSpec {

        TestSpec givenAValidRestRequest() {
            request = EmployeeListDepartmentRestRequest.builder()
                    .departmentId(1)
                    .build();
            return this;
        }

        TestSpec givenDepartmentRepositoryFindsAValidDepartment() {
            department = Department.builder()
                    .id(1)
                    .description("description")
                    .name("name")
                    .build();

            when(cachedDepartmentRepository.findById(1)).thenReturn(Optional.of(department));

            return this;
        }

        TestSpec givenDepartmentRepositoryDoesntFindAValidDepartment() {
            when(cachedDepartmentRepository.findById(1)).thenReturn(Optional.empty());

            return this;
        }

        TestSpec givenEmployeeRepositoryFindsByDepartment() {
            Employee employee1 = Employee.builder()
                    .name("name1")
                    .age(1)
                    .cpf("0123456781")
                    .phone("0123456781")
                    .uuid("2544abcf-13e1-4238-ac0a-5fcd5f9a4710")
                    .email("user1@email.com")
                    .build();

            Employee employee2 = Employee.builder()
                    .name("name2")
                    .age(2)
                    .cpf("0123456782")
                    .phone("0123456782")
                    .uuid("90526909-dcdb-4ca8-a1fa-ce37831ba46b")
                    .email("user2@email.com")
                    .build();


            when(cachedEmployeeRepository.findByDepartment(any())).thenReturn(Arrays.asList(employee1, employee2));

            return this;
        }

        TestSpec whenList() {
            try {
                response = business.list(request);
            } catch (ServiceException e) {
                serviceException = e;
            }
            return this;
        }

        TestSpec thenDepartmentRepositoryFindByIdIsUsed() {
            verify(cachedDepartmentRepository, times(1)).findById(any());
            return this;
        }

        TestSpec thenEmployeeRepositoryFindByDepartmentIsUsed() {
            verify(cachedEmployeeRepository, times(1)).findByDepartment(any());
            return this;
        }


        TestSpec thenAValidRestResponseIsReturned() {
            assertThat(response).isNotNull();
            assertThat(response.getEmployeeList().size()).isEqualTo(2);
            assertThat(response.getEmployeeList().get(0).toString()).isEqualTo("EmployeeRestResponse(uuid=2544abcf-13e1-4238-ac0a-5fcd5f9a4710, name=name1, email=user1@email.com)");
            assertThat(response.getEmployeeList().get(1).toString()).isEqualTo("EmployeeRestResponse(uuid=90526909-dcdb-4ca8-a1fa-ce37831ba46b, name=name2, email=user2@email.com)");
            return this;
        }

        TestSpec thenDepartmentNotFoundExceptionIsThrown() {
            assertThat(serviceException).isNotNull();
            assertThat(404).isEqualTo(serviceException.getHttpStatus().value());
            assertThat(Utils.buildErrorFromEnumServiceException(
                    EnumServiceExceptions.DEPARTMENT_NOT_FOUND)).isEqualTo(serviceException.getExceptionList().get(0));
            return this;
        }
    }

}