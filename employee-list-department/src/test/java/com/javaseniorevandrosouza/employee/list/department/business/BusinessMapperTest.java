package com.javaseniorevandrosouza.employee.list.department.business;

import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.list.department.rest.EmployeeRestResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class BusinessMapperTest {

    private BusinessMapper businessMapper;

    private TestSpec testSpec;

    private List<Employee> request;

    private List<EmployeeRestResponse> employeeRestResponseList;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        businessMapper = Mappers.getMapper(BusinessMapper.class);
    }

    @Test
    public void test_employeeListToEmployeeListRestResponse_shall_and_return_valid_response() {
        testSpec.givenValidRestRequest()
                .whenEmployeeListToEmployeeListRestResponse()
                .thenAValidEmployeeRestResponseListIsMappedFromGivenInputs();
    }

    private class TestSpec {

        TestSpec givenValidRestRequest() {

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

            request = Arrays.asList(employee1, employee2);

            return this;
        }

        TestSpec whenEmployeeListToEmployeeListRestResponse() {
            employeeRestResponseList = businessMapper.employeeListToEmployeeListRestResponse(request);

            return this;
        }

        TestSpec thenAValidEmployeeRestResponseListIsMappedFromGivenInputs() {
            assertThat(employeeRestResponseList).isNotNull();

            assertThat(2).isEqualTo(employeeRestResponseList.size());

            assertThat(request.get(0).getEmail()).isEqualTo(employeeRestResponseList.get(0).getEmail());
            assertThat(request.get(0).getName()).isEqualTo(employeeRestResponseList.get(0).getName());

            assertThat(request.get(1).getEmail()).isEqualTo(employeeRestResponseList.get(1).getEmail());
            assertThat(request.get(1).getName()).isEqualTo(employeeRestResponseList.get(1).getName());

            return this;
        }

    }
}