package com.javaseniorevandrosouza.employee.insert.business;

import com.javaseniorevandrosouza.employee.common.exception.EnumServiceExceptions;
import com.javaseniorevandrosouza.employee.common.exception.ServiceException;
import com.javaseniorevandrosouza.employee.common.exception.Utils;
import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.repository.DepartmentRepository;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import com.javaseniorevandrosouza.employee.insert.rest.InsertRestRequest;
import com.javaseniorevandrosouza.employee.insert.rest.InsertRestResponse;
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
    private InsertServiceBusiness business;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    private InsertRestRequest request;

    private InsertRestResponse response;

    private TestSpec testSpec;

    private ServiceException serviceException;

    private Department department;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_insertEmployee_shall_persist_and_return_valid_response() {
        testSpec.givenAValidRestRequest(30)
                .givenDepartmentRepositoryFindsAValidDepartment()
                .whenInsertEmployee()
                .thenDepartmentRepositoryFindByIdIsUsed()
                .thenEmployeeRepositorySavesEmployee()
                .thenAValidRestResponseIsReturned();
    }

    @Test
    public void test_insertEmployee_shall_throw_exception_if_department_not_found() {
        testSpec.givenAValidRestRequest(30)
                .givenDepartmentRepositoryDoesntFindAValidDepartment()
                .whenInsertEmployee()
                .thenDepartmentRepositoryFindByIdIsUsed()
                .thenEmployeeRepositoryDoesntSaveEmployee()
                .thenDepartmentNotFoundExceptionIsThrown();
    }

    @Test
    public void test_insertEmployee_shall_throw_exception_if_business_rule_department_contains_more_than_20_percent_age_under_18() {
        testSpec.givenAValidRestRequest(17)
                .givenDepartmentRepositoryFindsAValidDepartment()
                .givenEmployeeRepositoryCountByDepartmentReturns1()
                .givenEmployeeRepositoryCountByDepartmentAndAgeLessThan18Returns1()
                .whenInsertEmployee()
                .thenDepartmentRepositoryFindByIdIsUsed()
                .thenEmployeeRepositoryCountByDepartmentAndAgeLessThanIsUsed()
                .thenEmployeeRepositoryDoesntSaveEmployee()
                .thenEmployeeUnder18ThresholdExceptionIsThrown();
    }

    @Test
    public void test_insertEmployee_shall_throw_exception_if_business_rule_department_contains_more_than_20_percent_age_over_65() {
        testSpec.givenAValidRestRequest(66)
                .givenDepartmentRepositoryFindsAValidDepartment()
                .givenEmployeeRepositoryCountByDepartmentReturns1()
                .givenEmployeeRepositoryCountByDepartmentAndAgeGreaterThanReturns1()
                .whenInsertEmployee()
                .thenDepartmentRepositoryFindByIdIsUsed()
                .thenEmployeeRepositoryCountByDepartmentAndAgeGreaterThanIsUsed()
                .thenEmployeeRepositoryDoesntSaveEmployee()
                .thenEmployeeOver65ThresholdExceptionIsThrown();
    }

    private class TestSpec {

        TestSpec givenAValidRestRequest(int age) {
            request = InsertRestRequest.builder()
                    .age(age)
                    .cpf("08895987896")
                    .departmentId(1)
                    .email("user@email.com")
                    .name("name")
                    .phone("25699874552")
                    .build();
            return this;
        }

        TestSpec givenDepartmentRepositoryFindsAValidDepartment() {
            department = Department.builder()
                    .id(1)
                    .description("description")
                    .name("name")
                    .build();

            when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

            return this;
        }

        TestSpec givenDepartmentRepositoryDoesntFindAValidDepartment() {
            when(departmentRepository.findById(1)).thenReturn(Optional.empty());

            return this;
        }

        TestSpec givenEmployeeRepositoryCountByDepartmentReturns1() {
            when(employeeRepository.countByDepartment(department)).thenReturn(1);

            return this;
        }

        TestSpec givenEmployeeRepositoryCountByDepartmentAndAgeLessThan18Returns1() {
            when(employeeRepository.countByDepartmentAndAgeLessThan(department, 18)).thenReturn(1);

            return this;
        }

        TestSpec givenEmployeeRepositoryCountByDepartmentAndAgeGreaterThanReturns1() {
            when(employeeRepository.countByDepartmentAndAgeGreaterThan(department, 65)).thenReturn(1);

            return this;
        }

        TestSpec whenInsertEmployee() {
            try {
                response = business.insertEmployee(request);
            } catch (ServiceException e) {
                serviceException = e;
            }
            return this;
        }

        TestSpec thenDepartmentRepositoryFindByIdIsUsed() {
            verify(departmentRepository, times(1)).findById(any());
            return this;
        }

        TestSpec thenEmployeeRepositorySavesEmployee() {
            verify(employeeRepository, times(1)).save(any());
            return this;
        }

        TestSpec thenEmployeeRepositoryDoesntSaveEmployee() {
            verify(employeeRepository, times(0)).save(any());
            return this;
        }

        TestSpec thenEmployeeRepositoryCountByDepartmentAndAgeLessThanIsUsed() {
            verify(employeeRepository, times(1)).countByDepartmentAndAgeLessThan(department, 18);
            return this;
        }

        TestSpec thenEmployeeRepositoryCountByDepartmentAndAgeGreaterThanIsUsed() {
            verify(employeeRepository, times(1)).countByDepartmentAndAgeGreaterThan(department, 65);
            return this;
        }

        TestSpec thenAValidRestResponseIsReturned() {
            assertThat(response.toString()).containsPattern("^InsertRestResponse\\(uuid=.{8}-.{4}-.{4}-.{4}-.{12}\\)$");
            return this;
        }

        TestSpec thenDepartmentNotFoundExceptionIsThrown() {
            assertThat(serviceException).isNotNull();
            assertThat(404).isEqualTo(serviceException.getHttpStatus().value());
            assertThat(Utils.buildErrorFromEnumServiceException(
                    EnumServiceExceptions.DEPARTMENT_NOT_FOUND)).isEqualTo(serviceException.getExceptionList().get(0));
            return this;
        }

        TestSpec thenEmployeeUnder18ThresholdExceptionIsThrown() {
            assertThat(serviceException).isNotNull();
            assertThat(400).isEqualTo(serviceException.getHttpStatus().value());
            assertThat(Utils.buildErrorFromEnumServiceException(
                    EnumServiceExceptions.EMPLOYEE_UNDER_18_THRESHOLD)).isEqualTo(serviceException.getExceptionList().get(0));
            return this;
        }

        TestSpec thenEmployeeOver65ThresholdExceptionIsThrown() {
            assertThat(serviceException).isNotNull();
            assertThat(400).isEqualTo(serviceException.getHttpStatus().value());
            assertThat(Utils.buildErrorFromEnumServiceException(
                    EnumServiceExceptions.EMPLOYEE_OVER_65_THRESHOLD)).isEqualTo(serviceException.getExceptionList().get(0));
            return this;
        }
    }

}
