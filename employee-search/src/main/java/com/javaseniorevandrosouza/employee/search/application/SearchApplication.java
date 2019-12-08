package com.javaseniorevandrosouza.employee.search.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.javaseniorevandrosouza.employee.search", "com.javaseniorevandrosouza.employee.common.exception"})
@EntityScan("com.javaseniorevandrosouza.employee.data.entity")
@EnableJpaRepositories("com.javaseniorevandrosouza.employee.data")
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

}