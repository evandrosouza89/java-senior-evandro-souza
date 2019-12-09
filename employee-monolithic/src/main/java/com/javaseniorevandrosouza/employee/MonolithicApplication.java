package com.javaseniorevandrosouza.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.javaseniorevandrosouza.employee.insert.*",
        "com.javaseniorevandrosouza.employee.list.department.*",
        "com.javaseniorevandrosouza.employee.remove.*",
        "com.javaseniorevandrosouza.employee.search.*",
        "com.javaseniorevandrosouza.employee.*"})
public class MonolithicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonolithicApplication.class, args);
    }

}