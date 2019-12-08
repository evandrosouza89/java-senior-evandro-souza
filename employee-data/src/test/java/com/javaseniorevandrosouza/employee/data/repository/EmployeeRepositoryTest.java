package com.javaseniorevandrosouza.employee.data.repository;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Employee employee1;

    private Employee employee2;

    private Employee employeeFound;

    private Employee employeePersisted;

    private Department department;

    private Iterable<Employee> employeesFound;

    private TestSpec testSpec;

    private Integer employessCount;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_findById_shall_return_expected_employee() {
        testSpec.givenAValidDepartment()
                .givenAValidEmployee()
                .givenAPersistedEntry()
                .whenRepositoryFindById()
                .thenAEmployeeIsFound();
    }

    @Test
    public void test_findByUuid_shall_return_expected_employee() {
        testSpec.givenAValidDepartment()
                .givenAValidEmployee()
                .givenAPersistedEntry()
                .whenRepositoryFindByUuId()
                .thenAEmployeeIsFound();
    }

    @Test
    public void test_findAll_shall_return_expected_list_of_employees() {
        testSpec.givenAValidDepartment()
                .givenValidEmployees()
                .givenPersistedEntries()
                .whenRepositoryFindAll()
                .thenAllEmployeesAreListed();
    }

    @Test
    public void test_findByDepartment_shall_return_expected_list_of_employees() {
        testSpec.givenAValidDepartment()
                .givenValidEmployees()
                .givenPersistedEntries()
                .whenRepositoryFindByDepartment()
                .thenAllEmployeesAreListed();
    }

    @Test
    public void test_save_shall_save_repository() {
        testSpec.givenAValidDepartment()
                .givenAValidEmployee()
                .whenRepositorySave()
                .thenAEmployeeIsSaved();
    }

    @Test
    public void test_countByDepartment_shall_return_expected_count_of_employees() {
        testSpec.givenAValidDepartment()
                .givenValidEmployees()
                .givenPersistedEntries()
                .whenCountByDepartment()
                .thenExpectedEmployeeCountIsFound();
    }

    @Test
    public void test_countByDepartmentAndAgeLessThan_shall_return_expected_count_of_employees() {
        testSpec.givenAValidDepartment()
                .givenValidEmployeesYoungerThan18()
                .givenPersistedEntries()
                .whenRepositoryCountByDepartmentAndAgeLessThan(18)
                .thenExpectedEmployeeCountIsFound();
    }

    @Test
    public void test_countByDepartmentAndAgeGreaterThan_shall_return_expected_count_of_employees() {
        testSpec.givenAValidDepartment()
                .givenValidEmployeesOlderThan65()
                .givenPersistedEntries()
                .whenRepositoryCountByDepartmentAndAgeGreaterThan(65)
                .thenExpectedEmployeeCountIsFound();
    }

    private static Employee createAEmployee(int age, Department department) {
        return Employee.builder()
                .name("name" + age)
                .age(age)
                .cpf("012345678" + age)
                .phone("012345678" + age)
                .uuid(UUID.randomUUID().toString())
                .email(age + "@email.com")
                .department(department)
                .build();
    }

    private class TestSpec {

        TestSpec givenAValidEmployee() {
            employee1 = createAEmployee(1, department);
            return this;
        }

        TestSpec givenValidEmployees() {
            employee1 = createAEmployee(1, department);
            employee2 = createAEmployee(2, department);
            return this;
        }

        TestSpec givenValidEmployeesOlderThan65() {
            employee1 = createAEmployee(66, department);
            employee2 = createAEmployee(66, department);
            return this;
        }

        TestSpec givenValidEmployeesYoungerThan18() {
            employee1 = createAEmployee(17, department);
            employee2 = createAEmployee(17, department);
            return this;
        }

        TestSpec givenAValidDepartment() {
            department = departmentRepository.findById(1).isPresent() ? departmentRepository.findById(1).get() : null;
            return this;
        }

        TestSpec givenAPersistedEntry() {
            employeePersisted = entityManager.persist(employee1);
            return this;
        }

        TestSpec givenPersistedEntries() {
            entityManager.persist(employee1);
            entityManager.persist(employee2);
            return this;
        }

        TestSpec whenRepositoryFindById() {
            employeeFound = employeeRepository.findById(employeePersisted.getId()).orElse(null);
            return this;
        }

        TestSpec whenRepositoryFindByUuId() {
            employeeFound = employeeRepository.findByUuid(employeePersisted.getUuid()).orElse(null);
            return this;
        }

        TestSpec whenRepositoryFindByDepartment() {
            employeesFound = employeeRepository.findByDepartment(department);
            return this;
        }

        TestSpec whenCountByDepartment() {
            employessCount = employeeRepository.countByDepartment(department);
            return this;
        }

        TestSpec whenRepositoryCountByDepartmentAndAgeLessThan(int age) {
            employessCount = employeeRepository.countByDepartmentAndAgeLessThan(department, age);
            return this;
        }

        TestSpec whenRepositoryCountByDepartmentAndAgeGreaterThan(int age) {
            employessCount = employeeRepository.countByDepartmentAndAgeGreaterThan(department, age);
            return this;
        }

        TestSpec whenRepositoryFindAll() {
            employeesFound = employeeRepository.findAll();
            return this;
        }

        TestSpec whenRepositorySave() {
            employeeRepository.save(employee1);
            return this;
        }

        TestSpec thenAEmployeeIsFound() {
            assertThat(employee1.toString()).isEqualTo(employeeFound.toString());
            return this;
        }

        TestSpec thenAllEmployeesAreListed() {
            assertThat(employeesFound).size().isGreaterThanOrEqualTo(2);
            return this;
        }

        TestSpec thenAEmployeeIsSaved() {
            Employee employeeFound = employeeRepository.findByUuid(employee1.getUuid()).orElse(null);

            assertThat(employeeFound).isNotNull();

            assertThat(employee1.getId()).isEqualTo(employeeFound.getId());

            return this;
        }

        TestSpec thenExpectedEmployeeCountIsFound() {
            assertThat(employessCount).isGreaterThanOrEqualTo(2);
            return this;
        }
    }

}