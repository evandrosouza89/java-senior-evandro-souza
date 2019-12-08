package com.javaseniorevandrosouza.employee.search.business;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.search.rest.SearchRestResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class BusinessMapperTest {

    private BusinessMapper businessMapper;

    private TestSpec testSpec;

    private Employee employee;

    private SearchRestResponse searchRestResponse;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        businessMapper = Mappers.getMapper(BusinessMapper.class);
    }

    @Test
    public void test_employeeToSearchRestResponse_shall_and_return_valid_response() {
        testSpec.givenValidEmployee()
                .whenEmployeeToSearchRestResponse()
                .thenAValidSearchRequestIsMappedFromGivenInputs();
    }

    private class TestSpec {

        TestSpec givenValidEmployee() {
            Department department = Department.builder()
                    .id(1)
                    .build();

            employee = Employee.builder()
                    .name("name")
                    .age(50)
                    .cpf("0123456781")
                    .phone("0123456781")
                    .uuid(UUID.randomUUID().toString())
                    .email("user@email.com")
                    .department(department)
                    .build();

            return this;
        }

        TestSpec whenEmployeeToSearchRestResponse() {
            searchRestResponse = businessMapper.employeeToSearchRestResponse(employee);

            return this;
        }

        TestSpec thenAValidSearchRequestIsMappedFromGivenInputs() {
            assertThat(searchRestResponse).isNotNull();

            assertThat(employee.getAge()).isEqualTo(searchRestResponse.getAge());
            assertThat(employee.getCpf()).isEqualTo(searchRestResponse.getCpf());
            assertThat(employee.getDepartment().getId()).isEqualTo(searchRestResponse.getDepartmentId());
            assertThat(employee.getEmail()).isEqualTo(searchRestResponse.getEmail());
            assertThat(employee.getName()).isEqualTo(searchRestResponse.getName());

            return this;
        }

    }
}