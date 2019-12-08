package com.javaseniorevandrosouza.employee.insert.business;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.insert.rest.InsertRestRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
public class BusinessMapperTest {

    private BusinessMapper businessMapper;

    private TestSpec testSpec;

    private InsertRestRequest request;

    private String uuid;

    private Department department;

    private Employee employee;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        businessMapper = Mappers.getMapper(BusinessMapper.class);
    }

    @Test
    public void test_insertRestRequestAndUUIDAndDepartmentToEmployee_shall_and_return_valid_response() {
        testSpec.givenValidRestRequest()
                .givenValidDepartment()
                .givenValidUuid()
                .whenInsertRestRequestAndUUIDAndDepartmentToEmployee()
                .thenAValidEmployeeIsMappedFromGivenInputs();
    }

    private class TestSpec {

        TestSpec givenValidRestRequest() {
            request = InsertRestRequest.builder()
                    .age(99)
                    .cpf("cpf")
                    .departmentId(1)
                    .email("email")
                    .name("name")
                    .phone("phone")
                    .build();

            return this;
        }

        TestSpec givenValidUuid() {
            uuid = "6580fc2d-0c73-42ba-b301-471a90887970";

            return this;
        }

        TestSpec givenValidDepartment() {
            department = Department.builder()
                    .id(1)
                    .description("description")
                    .name("name")
                    .build();

            return this;
        }

        TestSpec whenInsertRestRequestAndUUIDAndDepartmentToEmployee() {
            employee = businessMapper.insertRestRequestAndUUIDAndDepartmentToEmployee(request, uuid, department);

            return this;
        }

        TestSpec thenAValidEmployeeIsMappedFromGivenInputs() {
            assertThat(employee).isNotNull();

            assertThat(request.getAge()).isEqualTo(employee.getAge());
            assertThat(request.getCpf()).isEqualTo(employee.getCpf());
            assertThat(department).isEqualTo(employee.getDepartment());
            assertThat(request.getEmail()).isEqualTo(employee.getEmail());
            assertThat(request.getName()).isEqualTo(employee.getName());

            return this;
        }

    }
}