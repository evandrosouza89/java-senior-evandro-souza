package com.javaseniorevandrosouza.employee.list.department.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.javaseniorevandrosouza.employee.list.department"
        , "com.javaseniorevandrosouza.employee.common.exception"
        , "com.javaseniorevandrosouza.employee.data.wrapper"})
@EntityScan("com.javaseniorevandrosouza.employee.data.entity")
@EnableJpaRepositories("com.javaseniorevandrosouza.employee.data")
@SpringBootApplication
public class ListDepartmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListDepartmentApplication.class, args);
    }

}