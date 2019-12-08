package com.javaseniorevandrosouza.employee.monolithic.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.javaseniorevandrosouza.employee.insert.*",
        "com.javaseniorevandrosouza.employee.list.department.*",
        "com.javaseniorevandrosouza.employee.remove.*",
        "com.javaseniorevandrosouza.employee.search.*",
        "com.javaseniorevandrosouza.employee.monolithic.*"})
@SpringBootApplication
public class MonolithicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonolithicApplication.class, args);
    }

}