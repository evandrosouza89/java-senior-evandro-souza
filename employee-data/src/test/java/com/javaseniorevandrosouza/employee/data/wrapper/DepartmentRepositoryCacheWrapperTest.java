package com.javaseniorevandrosouza.employee.data.wrapper;

import com.javaseniorevandrosouza.employee.data.entity.Department;
import com.javaseniorevandrosouza.employee.data.repository.DepartmentRepository;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration
public class DepartmentRepositoryCacheWrapperTest {

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
        public DepartmentRepositoryCacheWrapper departmentRepositoryCacheWrapper() {
            return new DepartmentRepositoryCacheWrapper(Mockito.mock(DepartmentRepository.class));
        }
    }

    @Autowired
    private DepartmentRepositoryCacheWrapper departmentRepositoryCacheWrapper;

    private Department department;

    private Department departmentFound;

    private TestSpec testSpec;

    @Before
    public void setupTestSpec() {
        testSpec = new TestSpec();
        Mockito.reset(departmentRepositoryCacheWrapper.getDepartmentRepository());
    }

    @Test
    public void test_findById_shall_return_not_cached_expected_department() {
        testSpec.givenAValidDepartment()
                .givenFindByIdReturnsValidDeparment()
                .whenWrappedRepositoryFindById()
                .thenADepartmentIsFound()
                .thenResultIsNotCached();
    }

    @Test
    public void test_findById_findByid_shall_return_cached_expected_department() {
        testSpec.givenAValidDepartment()
                .givenFindByIdReturnsValidDeparment()
                .whenWrappedRepositoryFindById()
                .whenWrappedRepositoryFindById()
                .whenWrappedRepositoryFindById()
                .thenADepartmentIsFound()
                .thenResultIsCached();
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
            department = createADepartment(123456);
            return this;
        }

        TestSpec givenFindByIdReturnsValidDeparment() {
            when(departmentRepositoryCacheWrapper.getDepartmentRepository().findById(any())).thenReturn(Optional.of(department));
            return this;
        }

        TestSpec whenWrappedRepositoryFindById() {
            departmentFound = departmentRepositoryCacheWrapper.findById(123456).orElse(null);

            return this;
        }

        TestSpec thenADepartmentIsFound() {
            assertThat(department.toString()).isEqualTo(departmentFound.toString());
            return this;
        }

        TestSpec thenResultIsNotCached() {
            Mockito.verify(departmentRepositoryCacheWrapper.getDepartmentRepository(), times(1)).findById(123456);

            return this;
        }

        TestSpec thenResultIsCached() {
            Mockito.verify(departmentRepositoryCacheWrapper.getDepartmentRepository(), times(0)).findById(123456);

            return this;
        }
    }

}