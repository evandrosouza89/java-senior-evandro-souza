package com.javaseniorevandrosouza.employee.data.repository;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department1;

    private Department department2;

    private Department departmentFound;

    private Iterable<Department> departmentsFound;

    private TestSpec testSpec;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
    }

    @Test
    public void test_findById_shall_return_expected_department() {
        testSpec.givenAValidDepartment()
                .givenAPersistedEntry()
                .whenRepositoryFindById()
                .thenADepartmentIsFound();
    }

    @Test
    public void test_findAll_shall_return_expected_list_of_departments() {
        testSpec.givenValidDepartments()
                .givenPersistedEntries()
                .whenRepositoryFindAll()
                .thenAllDepartmentsAreListed();
    }

    @Test
    public void test_save_shall_save_department() {
        testSpec.givenAValidDepartment()
                .whenDepartmentSave()
                .thenADepartmentIsSaved();
    }

    private static Department createADepartment(int id) {
        return Department.builder()
                .id(id)
                .name("name" + id)
                .description("description" + id)
                .build();
    }

    private class TestSpec {

        TestSpec givenAValidDepartment() {
            department1 = createADepartment(123456);
            return this;
        }

        TestSpec givenValidDepartments() {
            department1 = createADepartment(123456);
            department2 = createADepartment(234567);
            return this;
        }

        TestSpec givenAPersistedEntry() {
            entityManager.persist(department1);
            return this;
        }

        TestSpec givenPersistedEntries() {
            entityManager.persist(department1);
            entityManager.persist(department2);
            return this;
        }

        TestSpec whenRepositoryFindById() {
            departmentFound = departmentRepository.findById(123456).orElse(null);
            return this;
        }

        TestSpec whenRepositoryFindAll() {
            departmentsFound = departmentRepository.findAll();
            return this;
        }

        TestSpec whenDepartmentSave() {
            departmentRepository.save(department1);
            return this;
        }

        TestSpec thenADepartmentIsFound() {
            assertThat(department1.toString()).isEqualTo(departmentFound.toString());
            return this;
        }

        TestSpec thenAllDepartmentsAreListed() {
            assertThat(departmentsFound).size().isGreaterThanOrEqualTo(2);
            return this;
        }

        TestSpec thenADepartmentIsSaved() {
            Department departmentFound = departmentRepository.findById(123456).orElse(null);

            assertThat(departmentFound).isNotNull();

            assertThat(department1.getId()).isEqualTo(departmentFound.getId());

            return this;
        }
    }
}