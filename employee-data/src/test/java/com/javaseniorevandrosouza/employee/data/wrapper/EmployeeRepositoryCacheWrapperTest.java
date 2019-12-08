package com.javaseniorevandrosouza.employee.data.wrapper;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.entity.Employee;
import com.javaseniorevandrosouza.employee.data.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration
public class EmployeeRepositoryCacheWrapperTest {

    @Configuration
    @EnableCaching
    static class SpringConfig {
        @Bean
        public CacheManager cacheManager() {

            return new ConcurrentMapCacheManager() {

                @Override
                protected Cache createConcurrentMapCache(final String name) {
                    return new ConcurrentMapCache(name, false);
                }
            };
        }

        @Bean
        public EmployeeRepositoryCacheWrapper employeeRepositoryCacheWrapper() {
            return new EmployeeRepositoryCacheWrapper(Mockito.mock(EmployeeRepository.class));
        }
    }

    @Autowired
    private EmployeeRepositoryCacheWrapper employeeRepositoryCacheWrapper;

    private Department department;

    private List<Employee> employeeList;

    private List<Employee> employeesFoundList;

    private TestSpec testSpec;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        Mockito.reset(employeeRepositoryCacheWrapper.getEmployeeRepository());
    }

    @Test
    public void test_findById_shall_return_not_cached_expected_department() {
        testSpec.givenAValidDepartment()
                .givenFindByDepartmentReturnsValidEmployeeList()
                .whenWrappedRepositoryFindByDepartment()
                .thenAEmployeeListIsFound()
                .thenResultIsNotCached();
    }

    @Test
    public void test_findById_findByid_shall_return_cached_expected_department() {
        testSpec.givenAValidDepartment()
                .givenFindByDepartmentReturnsValidEmployeeList()
                .whenWrappedRepositoryFindByDepartment()
                .whenWrappedRepositoryFindByDepartment()
                .whenWrappedRepositoryFindByDepartment()
                .thenAEmployeeListIsFound()
                .thenResultIsCached();
    }

    private class TestSpec {

        TestSpec givenAValidDepartment() {
            department = Department.builder()
                    .id(1)
                    .name("name")
                    .description("description")
                    .build();

            return this;
        }

        TestSpec givenFindByDepartmentReturnsValidEmployeeList() {
            Employee employee1 = Employee.builder()
                    .name("name1")
                    .age(1)
                    .cpf("0123456781")
                    .phone("0123456781")
                    .uuid("2544abcf-13e1-4238-ac0a-5fcd5f9a4710")
                    .email("user1@email.com")
                    .department(department)
                    .build();

            Employee employee2 = Employee.builder()
                    .name("name2")
                    .age(2)
                    .cpf("0123456782")
                    .phone("0123456782")
                    .uuid("90526909-dcdb-4ca8-a1fa-ce37831ba46b")
                    .email("user2@email.com")
                    .department(department)
                    .build();

            employeeList = Arrays.asList(employee1, employee2);

            when(employeeRepositoryCacheWrapper.getEmployeeRepository().findByDepartment(any())).thenReturn(employeeList);

            return this;
        }

        TestSpec whenWrappedRepositoryFindByDepartment() {
            employeesFoundList = employeeRepositoryCacheWrapper.findByDepartment(department);

            return this;
        }

        TestSpec thenAEmployeeListIsFound() {
            assertThat(employeesFoundList.size()).isEqualTo(employeeList.size());
            assertThat(employeesFoundList.get(0).toString()).isEqualTo(employeeList.get(0).toString());
            assertThat(employeesFoundList.get(1).toString()).isEqualTo(employeeList.get(1).toString());

            return this;
        }

        TestSpec thenResultIsNotCached() {
            Mockito.verify(employeeRepositoryCacheWrapper.getEmployeeRepository(), times(1)).findByDepartment(department);

            return this;
        }

        TestSpec thenResultIsCached() {
            Mockito.verify(employeeRepositoryCacheWrapper.getEmployeeRepository(), times(0)).findByDepartment(department);

            return this;
        }
    }

}